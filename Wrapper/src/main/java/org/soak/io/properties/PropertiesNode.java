package org.soak.io.properties;

import java.util.Optional;

public interface PropertiesNode<T> {

    String name();

    Optional<T> value();

    T defaultValue();

    void setMemoryValue(String value);

    default T orElse() {
        return value().orElseGet(this::defaultValue);
    }
}
