package org.soak.wrapper.block.state.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.soak.exception.NotImplementedException;
import org.soak.map.SoakMessageMap;
import org.soak.wrapper.block.state.AbstractBlockSnapshotState;
import org.soak.wrapper.persistence.SoakImmutablePersistentDataContainer;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Keys;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SoakSignBlockEntitySnapshot extends AbstractBlockSnapshotState implements Sign {

    private final List<Component> linesOverride = new LinkedList<>();
    private boolean glowingOverride;

    public SoakSignBlockEntitySnapshot(BlockSnapshot snapshot, boolean glowingTextOverride, Component... linesOverride) {
        this(snapshot, glowingTextOverride, List.of(linesOverride));
    }

    public SoakSignBlockEntitySnapshot(BlockSnapshot snapshot, boolean glowingTextOverride, Collection<Component> linesOverride) {
        this(snapshot);
        this.linesOverride.addAll(linesOverride);
        this.glowingOverride = glowingTextOverride;
    }

    public SoakSignBlockEntitySnapshot(BlockSnapshot snapshot) {
        super(snapshot);
        this.glowingOverride = snapshot.getOrElse(Keys.GLOWING_TEXT, false);
        this.linesOverride.addAll(snapshot.getOrElse(Keys.SIGN_LINES, Collections.emptyList()));
    }

    @Override
    public @NotNull List<Component> lines() {
        if (this.linesOverride.isEmpty()) {
            this.snapshot.getOrElse(Keys.SIGN_LINES, Collections.emptyList());
        }
        return Collections.unmodifiableList(this.linesOverride);
    }

    @Override
    public @NotNull Component line(int i) throws IndexOutOfBoundsException {
        return lines().get(i);
    }

    @Override
    public void line(int i, @NotNull Component component) throws IndexOutOfBoundsException {
        this.linesOverride.set(i, component);
        var currentLines = this.snapshot.getOrElse(Keys.SIGN_LINES, new LinkedList<>());
        currentLines.set(i, component);
        this.snapshot = this.snapshot.with(Keys.SIGN_LINES, currentLines).orElse(this.snapshot);
    }

    @Override
    public @NotNull String[] getLines() {
        return lines().stream().map(SoakMessageMap::mapToBukkit).toArray(String[]::new);
    }

    @Override
    public @NotNull String getLine(int i) throws IndexOutOfBoundsException {
        return SoakMessageMap.mapToBukkit(lines().get(i));
    }

    @Override
    public void setLine(int i, @NotNull String s) throws IndexOutOfBoundsException {
        line(i, SoakMessageMap.toComponent(s));
    }

    @Override
    public boolean isEditable() {
        throw NotImplementedException.createByLazy(Sign.class, "isEditable");
    }

    @Override
    public void setEditable(boolean b) {
        throw NotImplementedException.createByLazy(Sign.class, "setEditable", boolean.class);
    }

    @Override
    public boolean isGlowingText() {
        return this.snapshot.getOrElse(Keys.GLOWING_TEXT, this.glowingOverride);
    }

    @Override
    public void setGlowingText(boolean b) {
        this.glowingOverride = b;
        this.snapshot = this.snapshot.with(Keys.GLOWING_TEXT, b).orElse(this.snapshot);
    }

    @Override
    public @NotNull DyeColor getColor() {
        throw NotImplementedException.createByLazy(Sign.class, "getColor");
    }

    @Override
    public void setColor(@NotNull DyeColor dyeColor) {
        throw NotImplementedException.createByLazy(Sign.class, "setColor", DyeColor.class);
    }

    @Override
    public @NotNull SignSide getSide(@NotNull Side side) {
        throw NotImplementedException.createByLazy(Sign.class, "getSide", Side.class);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return new SoakImmutablePersistentDataContainer(snapshot);
    }

    @Override
    public boolean isSnapshot() {
        return true;
    }
}
