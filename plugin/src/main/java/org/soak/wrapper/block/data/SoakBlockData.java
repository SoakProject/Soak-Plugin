package org.soak.wrapper.block.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.block.BlockState;

public class SoakBlockData extends AbstractBlockData {
    public SoakBlockData(BlockState state) {
        super(state);
    }

    @Override
    public @NotNull AbstractBlockData clone() {
        return new SoakBlockData(this.sponge());
    }
}
