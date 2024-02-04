package org.soak.wrapper.block.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakMirrorMap;
import org.soak.map.SoakRotationMap;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.block.data.type.BlockDataTypes;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Keys;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractBlockData implements BlockData, CommonBlockData {

    protected BlockState spongeState;

    protected AbstractBlockData(BlockState state) {
        this.spongeState = state;
    }

    public static CommonBlockData createBlockData(BlockState state) {
        return BlockDataTypes.valueFor(state).map(type -> {
            try {
                return type.instance(state);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).orElseGet(() -> new SoakBlockData(state));
    }

    @Override
    public int getLightEmission() {
        return this.spongeState.getInt(Keys.LIGHT_EMISSION).orElse(-1);
    }

    @Override
    public boolean isOccluding() {
        return this.spongeState.get(Keys.IS_OCCUPIED).orElse(false);
    }

    @Override
    public boolean requiresCorrectToolForDrops() {
        throw NotImplementedException.createByLazy(BlockData.class, "requiresCorrectToolForDrops");
    }

    @Override
    public boolean isPreferredTool(@NotNull ItemStack itemStack) {
        throw NotImplementedException.createByLazy(BlockData.class, "isPreferredTool");
    }

    @Override
    public @NotNull PistonMoveReaction getPistonMoveReaction() {
        throw NotImplementedException.createByLazy(BlockData.class, "getPistonMoveReaction");
    }

    @Override
    public boolean isSupported(@NotNull Block block) {
        throw NotImplementedException.createByLazy(BlockData.class, "isSupported", Block.class);
    }

    @Override
    public boolean isSupported(@NotNull Location location) {
        return isSupported(location.getBlock());
    }

    @Override
    public boolean isFaceSturdy(@NotNull BlockFace blockFace, @NotNull BlockSupport blockSupport) {
        throw NotImplementedException.createByLazy(BlockData.class, "isFaceSturdy", BlockFace.class, BlockSupport.class);
    }

    @Override
    public @NotNull Material getPlacementMaterial() {
        throw NotImplementedException.createByLazy(BlockData.class, "getPlacementMaterial");
    }

    @Override
    public void rotate(@NotNull StructureRotation structureRotation) {
        this.spongeState.rotate(SoakRotationMap.toSponge(structureRotation));
    }

    @Override
    public void mirror(@NotNull Mirror mirror) {
        this.spongeState.mirror(SoakMirrorMap.toSponge(mirror));
    }

    @Override
    public boolean isRandomlyTicked() {
        throw NotImplementedException.createByLazy(BlockData.class, "isRandomlyTicked");
    }

    @Override
    public BlockState sponge() {
        return this.spongeState;
    }

    @Override
    public CommonBlockData setSponge(BlockState state) {
        this.spongeState = state;
        return this;
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
