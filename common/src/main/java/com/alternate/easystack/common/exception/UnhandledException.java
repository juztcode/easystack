package com.alternate.easystack.common.exception;

public class UnhandledException extends RuntimeException {
    public UnhandledException(Throwable cause) {
        super("Unhandled exception", cause);
    }
}