package com.alternate.easystack.core;

import org.rapidoid.http.fast.On;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RapidoidWebApi implements WebApi {

    private final String address;
    private final int port;

    public RapidoidWebApi(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void start(Application application) {
        On.address(address);
        On.port(port);

        for (Map.Entry<String, Service> serviceEntry : application.getServiceMap().entrySet()) {
            String serviceName = serviceEntry.getKey();

            for (Map.Entry<String, HandlerWrapper> handlerEntry : serviceEntry.getValue().getHandlersMap().entrySet()) {
                String handlerName = handlerEntry.getKey();

                On.post(toPath(serviceName, handlerName)).plain(req -> handlerEntry.getValue().invokeJson(toJson(req.body())));
            }
        }
    }

    private static String toPath(String serviceName, String handlerName) {
        return "/" + serviceName + "/" + handlerName;
    }

    private static String toJson(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }
}
