package io.github.juztcode.easystack.core;

public class GenericResponse implements Response {
    private final boolean status;
    private final String message;
    private final Object data;

    public GenericResponse(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "status=" + status +
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
        return new GenericResponse(true, null, null);
    }

    public static GenericResponse ok(String message) {
        return new GenericResponse(true, message, null);
    }

    public static GenericResponse ok(Object data) {
        return new GenericResponse(true, null, data);
    }

    public static GenericResponse error(String message) {
        return new GenericResponse(false, message, null);
    }

    public static GenericResponse error(Throwable error) {
        return new GenericResponse(false, error.getMessage(), null);
    }
}
