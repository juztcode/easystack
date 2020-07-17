package com.alternate.easystack.core;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.alternate.easystack.common.exception.UnhandledExceptionHandler.unhandled;

public class HandlerManager {
    private final DbService dbService;
    private final Map<String, Handler> handlersMap = new HashMap<>();

    public HandlerManager(DbService dbService) {
        this.dbService = dbService;
    }

    public void register(String path, Class<? extends Handler> handler) {
        Handler instance = unhandled(handler::newInstance);
        handlersMap.put(path, instance);
    }

    public void unregister(String path) {
        handlersMap.remove(path);
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> T invoke(String path, Request request) {
        return (T) handlersMap.get(path).apply(new Context(dbService), request);
    }

    public Stream<Map.Entry<String, Handler>> stream() {
        return handlersMap.entrySet().stream();
    }
}
