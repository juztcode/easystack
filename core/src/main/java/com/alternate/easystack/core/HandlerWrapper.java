package com.alternate.easystack.core;

public class HandlerWrapper {

    private final Class<? extends Request> requestType;
    private final Class<? extends Response> responseType;
    private final Handler handler;

    public HandlerWrapper(Class<? extends Request> requestType, Class<? extends Response> responseType, Handler handler) {
        this.requestType = requestType;
        this.responseType = responseType;
        this.handler = handler;
    }

    public Class<? extends Request> getRequestType() {
        return requestType;
    }

    public Class<? extends Response> getResponseType() {
        return responseType;
    }

    public Handler getHandler() {
        return handler;
    }
}
