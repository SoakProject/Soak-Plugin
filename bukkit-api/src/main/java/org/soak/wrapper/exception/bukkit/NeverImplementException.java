package org.soak.wrapper.exception.bukkit;

public class NeverImplementException extends RuntimeException {

    public NeverImplementException(String methodName) {
        super(methodName + " will never be implemented");
    }
}
