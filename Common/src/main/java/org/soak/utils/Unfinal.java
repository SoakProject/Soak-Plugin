package org.soak.utils;

public class Unfinal<T> {

    private T value;

    public Unfinal() {

    }

    public Unfinal(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public T set(T value) {
        this.value = value;
        return value;
    }

    public void setNull() {
        this.value = null;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Unfinal<?>) {
            return this.value.equals(((Unfinal<?>) obj).value);
        }
        return this.value.equals(obj);
    }
}
