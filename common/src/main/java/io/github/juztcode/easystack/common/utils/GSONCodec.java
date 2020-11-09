package io.github.juztcode.easystack.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.github.juztcode.easystack.common.concurrent.ThreadLocalWithDefault;
import io.github.juztcode.easystack.common.exception.UnhandledExceptionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GSONCodec {

    @SuppressWarnings("rawtypes")
    private static final Map<String, Class> classMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<Gson> THR_GSON = new ThreadLocalWithDefault<>(Gson::new);


    private GSONCodec() {
    }

    public static String encode(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        return THR_GSON.get().toJson(obj);
    }

    @SuppressWarnings("rawtypes")
    public static <T> T decode(String type, String data) {
        Class clazz = classMap.computeIfAbsent(type, k -> UnhandledExceptionHandler.unhandled(() -> Class.forName(k)));
        return decode(clazz, data);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T decode(Class type, String data) {
        if (type == String.class) {
            return (T) data;
        }

        return (T) THR_GSON.get().fromJson(data, type);
    }

    @SuppressWarnings("rawtypes")
    public static <T> T decode(String type, Map<String, Object> data) {
        Class clazz = classMap.computeIfAbsent(type, k -> UnhandledExceptionHandler.unhandled(() -> Class.forName(k)));
        return decode(clazz, data);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T decode(Class type, Map<String, Object> data) {
        JsonElement jsonElement = THR_GSON.get().toJsonTree(data);
        return (T) THR_GSON.get().fromJson(jsonElement, type);
    }

    @SuppressWarnings("rawtypes")
    public static <T> T clone(T object) {
        if (object == null) {
            return null;
        }

        Class clazz = object.getClass();
        return decode(clazz, encode(object));
    }
}
