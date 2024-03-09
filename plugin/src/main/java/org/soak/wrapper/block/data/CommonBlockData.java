package org.soak.wrapper.block.data;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.soak.utils.single.SoakSingleInstance;
import org.spongepowered.api.block.BlockState;

public interface CommonBlockData extends BlockData, SoakSingleInstance<BlockState> {

    BlockState sponge();

    default Location withDefaultY(@NotNull Location loc) {
        return loc;
    }

    @Override
    default boolean isSame(BlockState sponge) {
        return sponge.equals(this.sponge());
    }
}
