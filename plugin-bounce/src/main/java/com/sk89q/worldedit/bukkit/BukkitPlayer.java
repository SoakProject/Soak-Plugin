package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.HandSide;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.UUID;

public class BukkitPlayer extends AbstractPlayerActor {

    private final AbstractPlayerActor player;

    public BukkitPlayer(AbstractPlayerActor player) {
        this.player = player;
    }

    @Override
    public World getWorld() {
        return BukkitAdapter.asBukkitWorld(this.player.getWorld());
    }

    @Override
    public BaseItemStack getItemInHand(HandSide handSide) {
        return this.player.getItemInHand(handSide);
    }

    @Override
    public void giveItem(BaseItemStack itemStack) {
        this.player.giveItem(itemStack);
    }

    @Override
    public BlockBag getInventoryBlockBag() {
        return this.player.getInventoryBlockBag();
    }

    @Nullable
    @Override
    public BaseEntity getState() {
        return this.player.getState();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public void printRaw(String msg) {
        this.printRaw(msg);
    }

    @Override
    public void printDebug(String msg) {
        this.printDebug(msg);
    }

    @Override
    public void print(String msg) {
        this.player.print(msg);
    }

    @Override
    public void printError(String msg) {
        this.player.printError(msg);
    }

    @Override
    public void print(Component component) {
        this.player.print(component);
    }

    @Override
    public Locale getLocale() {
        return this.player.getLocale();
    }

    @Override
    public Location getLocation() {
        return this.player.getLocation();
    }

    @Override
    public boolean setLocation(Location location) {
        return this.player.setLocation(location);
    }

    @Override
    public SessionKey getSessionKey() {
        return this.player.getSessionKey();
    }

    @Nullable
    @Override
    public <T> T getFacet(Class<? extends T> cls) {
        return this.player.getFacet(cls);
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String[] getGroups() {
        return this.player.getGroups();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }
}
