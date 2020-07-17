package com.alternate.easystack.core;

import org.rapidoid.http.fast.On;

import java.nio.charset.StandardCharsets;

public class RapidoidWebApi implements WebApi {

    private final String address;
    private final int port;

    public RapidoidWebApi(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void start(HandlerManager handlerManager) {
        On.address(address);
        On.port(port);

        handlerManager.getRegisteredPaths().forEach(path -> {
            On.post(path).plain(req -> handlerManager.invokeJson(path, toJson(req.body())));
        });
    }

    private static String toJson(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }
}
