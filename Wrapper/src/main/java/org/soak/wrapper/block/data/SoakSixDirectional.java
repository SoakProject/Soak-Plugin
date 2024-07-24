package org.soak.wrapper.block.data;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface SoakSixDirectional extends SoakDirectional {

    //this isnt exposed by Sponge .... somehow????
    @Override
    default @NotNull Set<BlockFace> getFaces() {
        return Set.of(BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN, BlockFace.UP);
    }
}
