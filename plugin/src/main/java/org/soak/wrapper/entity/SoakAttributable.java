package org.soak.wrapper.entity;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.plugin.exception.NotImplementedException;

public interface SoakAttributable extends Attributable {

    org.spongepowered.api.entity.Entity spongeEntity();

    @Override
    default @Nullable AttributeInstance getAttribute(@NotNull Attribute attribute) {
        throw NotImplementedException.createByLazy(Attributable.class, "getAttribute", Attribute.class);

    }

    @Override
    default void registerAttribute(@NotNull Attribute attribute) {
        throw NotImplementedException.createByLazy(Attributable.class, "registerAttribute", Attribute.class);
    }
}
