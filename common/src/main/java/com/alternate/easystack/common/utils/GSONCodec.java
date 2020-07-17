package com.alternate.easystack.common.utils;

import com.alternate.easystack.common.concurrent.ThreadLocalWithDefault;
import com.google.gson.Gson;

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

    @SuppressWarnings("unchecked")
    public static <T> T decode(String type, String data) {
        Class clazz = classMap.computeIfAbsent(type, k -> unhandled(() -> Class.forName(k)));

        if (clazz == String.class) {
            return (T) data;
        }

        return (T) THR_GSON.get().fromJson(data, clazz);
    }
}
