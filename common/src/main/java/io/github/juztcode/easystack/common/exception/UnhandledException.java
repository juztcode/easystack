package io.github.juztcode.easystack.common.exception;

public class UnhandledException extends RuntimeException {
    public UnhandledException(Throwable cause) {
        super("Unhandled exception", cause);
    }
}