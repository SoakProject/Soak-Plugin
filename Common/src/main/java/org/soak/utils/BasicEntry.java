package org.soak.utils;

import java.util.Map;

public class BasicEntry<T, V> implements Map.Entry<T, V> {

    private final T key;
    private final V value;

    public BasicEntry(T key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public T getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public V setValue(V v) {
        throw new RuntimeException("Cannot set");
    }
}
