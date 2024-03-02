package org.soak.wrapper.block.state.bed;

import org.bukkit.DyeColor;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakColourMap;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.block.state.AbstractBlockState;
import org.spongepowered.api.block.entity.Bed;
import org.spongepowered.api.data.Keys;

public class SoakBedBlockEntity extends AbstractBlockState<Bed> implements org.bukkit.block.Bed {
    public SoakBedBlockEntity(Bed blockEntity) {
        super(blockEntity);
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public boolean update(boolean b) {
        return true;
    }

    @Override
    public boolean update(boolean b, boolean b1) {
        return true;
    }

    @Override
    public boolean isCollidable() {
        ;
        throw NotImplementedException.createByLazy(SoakBedBlockEntity.class, "isCollidable");
    }

    @Override
    public @NotNull DyeColor getColor() {
        var spongeDye = this.blockEntity.color().get();
        return SoakColourMap.toBukkitDye(spongeDye);
    }

    @Override
    @Deprecated
    public void setColor(DyeColor dyeColor) {
        var spongeDye = SoakColourMap.toSpongeDye(SoakColourMap.toSponge(dyeColor.getColor())).orElseThrow(() -> new RuntimeException("Could not map " + dyeColor.name() + " to sponge"));
        this.blockEntity.offer(Keys.DYE_COLOR, spongeDye);

    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        throw NotImplementedException.createByLazy(SoakBedBlockEntity.class, "getPersistentDataContainer");
    }

    @Override
    public boolean isSnapshot() {
        return false;
    }
}
