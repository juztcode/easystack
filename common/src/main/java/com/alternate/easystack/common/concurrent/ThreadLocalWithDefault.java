package com.alternate.easystack.common.concurrent;

import java.util.function.Supplier;

public class ThreadLocalWithDefault<T> extends ThreadLocal<T> {

    private final Supplier<T> valueSupplier;

    public ThreadLocalWithDefault(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    @Override
    protected T initialValue() {
        return valueSupplier.get();
    }
}
