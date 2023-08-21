package org.soak.wrapper.block.data;

import org.bukkit.block.data.BlockData;
import org.spongepowered.api.block.BlockState;

public interface CommonBlockData extends BlockData {

    BlockState sponge();

    CommonBlockData setSponge(BlockState state);
}
