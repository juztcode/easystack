package com.alternate.easystack.core;

import com.alternate.easystack.common.utils.GSONCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.alternate.easystack.common.exception.UnhandledExceptionHandler.unhandled;

public class HandlerManager {
    private final DbService dbService;
    private final Map<String, HandlerWrapper> handlersMap = new HashMap<>();
    private final Map<Class, Logger> loggerMap = new HashMap<>();

    public HandlerManager(DbService dbService) {
        this.dbService = dbService;
    }

    @SuppressWarnings("unchecked")
    public void register(String path, Class<? extends Handler> handler) {
        Class<? extends Request> request = (Class<? extends Request>) ((ParameterizedType) handler.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        Class<? extends Response> response = (Class<? extends Response>) ((ParameterizedType) handler.getGenericInterfaces()[0]).getActualTypeArguments()[1];

        Handler instance = unhandled(handler::newInstance);
        handlersMap.put(path, new HandlerWrapper(request, response, instance));
    }

    public void unregister(String path) {
        handlersMap.remove(path);
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> T invoke(String path, Request request) {
        HandlerWrapper wrapper = handlersMap.get(path);

        Logger logger = loggerMap.computeIfAbsent(wrapper.getHandler().getClass(), LoggerFactory::getLogger);
        ContextEx contextEx = new ContextEx(dbService, logger);

        try {
            T response = (T) wrapper.getHandler().apply(contextEx, request);
            contextEx.commitTx();
            return response;
        } catch (Throwable e) {
            throw (e instanceof HandlerException) ? (HandlerException) e : new HandlerException(e);
        }
    }

    public String invokeJson(String path, String request) {
        HandlerWrapper wrapper = handlersMap.get(path);
        Request requestObj = GSONCodec.decode(wrapper.getRequestType(), request);
        Response responseObj = invoke(path, requestObj);
        return GSONCodec.encode(responseObj);
    }

    public Set<String> getRegisteredPaths() {
        return handlersMap.keySet();
    }
}
