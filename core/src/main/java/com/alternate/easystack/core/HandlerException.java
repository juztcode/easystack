package com.alternate.easystack.core;

public class HandlerException extends RuntimeException {

    private final String errorGroup;
    private final int errorCode;

    public HandlerException(ErrorCode errorCode, String message) {
        super(message);
        this.errorGroup = errorCode.getClass().getName();
        this.errorCode = errorCode.getErrorCode();
    }

    public HandlerException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorGroup = errorCode.getClass().getName();
        this.errorCode = errorCode.getErrorCode();
    }

    public String getErrorGroup() {
        return errorGroup;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
