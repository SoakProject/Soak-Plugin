package org.soak.config.node;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ConfigNodeMeta<T> {

    private @Nullable T defaultValue;
    private @Nullable String comment;

    public Optional<T> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public ConfigNodeMeta<T> setDefaultValue(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public ConfigNodeMeta<T> setComment(@Nullable String comment) {
        this.comment = comment;
        return this;
    }
}
