package org.soak.plugin.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Singleton<T> implements Supplier<T> {

    private final @NotNull Supplier<T> getter;
    private T value;

    public Singleton(@NotNull Supplier<T> getter) {
        this.getter = getter;
    }

    @Override
    public T get() {
        if (this.value == null) {
            this.value = this.getter.get();
        }
        return this.value;
    }
}
