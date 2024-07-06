package org.soak.wrapper.block.state.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakColourMap;
import org.soak.map.SoakMessageMap;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.block.state.AbstractBlockState;
import org.soak.wrapper.block.state.capture.CapturedAbstractBlockState;
import org.spongepowered.api.block.entity.Sign;
import org.spongepowered.api.data.Keys;

import java.util.List;

public class SoakSignBlockEntity extends AbstractBlockState<Sign> implements org.bukkit.block.Sign {

    public SoakSignBlockEntity(Sign blockEntity) {
        super(blockEntity);
    }

    @Override
    public CapturedAbstractBlockState<?> asSnapshot() {
        return new SoakSignBlockEntityCapture(this);
    }

    @Override
    public @NotNull List<Component> lines() {
        return this.sponge().lines().get();
    }

    @Override
    public @NotNull Component line(int index) throws IndexOutOfBoundsException {
        return lines().get(index);
    }

    @Override
    public void line(int index, @NotNull Component line) throws IndexOutOfBoundsException {
        var lines = this.sponge().get(Keys.SIGN_LINES).orElseThrow(() -> new RuntimeException("No sign data"));
        lines.set(index, line);
        this.sponge().offer(Keys.SIGN_LINES, lines);
    }

    @Override
    public @NotNull String[] getLines() {
        return lines().stream().map(SoakMessageMap::mapToBukkit).toArray(String[]::new);
    }

    @Override
    public @NotNull String getLine(int index) throws IndexOutOfBoundsException {
        return SoakMessageMap.mapToBukkit(this.line(index));
    }

    @Override
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException {
        line(index, SoakMessageMap.toComponent(line));
    }

    @Override
    public boolean isEditable() {
        throw NotImplementedException.createByLazy(SoakSignBlockEntity.class, "isEditable");
    }

    @Override
    public void setEditable(boolean editable) {
        throw NotImplementedException.createByLazy(SoakSignBlockEntity.class, "setEditable", boolean.class);
    }

    @Override
    public boolean isGlowingText() {
        return this.blockEntity.get(Keys.GLOWING_TEXT).orElse(false);
    }

    @Override
    public void setGlowingText(boolean b) {
        this.blockEntity.offer(Keys.GLOWING_TEXT, b);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        throw NotImplementedException.createByLazy(SoakSignBlockEntity.class, "getPersistentDataContainer");
    }

    @Override
    public boolean isSnapshot() {
        return false;
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public boolean update(boolean force) {
        return true;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public @Nullable DyeColor getColor() {
        return this.blockEntity.get(Keys.COLOR).map(colour -> DyeColor.getByColor(SoakColourMap.toBukkit(colour))).orElse(DyeColor.BLACK);
    }

    @Override
    public void setColor(DyeColor color) {
        this.blockEntity.offer(Keys.COLOR, SoakColourMap.toSponge(color.getColor()));
    }

    @Override
    public @NotNull SignSide getSide(@NotNull Side side) {
        throw NotImplementedException.createByLazy(SoakSignBlockEntity.class, "getSide", Side.class);
    }
}
