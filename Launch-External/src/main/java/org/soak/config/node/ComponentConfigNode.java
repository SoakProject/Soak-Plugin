package org.soak.config.node;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public class ComponentConfigNode extends AbstractConfigNode<Component> {

    public ComponentConfigNode(@NotNull Object... node) {
        this(null, node);
    }

    public ComponentConfigNode(@Nullable ConfigNodeMeta meta, @NotNull Object... node) {
        super(meta, node);
    }

    @Override
    public Optional<Component> getValue(CommentedConfigurationNode node) {
        String gson = node.getString();
        if (gson == null) {
            return Optional.empty();
        }
        var component = GsonComponentSerializer.gson().deserialize(gson);
        return Optional.of(component);
    }

    @Override
    public void setValue(CommentedConfigurationNode node, Component value) throws SerializationException {
        node.set(GsonComponentSerializer.gson().serialize(value));
    }

    @Override
    public Optional<Component> failSafeDefault() {
        return Optional.empty();
    }
}
