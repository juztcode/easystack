package com.alternate.easystack.example;

import com.alternate.easystack.core.Response;

public class GenericResponse implements Response {
    private final boolean status;
    private final Object data;

    public GenericResponse(boolean status, Object data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
