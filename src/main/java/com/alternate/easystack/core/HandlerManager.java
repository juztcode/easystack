package com.alternate.easystack.core;

import com.alternate.easystack.services.DbService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HandlerManager {
    private final DbService dbService;
    private final Map<String, Handler> handlersMap = new HashMap<>();

    public HandlerManager(DbService dbService) {
        this.dbService = dbService;
    }

    public void add(String path, Handler handler) {
        handlersMap.put(path, handler);
    }

    public void remove(String path) {
        handlersMap.remove(path);
    }

    public Handler get(String path) {
        return handlersMap.get(path);
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> T invoke(String path, Request request) {
        return (T) handlersMap.get(path).apply(new Context(dbService), request);
    }

    public Stream<Map.Entry<String, Handler>> stream() {
        return handlersMap.entrySet().stream();
    }
}
