package org.soak.wrapper.block.state.capture;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public abstract class CapturedAbstractBlockState<BS extends BlockState> implements BlockState {

    private final Map<String, CapturedBlockStateData<?, BS>> updateValues = new ConcurrentHashMap<>();
    private final BS blockState;

    public CapturedAbstractBlockState(BS blockState) {
        this.blockState = blockState;
    }

    protected CapturedAbstractBlockState<BS> add(String key, CapturedBlockStateData<?, BS> data) {
        this.updateValues.put(key, data);
        return this;
    }

    public BS basedOn() {
        return this.blockState;
    }

    protected <T> void setValue(String key, T value) {
        var blockStateData = this.updateValues.get(key);
        ((CapturedBlockStateData<T, BS>) blockStateData).setValue(value);
    }

    protected <T> @Nullable T value(String key) {
        var blockStateData = this.updateValues.get(key);
        return ((CapturedBlockStateData<T, BS>) blockStateData).value(blockState);
    }

    protected Stream<String> keys() {
        return Arrays.stream(BlockState.class.getDeclaredMethods()).map(Method::getName);
    }

    @Override
    public @NotNull Block getBlock() {
        return this.blockState.getBlock();
    }

    @Override
    @Deprecated
    public @NotNull MaterialData getData() {
        return this.blockState.getData();
    }

    @Override
    @Deprecated
    public void setData(@NotNull MaterialData materialData) {
        this.blockState.setData(materialData);
    }

    @Override
    public @NotNull BlockData getBlockData() {
        return this.blockState.getBlockData();
    }

    @Override
    public void setBlockData(@NotNull BlockData blockData) {
        this.blockState.setBlockData(blockData);
    }

    @Override
    public @NotNull Material getType() {
        return this.blockState.getType();
    }

    @Override
    public void setType(@NotNull Material material) {
        this.blockState.setType(material);
    }

    @Override
    public byte getLightLevel() {
        return this.blockState.getLightLevel();
    }

    @Override
    public @NotNull World getWorld() {
        return this.blockState.getWorld();
    }

    @Override
    public int getX() {
        return this.blockState.getX();
    }

    @Override
    public int getY() {
        return this.blockState.getY();
    }

    @Override
    public int getZ() {
        return this.blockState.getZ();
    }

    @Override
    public @NotNull Location getLocation() {
        return this.blockState.getLocation();
    }

    @Override
    public @Nullable Location getLocation(@Nullable Location location) {
        return this.blockState.getLocation(location);
    }

    @Override
    public @NotNull Chunk getChunk() {
        return this.blockState.getChunk();
    }

    @Override
    public boolean update() {
        return update(false);
    }

    @Override
    public boolean update(boolean force) {
        return update(force, false);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        this.updateValues.values().forEach(blockStateData -> blockStateData.setValue(this.blockState));
        return this.blockState.update(force, applyPhysics);
    }

    @Override
    public byte getRawData() {
        return this.blockState.getRawData();
    }

    @Override
    public void setRawData(byte b) {
        this.blockState.setRawData(b);
    }

    @Override
    public boolean isPlaced() {
        return this.blockState.isPlaced();
    }

    @Override
    public boolean isCollidable() {
        return this.blockState.isCollidable();
    }

    @Override
    public @Unmodifiable @NotNull Collection<ItemStack> getDrops() {
        return this.blockState.getDrops();
    }

    @Override
    public @NotNull @Unmodifiable Collection<ItemStack> getDrops(@Nullable ItemStack itemStack) {
        return this.blockState.getDrops(itemStack);
    }

    @Override
    public @NotNull @Unmodifiable Collection<ItemStack> getDrops(@NotNull ItemStack itemStack, @Nullable Entity entity) {
        return this.blockState.getDrops(itemStack, entity);
    }

    @Override
    public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
        this.blockState.setMetadata(s, metadataValue);
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String s) {
        return this.blockState.getMetadata(s);
    }

    @Override
    public boolean hasMetadata(@NotNull String s) {
        return this.blockState.hasMetadata(s);
    }

    @Override
    public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
        this.blockState.removeMetadata(s, plugin);
    }
}
