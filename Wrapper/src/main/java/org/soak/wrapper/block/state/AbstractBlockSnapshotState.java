package org.soak.wrapper.block.state;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
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
import org.soak.WrapperManager;
import org.soak.exception.NotImplementedException;
import org.soak.plugin.SoakManager;
import org.soak.utils.ReflectionHelper;
import org.soak.wrapper.block.SoakBlock;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.soak.wrapper.block.state.generic.GenericBlockSnapshotState;
import org.soak.wrapper.block.state.sign.SoakSignBlockEntitySnapshot;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.math.vector.Vector3i;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AbstractBlockSnapshotState implements BlockState {

    protected BlockSnapshot snapshot;

    public AbstractBlockSnapshotState(BlockSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public static @NotNull BlockState wrap(BlockSnapshot snapshot) {
        var opSign = wrapSign(snapshot);
        if (opSign != null) {
            return opSign;
        }
        return new GenericBlockSnapshotState(snapshot);
    }

    /*
    Dont normally need to do this, it seems Keys.SIGN_LINES no longer works
     */
    private static @Nullable SoakSignBlockEntitySnapshot wrapSign(BlockSnapshot snapshot) {
        try {
            Object compoundTag = ReflectionHelper.getField(snapshot, "compound");
            if (compoundTag == null) {
                return null;
            }
            Map<String, ?> tags = ReflectionHelper.getField(compoundTag, "tags");
            if (!(tags.containsKey("Text1") || tags.containsKey("Text2") || tags.containsKey("Text3") || tags.containsKey("Text4"))) {
                return null;
            }
            Component line1 = GsonComponentSerializer.gson().deserialize(ReflectionHelper.getValueFromTagType(tags.get("Text1")));
            Component line2 = GsonComponentSerializer.gson().deserialize(ReflectionHelper.getValueFromTagType(tags.get("Text2")));
            Component line3 = GsonComponentSerializer.gson().deserialize(ReflectionHelper.getValueFromTagType(tags.get("Text3")));
            Component line4 = GsonComponentSerializer.gson().deserialize(ReflectionHelper.getValueFromTagType(tags.get("Text4")));
            byte glowingTextByte = ReflectionHelper.getValueFromTagType(tags.get("GlowingText"));
            return new SoakSignBlockEntitySnapshot(snapshot, glowingTextByte != 0, line1, line2, line3, line4);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    @Override
    public @NotNull BlockData getBlockData() {
        var spongeBlockState = this.snapshot.state();
        return SoakManager.<WrapperManager>getManager().getMemoryStore().get(spongeBlockState);
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
        var worldKey = this.snapshot.world();
        var spongeWorld = Sponge.server()
                .worldManager()
                .world(worldKey)
                .orElseThrow(() -> new RuntimeException("BlockState BlockSnapshot attempted to access a unloaded world"));
        return SoakManager.<WrapperManager>getManager().getMemoryStore().get(spongeWorld);
    }

    private int position(Function<Vector3i, Integer> fun) {
        return fun.apply(this.snapshot.position());
    }

    @Override
    public int getX() {
        return position(Vector3i::x);
    }

    @Override
    public int getY() {
        return position(Vector3i::y);
    }

    @Override
    public int getZ() {
        return position(Vector3i::z);
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
        return this.snapshot.location().map(loc -> loc.createSnapshot().equals(this.snapshot)).orElse(false);
    }

    @Override
    public boolean isCollidable() {
        throw NotImplementedException.createByLazy(BlockState.class, "isCollidable");
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

    @Override
    public @NotNull Block getBlock() {
        return this.snapshot.location()
                .map(loc -> (Block) new SoakBlock(loc))
                .orElseGet(() -> new SoakBlockSnapshot(this.snapshot));
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
        return Material.getBlockMaterial(this.snapshot.state().type());
    }

    @Override
    public void setType(@NotNull Material arg0) {
        throw NotImplementedException.createByLazy(BlockState.class, "setType", Material.class);
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
        return this.snapshot.restore(force, applyPhysics ? BlockChangeFlags.ALL : BlockChangeFlags.NOTIFY_CLIENTS);
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
}
