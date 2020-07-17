package com.alternate.easystack.core;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author randilfernando
 */
public class ErrorResponse implements Response {

    private final String message;
    private final String stacktrace;

    public ErrorResponse(String message, Throwable throwable) {
        this.message = message;
        this.stacktrace = extractStacktrace(throwable);
    }

    public String getMessage() {
        return message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    private static String extractStacktrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);

        return sw.toString();
    }
}
