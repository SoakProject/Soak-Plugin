package org.soak.wrapper.block.data.type;

import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakHalfMap;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.soak.wrapper.block.data.SoakFourDirectional;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.BlockStateKeys;
import org.spongepowered.api.data.Keys;

public class SoakBed extends AbstractBlockData implements Bed, SoakFourDirectional {


    public SoakBed(BlockState state) {
        super(state);
    }

    @NotNull
    @Override
    public Part getPart() {
        return this
                .spongeState
                .get(BlockStateKeys.BED_PART)
                .map(SoakHalfMap::toBukkit)
                .orElseThrow(() -> new RuntimeException("Bed Part cannot be found with " + this.spongeState.asString()));
    }

    @Override
    public void setPart(@NotNull Bed.Part part) {
        var type = SoakHalfMap.toSponge(part);
        this.spongeState = this
                .spongeState
                .with(BlockStateKeys.BED_PART, type.get())
                .orElseThrow(() -> new RuntimeException("Bed part is not supported on " + this.spongeState.asString()));
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
