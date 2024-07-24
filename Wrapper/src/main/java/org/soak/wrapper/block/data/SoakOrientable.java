package org.soak.wrapper.block.data;

import org.bukkit.Axis;
import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakDirectionMap;
import org.spongepowered.api.data.Keys;

public interface SoakOrientable extends CommonBlockData, Orientable {
    @Override
    default @NotNull Axis getAxis() {
        var spongeAxis = this.sponge()
                .get(Keys.AXIS)
                .orElseThrow(() -> new RuntimeException(this.getClass().getName() + " is not orientable"));
        return SoakDirectionMap.toBukkit(spongeAxis);
    }

    @Override
    default void setAxis(@NotNull Axis axis) {
        var spongeAxis = SoakDirectionMap.toSponge(axis);
        var blockState = this.sponge()
                .with(Keys.AXIS, spongeAxis)
                .orElseThrow(() -> new RuntimeException(this.getClass().getName() + " is not orientable"));
        this.setSponge(blockState);
    }
}
