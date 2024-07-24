package org.soak.io.properties;

import java.util.Optional;

public class OnlineModeProperty implements PropertiesNode<Boolean> {

    private Boolean mode;

    @Override
    public String name() {
        return "online-mode";
    }

    @Override
    public Optional<Boolean> value() {
        return Optional.ofNullable(this.mode);
    }

    @Override
    public Boolean defaultValue() {
        return true;
    }

    @Override
    public void setMemoryValue(String value) {
        this.mode = Boolean.parseBoolean(value);
    }
}
