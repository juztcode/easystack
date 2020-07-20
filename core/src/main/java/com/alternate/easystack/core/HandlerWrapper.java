package com.alternate.easystack.core;

import com.alternate.easystack.common.utils.GSONCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import static com.alternate.easystack.common.exception.UnhandledExceptionHandler.unhandled;

public class HandlerWrapper {

    private final DbService dbService;
    private final Class<? extends Request> requestType;
    private final Class<? extends Response> responseType;
    private final Class<? extends Handler> handlerType;
    private final Handler handler;
    private final Logger logger;

    @SuppressWarnings("unchecked")
    public HandlerWrapper(Class<? extends Handler> handlerType, DbService dbService) {
        this.dbService = dbService;

        this.requestType = (Class<? extends Request>) ((ParameterizedType) handlerType.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        this.responseType = (Class<? extends Response>) ((ParameterizedType) handlerType.getGenericInterfaces()[0]).getActualTypeArguments()[1];
        this.handlerType = handlerType;

        this.handler = unhandled(handlerType::newInstance);
        this.logger = LoggerFactory.getLogger(handlerType);
    }

    public Class<? extends Request> getRequestType() {
        return requestType;
    }

    public Class<? extends Response> getResponseType() {
        return responseType;
    }

    public Class<? extends Handler> getHandlerType() {
        return handlerType;
    }

    public Handler getHandler() {
        return handler;
    }

    public Logger getLogger() {
        return logger;
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> T invoke(Request request) {
        if (!request.getClass().equals(requestType)) {
            throw new HandlerException("Invalid request type: " + request.getClass());
        }

        ContextEx contextEx = new ContextEx(dbService, logger);

        try {
            T response = (T) handler.apply(contextEx, request);
            contextEx.commitTx();
            return response;
        } catch (Throwable e) {
            throw (e instanceof HandlerException) ? (HandlerException) e : new HandlerException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> T invoke(Map<String, Object> request) {
        Request requestObj = GSONCodec.decode(requestType, request);
        return invoke(requestObj);
    }

    public String invokeJson(String request) {
        Request requestObj = GSONCodec.decode(requestType, request);
        Response responseObj = invoke(requestObj);
        return GSONCodec.encode(responseObj);
    }
}
