package com.alternate.easystack.core;

import java.text.MessageFormat;

public interface ErrorCode {
    int getErrorCode();

    String getErrorPattern();

    default String getMessage(Object... args) {
        return MessageFormat.format(getErrorPattern(), (Object[]) args);
    }
}
