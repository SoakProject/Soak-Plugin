package org.soak.wrapper.block.data;

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
