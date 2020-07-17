package com.alternate.easystack.core;

public class HandlerException extends RuntimeException {

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(Throwable cause) {
        super(cause);
    }
}
