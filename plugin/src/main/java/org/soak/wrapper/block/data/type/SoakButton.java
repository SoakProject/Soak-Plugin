package org.soak.wrapper.block.data.type;

import org.jetbrains.annotations.NotNull;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.soak.wrapper.block.data.SoakSixDirectional;
import org.spongepowered.api.block.BlockState;

public class SoakButton extends AbstractBlockData implements SoakSixDirectional {
    public SoakButton(BlockState state) {
        super(state);
    }

    @Override
    public @NotNull SoakButton clone() {
        return new SoakButton(this.spongeState);
    }
}
