package org.soak.wrapper.block.state;

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
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.block.SoakBlock;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.soak.wrapper.block.state.sign.SoakSignBlockEntity;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.block.entity.Sign;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.List;

public abstract class AbstractBlockState<TileEntity extends BlockEntity> implements BlockState {

    protected final TileEntity blockEntity;

    public AbstractBlockState(TileEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public static <T extends BlockEntity> AbstractBlockState<T> wrap(T blockEntity) {
        if (blockEntity instanceof Sign) {
            return (AbstractBlockState<T>) new SoakSignBlockEntity((Sign) blockEntity);
        }
        throw new RuntimeException("No mapping found for BlockEntity of " + blockEntity.getClass().getName());
    }

    public TileEntity sponge() {
        return this.blockEntity;
    }


    @Override
    public @NotNull BlockData getBlockData() {
        return AbstractBlockData.createBlockData(this.blockEntity.block());
    }

    @Override
    public void setBlockData(@NotNull BlockData arg0) {
        throw NotImplementedException.createByLazy(BlockState.class, "setBlockData", BlockData.class);
    }

    @Override
    public byte getLightLevel() {
        throw NotImplementedException.createByLazy(BlockState.class, "getLightLevel");
    }

    @Override
    public @NotNull World getWorld() {
        return SoakPlugin.plugin().getMemoryStore().get((ServerWorld) this.blockEntity.world());
    }

    @Override
    public int getX() {
        return this.blockEntity.blockPosition().x();
    }

    @Override
    public int getY() {
        return this.blockEntity.blockPosition().y();
    }

    @Override
    public int getZ() {
        return this.blockEntity.blockPosition().z();
    }

    @Override
    public @NotNull Chunk getChunk() {
        throw NotImplementedException.createByLazy(BlockState.class, "getChunk");
    }

    @Deprecated
    @Override
    public byte getRawData() {
        throw NotImplementedException.createByLazy(BlockState.class, "getRawData");
    }

    @Deprecated
    @Override
    public void setRawData(byte arg0) {
        throw NotImplementedException.createByLazy(BlockState.class, "setRawData", byte.class);
    }

    @Override
    public boolean isPlaced() {
        return this.blockEntity.serverLocation()
                .blockEntity()
                .map(entity -> entity.equals(this.blockEntity))
                .orElse(false);
    }

    @Override
    public @NotNull Block getBlock() {
        return new SoakBlock(this.blockEntity.serverLocation());
    }

    @Override
    @Deprecated
    public @NotNull MaterialData getData() {
        throw NotImplementedException.createByLazy(BlockState.class, "getData");
    }

    @Override
    @Deprecated
    public void setData(@NotNull MaterialData arg0) {
        throw NotImplementedException.createByLazy(BlockState.class, "setData", MaterialData.class);
    }

    @Override
    public Location getLocation(Location arg0) {
        throw NotImplementedException.createByLazy(BlockState.class, "getLocation", Location.class);
    }

    @Override
    public @NotNull Location getLocation() {
        return new Location(getWorld(), this.getX(), this.getY(), this.getZ());
    }

    @Override
    public @NotNull Material getType() {
        return Material.getBlockMaterial(this.blockEntity.block().type());
    }

    @Override
    public void setType(@NotNull Material arg0) {
        throw NotImplementedException.createByLazy(BlockState.class, "setType", Material.class);
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        throw NotImplementedException.createByLazy(AbstractBlockState.class,
                "setMetadata",
                String.class,
                MetadataValue.class);
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        throw NotImplementedException.createByLazy(AbstractBlockState.class, "getMetadata", String.class);
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        throw NotImplementedException.createByLazy(AbstractBlockState.class, "hasMetadata", String.class);
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        throw NotImplementedException.createByLazy(AbstractBlockState.class, "metadataKey", String.class, Plugin.class);
    }

    @Override
    public @Unmodifiable @NotNull Collection<ItemStack> getDrops() {
        throw NotImplementedException.createByLazy(BlockState.class, "getDrops");
    }

    @Override
    public @Unmodifiable @NotNull Collection<ItemStack> getDrops(@Nullable ItemStack itemStack) {
        throw NotImplementedException.createByLazy(BlockState.class, "getDrops", ItemStack.class);
    }

    @Override
    public @Unmodifiable @NotNull Collection<ItemStack> getDrops(@NotNull ItemStack itemStack, @Nullable Entity entity) {
        throw NotImplementedException.createByLazy(BlockState.class, "getDrops", ItemStack.class, Entity.class);
    }
}
