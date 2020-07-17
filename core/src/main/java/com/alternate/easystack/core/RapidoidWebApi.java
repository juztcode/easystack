package com.alternate.easystack.core;

import org.rapidoid.http.fast.On;

import java.nio.charset.StandardCharsets;

public class RapidoidWebApi implements WebApi {

    @Override
    public void start(HandlerManager handlerManager) {
        On.address("0.0.0.0");
        On.port(8080);

        handlerManager.getRegisteredPaths().forEach(path -> {
            On.post(path).plain(req -> handlerManager.invokeJson(path, toJson(req.body())));
        });
    }

    private static String toJson(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }
}
