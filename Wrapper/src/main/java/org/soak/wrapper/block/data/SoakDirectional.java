package org.soak.wrapper.block.data;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakDirectionMap;
import org.spongepowered.api.data.Keys;

public interface SoakDirectional extends CommonBlockData, Directional {

    @Override
    default @NotNull BlockFace getFacing() {
        return this.sponge()
                .get(Keys.DIRECTION)
                .map(SoakDirectionMap::toBukkit)
                .orElseThrow(() -> new RuntimeException("Cannot get the direction of a " + getAsString() + " in " + getClass().getSimpleName()));
    }

    @Override
    default void setFacing(@NotNull BlockFace facing) {
        var state = this.sponge()
                .with(Keys.DIRECTION, SoakDirectionMap.toSponge(facing))
                .orElseThrow(() -> new RuntimeException("Cannot set the direction of a " + getAsString() + " in " + getClass().getSimpleName()));
        this.setSponge(state);
    }

    @Override
    default Location withDefaultY(@NotNull Location loc) {
        float yaw = SoakDirectionMap.toYaw(this.getFacing());
        loc.setYaw(yaw);
        return loc;
    }
}
