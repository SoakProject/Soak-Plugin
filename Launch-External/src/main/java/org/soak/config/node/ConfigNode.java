package org.soak.config.node;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public interface ConfigNode<T> {

    Object[] node();

    Optional<T> parse(CommentedConfigurationNode node);

    void set(CommentedConfigurationNode node, T value) throws SerializationException;

    void setDefault(CommentedConfigurationNode node, boolean replace) throws SerializationException;

    Optional<T> getDefaultValue();

}
