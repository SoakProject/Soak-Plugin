package org.soak.plugin.config.node;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public interface ConfigNode<T> {

    Object[] node();

    Optional<T> parse(ConfigurationNode node);

    void set(ConfigurationNode node, T value) throws SerializationException;

}
