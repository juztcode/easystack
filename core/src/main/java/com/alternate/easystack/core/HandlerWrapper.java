package com.alternate.easystack.core;

import org.slf4j.Logger;

public class HandlerWrapper {

    private final Class<? extends Request> requestType;
    private final Class<? extends Response> responseType;
    private final Class<? extends Handler> handlerType;
    private final Handler handler;
    private final Logger logger;

    public HandlerWrapper(Class<? extends Request> requestType, Class<? extends Response> responseType,
                          Class<? extends Handler> handlerType, Handler handler, Logger logger) {
        this.requestType = requestType;
        this.responseType = responseType;
        this.handlerType = handlerType;
        this.handler = handler;
        this.logger = logger;
    }

    public Class<? extends Request> getRequestType() {
        return requestType;
    }

    public Class<? extends Response> getResponseType() {
        return responseType;
    }

    public Class<? extends Handler> getHandlerType() {
        return handlerType;
    }

    public Handler getHandler() {
        return handler;
    }

    public Logger getLogger() {
        return logger;
    }
}
