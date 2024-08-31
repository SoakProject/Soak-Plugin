package org.soak.config.node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public class BooleanConfigNode extends AbstractConfigNode<Boolean> {

    public BooleanConfigNode(@NotNull Object... node) {
        this(null, node);
    }

    public BooleanConfigNode(@Nullable ConfigNodeMeta meta, @NotNull Object... node) {
        super(meta, node);
    }

    @Override
    public Optional<Boolean> getValue(CommentedConfigurationNode targetNode) {
        if (targetNode.empty()) {
            return Optional.empty();
        }
        return Optional.of(targetNode.getBoolean());
    }

    @Override
    public void setValue(CommentedConfigurationNode node, Boolean value) throws SerializationException {
        node.set(value);
    }

    @Override
    public Optional<Boolean> failSafeDefault() {
        return Optional.of(false);
    }
}
