package org.soak.plugin.config.node;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public class ComponentConfigNode implements ConfigNode<Component> {

    private final Object[] nodes;

    public ComponentConfigNode(Object... nodes) {
        this.nodes = nodes;
    }

    @Override
    public Object[] node() {
        return this.nodes;
    }

    @Override
    public Optional<Component> parse(ConfigurationNode node) {
        String gson = node.node(this.node()).getString();
        if (gson == null) {
            return Optional.empty();
        }
        var component = GsonComponentSerializer.gson().deserialize(gson);
        return Optional.of(component);
    }

    @Override
    public void set(ConfigurationNode node, Component value) throws SerializationException {
        node.set(GsonComponentSerializer.gson().serialize(value));
    }
}
