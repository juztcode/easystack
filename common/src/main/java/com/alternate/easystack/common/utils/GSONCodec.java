package com.alternate.easystack.common.utils;

import com.alternate.easystack.common.concurrent.ThreadLocalWithDefault;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.alternate.easystack.common.exception.UnhandledExceptionHandler.unhandled;

public class GSONCodec {

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

    public static <T> T decode(String type, String data) {
        Class clazz = classMap.computeIfAbsent(type, k -> unhandled(() -> Class.forName(k)));
        return decode(clazz, data);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decode(Class type, String data) {
        if (type == String.class) {
            return (T) data;
        }

        return (T) THR_GSON.get().fromJson(data, type);
    }

    public static <T> T decode(String type, Map<String, Object> data) {
        Class clazz = classMap.computeIfAbsent(type, k -> unhandled(() -> Class.forName(k)));
        return decode(clazz, data);
    }

    @SuppressWarnings("unchecked")
    public static <T> T decode(Class type, Map<String, Object> data) {
        JsonElement jsonElement = THR_GSON.get().toJsonTree(data);
        return (T) THR_GSON.get().fromJson(jsonElement, type);
    }
}
