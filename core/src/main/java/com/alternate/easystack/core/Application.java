package com.alternate.easystack.core;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static com.alternate.easystack.common.exception.UnhandledExceptionHandler.unhandled;

public class Application {
    private final Map<String, Service> serviceMap = new HashMap<>();
    private final DbService dbService;

    public Application(DbService dbService) {
        this.dbService = dbService;
    }

    public void registerService(Class<? extends Service> service) {
        Constructor<? extends Service> constructor = unhandled(() -> service.getConstructor(DbService.class));
        Service instance = unhandled(() -> constructor.newInstance(dbService));
        serviceMap.put(service.getSimpleName(), instance);
    }

    public Map<String, Service> getServiceMap() {
        return serviceMap;
    }

    public Service getService(String service) {
        Service instance = serviceMap.get(service);

        if (instance == null) {
            throw new RuntimeException("No service found for: " + service);
        }

        return instance;
    }
}
