package org.soak.plugin.exception;

public class NotReadyException extends IllegalStateException {

    public NotReadyException(String error) {
        super(error);
    }
}
