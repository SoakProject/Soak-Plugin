package org.soak.config.node.properties;

import java.util.Optional;

public class SpawnProtectionProperty implements PropertiesNode<Integer> {

    private Integer spawn;

    @Override
    public String name() {
        return "spawn-protection";
    }

    @Override
    public Optional<Integer> value() {
        return Optional.ofNullable(this.spawn);
    }

    @Override
    public Integer defaultValue() {
        return 16;
    }

    @Override
    public void setMemoryValue(String value) {
        this.spawn = Integer.parseInt(value);
    }

    public void setMemoryValue(Integer value) {
        this.spawn = value;
    }
}
