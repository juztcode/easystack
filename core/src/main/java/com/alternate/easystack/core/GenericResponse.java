package com.alternate.easystack.core;

public class GenericResponse implements Response {
    private final boolean status;
    private final int code;
    private final String message;
    private final Object data;

    public GenericResponse(boolean status, int code, String message, Object data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public static GenericResponse ok() {
        return new GenericResponse(true, 0, null, null);
    }

    public static GenericResponse ok(String message) {
        return new GenericResponse(true, 0, message, null);
    }

    public static GenericResponse ok(Object data) {
        return new GenericResponse(true, 0, null, data);
    }

    public static GenericResponse error(int code, Throwable error) {
        return new GenericResponse(false, code, error.getMessage(), null);
    }
}
