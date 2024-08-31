package org.soak.config.node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ListConfigNode<T> extends AbstractConfigNode<List<T>> {

    private final ConfigNode<T> node;

    public ListConfigNode(@NotNull ConfigNode<T> node, Object... nodePath) {
        this(null, node, nodePath);
    }

    public ListConfigNode(@Nullable ConfigNodeMeta<List<T>> meta, @NotNull ConfigNode<T> node, Object... nodePath) {
        super(meta, nodePath);
        this.node = node;
    }

    @Override
    public Optional<List<T>> failSafeDefault() {
        return Optional.of(new LinkedList<>());
    }

    @Override
    public Optional<List<T>> getValue(CommentedConfigurationNode node) {
        if (node.isNull() || !node.isList()) {
            return Optional.empty();
        }
        var list = node
                .childrenList()
                .stream()
                .map(this.node::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return Optional.of(list);
    }

    @Override
    public void setValue(CommentedConfigurationNode node, List<T> value) throws SerializationException {
        if (value.isEmpty()) {
            node.set(new LinkedList<>());
            return;
        }

        for (var element : value) {
            node = node.appendListNode();
            this.node.set(node, element);
        }
    }
}
