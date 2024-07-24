package org.soak.wrapper.block.data.type;

import org.bukkit.Axis;
import org.bukkit.block.data.type.WallSign;
import org.jetbrains.annotations.NotNull;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.soak.wrapper.block.data.SoakFourDirectional;
import org.soak.wrapper.block.data.SoakOrientable;
import org.soak.wrapper.block.data.SoakWaterLogged;
import org.spongepowered.api.block.BlockState;

import java.util.Set;

public class SoakWallSign extends AbstractBlockData implements WallSign, SoakWaterLogged, SoakFourDirectional, SoakOrientable {

    public SoakWallSign(BlockState state) {
        super(state);
    }

    @Override
    public @NotNull AbstractBlockData clone() {
        return new SoakWallSign(this.sponge());
    }

    @Override
    public @NotNull Set<Axis> getAxes() {
        return Set.of(Axis.X, Axis.Z);
    }
}
