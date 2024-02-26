package org.soak.wrapper.block.data;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.block.BlockState;

public interface CommonBlockData extends BlockData {

    BlockState sponge();

    CommonBlockData setSponge(BlockState state);

    default Location withDefaultY(@NotNull Location loc) {
        return loc;
    }
}
