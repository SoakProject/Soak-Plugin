package org.soak.wrapper.block.data;

import org.bukkit.block.data.Waterlogged;
import org.spongepowered.api.data.Keys;

public interface SoakWaterLogged extends CommonBlockData, Waterlogged {

    @Override
    default boolean isWaterlogged() {
        return this.sponge().get(Keys.IS_WATERLOGGED).orElse(false);
    }

    @Override
    default void setWaterlogged(boolean waterlogged) {
        var blockstate = this.sponge()
                .with(Keys.IS_WATERLOGGED, waterlogged)
                .orElseThrow(() -> new RuntimeException(this.sponge().asString() + " does not support waterlogged"));
        this.setSponge(blockstate);
    }
}
