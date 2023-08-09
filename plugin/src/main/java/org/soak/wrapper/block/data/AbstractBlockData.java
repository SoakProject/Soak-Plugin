package org.soak.wrapper.block.data;

import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;
import org.spongepowered.api.block.BlockState;

public abstract class AbstractBlockData implements BlockData {

    protected BlockState spongeState;

    protected AbstractBlockData(BlockState state) {
        this.spongeState = state;
    }

    public static AbstractBlockData createBlockData(BlockState state) {
        return new SoakBlockData(state);
    }

    public BlockState getSponge() {
        return this.spongeState;
    }

    @Override
    public abstract @NotNull AbstractBlockData clone();

    @Override
    public boolean matches(BlockData arg0) {
        throw NotImplementedException.createByLazy(BlockData.class, "matches", BlockData.class);
    }

    @Override
    public @NotNull BlockData merge(@NotNull BlockData arg0) {
        throw NotImplementedException.createByLazy(BlockData.class, "merge", BlockData.class);
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.getBlockMaterial(this.spongeState.type());
    }

    @Override
    public @NotNull String getAsString(boolean hideUnspecified) {
        //TODO hideUnspecified
        return this.spongeState.asString();
    }

    @Override
    public @NotNull String getAsString() {
        return getAsString(true);
    }

    @Override
    public @NotNull SoundGroup getSoundGroup() {
        throw NotImplementedException.createByLazy(BlockData.class, "getSoundGroup");
    }
}
