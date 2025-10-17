package org.soak.wrapper.block.data.type;

import org.bukkit.block.data.Levelled;
import org.jetbrains.annotations.NotNull;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.BlockStateKeys;

public class SoakCauldron extends AbstractBlockData implements Levelled {

    public SoakCauldron(BlockState state) {
        super(state);
    }

    @Override
    public int getLevel() {
        return this.sponge().getInt(BlockStateKeys.LEVEL_CAULDRON).orElse(0);
    }

    @Override
    public void setLevel(int level) {
        this.spongeState = this.sponge()
                .with(BlockStateKeys.LEVEL_CAULDRON, level)
                .orElseThrow(() -> new RuntimeException("Does not support BlockStateKeys.LEVEL_CAULDRON. Instead supports: " + this.sponge().getKeys().stream().map(t -> t.key().asString()).toList()));
    }

    @Override
    public int getMaximumLevel() {
        return 3;
    }

    @Override
    public int getMinimumLevel() {
        return 1;
    }

    @Override
    public @NotNull AbstractBlockData clone() {
        return new SoakCauldron(this.sponge());
    }
}
