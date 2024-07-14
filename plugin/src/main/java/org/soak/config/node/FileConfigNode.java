package org.soak.config.node;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.util.Optional;

public class FileConfigNode implements ConfigNode<File> {

    private final Object[] nodes;

    public FileConfigNode(Object... nodes) {
        this.nodes = nodes;
    }

    @Override
    public Object[] node() {
        return this.nodes;
    }

    @Override
    public Optional<File> parse(ConfigurationNode node) {
        String path = node.node(this.node()).getString();
        if (path == null) {
            return Optional.empty();
        }
        return Optional.of(new File(path));
    }

    @Override
    public void set(ConfigurationNode node, File value) throws SerializationException {
        node.set(value.getPath());
    }
}
