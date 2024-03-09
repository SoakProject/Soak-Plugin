package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.SideEffect;
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.AbstractWorld;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class BukkitWorld extends AbstractWorld {

    private final AbstractWorld world;

    public BukkitWorld(AbstractWorld wrapped) {
        this.world = wrapped;
    }

    @Override
    public String getName() {
        return this.world.getName();
    }

    @Override
    public <B extends BlockStateHolder<B>> boolean setBlock(BlockVector3 position, B block, SideEffectSet sideEffects) throws WorldEditException {
        return this.world.setBlock(position, block, sideEffects);
    }

    @Override
    public boolean notifyAndLightBlock(BlockVector3 position, BlockState previousType) throws WorldEditException {
        return this.world.notifyAndLightBlock(position, previousType);
    }

    @Override
    public Set<SideEffect> applySideEffects(BlockVector3 position, BlockState previousType, SideEffectSet sideEffectSet) throws WorldEditException {
        return this.world.applySideEffects(position, previousType, sideEffectSet);
    }

    @Override
    public int getBlockLightLevel(BlockVector3 position) {
        return this.getBlockLightLevel(position);
    }

    @Override
    public boolean clearContainerBlockContents(BlockVector3 position) {
        return this.world.clearContainerBlockContents(position);
    }

    @Override
    public void dropItem(Vector3 position, BaseItemStack item) {
        this.world.dropItem(position, item);
    }

    @Override
    public void simulateBlockMine(BlockVector3 position) {
        this.world.simulateBlockMine(position);
    }

    @Override
    public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, BlockVector3 position) throws MaxChangedBlocksException {
        return this.world.generateTree(type, editSession, position);
    }

    @Override
    public BlockVector3 getSpawnPosition() {
        return this.world.getSpawnPosition();
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        return this.world.getEntities(region);
    }

    @Override
    public List<? extends Entity> getEntities() {
        return this.world.getEntities();
    }

    @Nullable
    @Override
    public Entity createEntity(Location location, BaseEntity entity) {
        return this.world.createEntity(location, entity);
    }

    @Override
    public BlockState getBlock(BlockVector3 position) {
        return this.world.getBlock(position);
    }

    @Override
    public BaseBlock getFullBlock(BlockVector3 position) {
        return this.getFullBlock(position);
    }

    @Override
    public String getId() {
        return this.world.getId();
    }
}
