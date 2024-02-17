package org.soak.wrapper.block.data.type;

import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.soak.wrapper.block.data.SoakFourDirectional;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Keys;

public class SoakBed extends AbstractBlockData implements Bed, SoakFourDirectional {


    public SoakBed(BlockState state) {
        super(state);
    }

    @NotNull
    @Override
    public Part getPart() {
        throw NotImplementedException.createByLazy(Bed.class, "getPart");
    }

    @Override
    public void setPart(@NotNull Bed.Part part) {
        throw NotImplementedException.createByLazy(Bed.class, "setPart", Bed.Part.class);
    }

    @Override
    public boolean isOccupied() {
        return this.sponge().get(Keys.IS_OCCUPIED).orElse(false);
    }

    @Override
    public void setOccupied(boolean b) {
        this.spongeState = this.spongeState.with(Keys.IS_OCCUPIED, b).orElseThrow(() -> new RuntimeException(this.spongeState.asString() + " doesn't support occupied. This should be for Bed"));
    }

    @Override
    public @NotNull AbstractBlockData clone() {
        return new SoakBed(this.sponge());
    }
}
