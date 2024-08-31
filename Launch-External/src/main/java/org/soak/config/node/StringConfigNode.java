package org.soak.config.node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public class StringConfigNode extends AbstractConfigNode<String> {

    public StringConfigNode(@NotNull Object... node) {
        this(null, node);
    }

    public StringConfigNode(@Nullable ConfigNodeMeta meta, @NotNull Object... node) {
        super(meta, node);
    }

    @Override
    public Optional<String> getValue(CommentedConfigurationNode targetNode) {
        if (targetNode.empty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(targetNode.getString());
    }

    @Override
    public void setValue(CommentedConfigurationNode node, String value) throws SerializationException {
        node.set(value);
    }

    @Override
    public Optional<String> failSafeDefault() {
        return Optional.empty();
    }
}
