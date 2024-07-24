package org.soak.config.node;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public class BooleanConfigNode implements ConfigNode<Boolean> {

    private Object[] node;

    public BooleanConfigNode(Object... node) {
        this.node = node;
    }

    @Override
    public Object[] node() {
        return this.node;
    }

    @Override
    public Optional<Boolean> parse(ConfigurationNode node) {
        var targetNode = node.node(node());
        if (targetNode.empty()) {
            return Optional.empty();
        }
        return Optional.of(targetNode.getBoolean());
    }

    @Override
    public void set(ConfigurationNode node, Boolean value) throws SerializationException {
        node.node(node()).set(value);
    }
}
