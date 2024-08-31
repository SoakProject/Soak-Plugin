package org.soak.config.node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractConfigNode<T> implements ConfigNode<T> {

    private @NotNull
    final Object[] node;
    private @NotNull
    final ConfigNodeMeta<T> meta;

    public AbstractConfigNode(@Nullable ConfigNodeMeta<T> meta, @NotNull Object... node) {
        this.node = node;
        this.meta = Objects.requireNonNullElseGet(meta, ConfigNodeMeta::new);
    }

    protected abstract void setValue(CommentedConfigurationNode node, T value) throws SerializationException;

    protected abstract Optional<T> getValue(CommentedConfigurationNode node);

    protected abstract Optional<T> failSafeDefault();

    @Override
    public Object[] node() {
        return this.node;
    }

    @Override
    public Optional<T> parse(CommentedConfigurationNode node) {
        return getValue(node.node(this.node));
    }

    @Override
    public void set(CommentedConfigurationNode node, T value) throws SerializationException {
        var opComment = this.meta.getComment();
        var valueNode = node.node(this.node);
        opComment.ifPresent(valueNode::comment);
        setValue(valueNode, value);
    }

    @Override
    public Optional<T> getDefaultValue() {
        var opDefault = this.meta.getDefaultValue();
        if (opDefault.isPresent()) {
            return opDefault;
        }
        return failSafeDefault();
    }

    @Override
    public void setDefault(CommentedConfigurationNode node, boolean replaceCurrent) throws SerializationException {
        var opDefaultValue = this.getDefaultValue();
        if (opDefaultValue.isEmpty()) {
            return;
        }
        if (!replaceCurrent && !node.node(node()).isNull()) {
            return;
        }
        set(node, opDefaultValue.get());
    }
}
