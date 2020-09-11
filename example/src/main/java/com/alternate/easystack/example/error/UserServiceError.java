package com.alternate.easystack.example.error;

import com.alternate.easystack.core.ErrorCode;

public enum UserServiceError implements ErrorCode {
    DUPLICATE_USER(1, "User already registered for username: {0}");

    private final int code;
    private final String pattern;

    UserServiceError(int code, String pattern) {
        this.code = code;
        this.pattern = pattern;
    }

    @Override
    public int getErrorCode() {
        return code;
    }

    @Override
    public String getErrorPattern() {
        return pattern;
    }
}
