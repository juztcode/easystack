package com.alternate.easystack.example;

import com.alternate.easystack.core.Response;

public class GenericResponse implements Response {
    private final boolean status;
    private final String message;

    public GenericResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
