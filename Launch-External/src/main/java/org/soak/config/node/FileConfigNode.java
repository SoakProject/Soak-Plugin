package org.soak.config.node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.util.Optional;

public class FileConfigNode extends AbstractConfigNode<File> {

    public FileConfigNode(@NotNull Object... node) {
        this(null, node);
    }

    public FileConfigNode(@Nullable ConfigNodeMeta meta, @NotNull Object... node) {
        super(meta, node);
    }

    @Override
    protected void setValue(CommentedConfigurationNode node, File value) throws SerializationException {
        node.set(value.getPath());
    }

    @Override
    protected Optional<File> getValue(CommentedConfigurationNode node) {
        String path = node.node(this.node()).getString();
        if (path == null) {
            return Optional.empty();
        }
        return Optional.of(new File(path));
    }

    @Override
    public Optional<File> failSafeDefault() {
        return Optional.empty();
    }
}
