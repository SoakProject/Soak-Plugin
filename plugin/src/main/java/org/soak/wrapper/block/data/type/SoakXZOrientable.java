package org.soak.wrapper.block.data.type;

import org.bukkit.Axis;
import org.jetbrains.annotations.NotNull;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.soak.wrapper.block.data.SoakOrientable;
import org.spongepowered.api.block.BlockState;

import java.util.Set;

public class SoakXZOrientable extends AbstractBlockData implements SoakOrientable {
    public SoakXZOrientable(BlockState state) {
        super(state);
    }

    @Override
    public @NotNull AbstractBlockData clone() {
        return new SoakXZOrientable(this.spongeState);
    }

    @Override
    public @NotNull Set<Axis> getAxes() {
        return Set.of(Axis.X, Axis.Z);
    }
}
