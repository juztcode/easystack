package com.alternate.easystack.core;

import com.alternate.easystack.common.utils.GSONCodec;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.alternate.easystack.common.exception.UnhandledExceptionHandler.unhandled;

public class HandlerManager {
    private final DbService dbService;
    private final Map<String, HandlerWrapper> handlersMap = new HashMap<>();

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
        return (T) handlersMap.get(path).getHandler().apply(new Context(dbService), request);
    }

    @SuppressWarnings("unchecked")
    public String invokeJson(String path, String request) {
        HandlerWrapper wrapper = handlersMap.get(path);
        Response responseObj = (Response) wrapper.getHandler().apply(new Context(dbService), GSONCodec.decode(wrapper.getRequestType(), request));
        return GSONCodec.encode(responseObj);
    }

    public Set<String> getRegisteredPaths() {
        return handlersMap.keySet();
    }
}
