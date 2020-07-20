package com.alternate.easystack.core;

import java.util.HashMap;
import java.util.Map;

public abstract class Service {
    private final DbService dbService;
    private final Map<String, HandlerWrapper> handlersMap = new HashMap<>();

    public Service(DbService dbService) {
        this.dbService = dbService;
    }

    @SuppressWarnings("unchecked")
    protected void registerHandler(Class<? extends Handler> handler) {
        String path = handler.getSimpleName();
        HandlerWrapper wrapper = new HandlerWrapper(handler, dbService);

        handlersMap.put(path, wrapper);
    }

    public Map<String, HandlerWrapper> getHandlersMap() {
        return handlersMap;
    }

    public HandlerWrapper getHandler(String handler) {
        HandlerWrapper wrapper = handlersMap.get(handler);

        if (wrapper == null) {
            throw new RuntimeException("No handler found for: " + handler);
        }

        return wrapper;
    }
}
