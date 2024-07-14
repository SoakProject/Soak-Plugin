package org.soak.wrapper.block.state.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakMessageMap;
import org.soak.exception.NotImplementedException;
import org.soak.wrapper.block.state.capture.CapturedAbstractBlockState;
import org.soak.wrapper.block.state.capture.CapturedBlockStateData;

import java.util.List;

public class SoakSignBlockEntityCapture extends CapturedAbstractBlockState<Sign> implements Sign {

    static final String LINES = "lines";
    static final String EDITABLE = "editable";
    static final String GLOWING_TEXT = "glowingText";
    static final String DYE_COLOUR = "dyeColor";

    public SoakSignBlockEntityCapture(Sign blockState) {
        super(blockState);
        this.add(LINES, new CapturedBlockStateData<>((state, data) -> {
            var lines = state.lines();
            lines.clear();
            lines.addAll(data);
        }, Sign::lines));
        this.add(EDITABLE, new CapturedBlockStateData<>(Sign::setEditable, Sign::isEditable));
        this.add(GLOWING_TEXT, new CapturedBlockStateData<>(Sign::setGlowingText, Sign::isGlowingText));
    }

    @Override
    public @NotNull List<Component> lines() {
        return this.value(LINES);
    }

    @Override
    public @NotNull Component line(int i) throws IndexOutOfBoundsException {
        return lines().get(i);
    }

    @Override
    public void line(int i, @NotNull Component component) throws IndexOutOfBoundsException {
        List<Component> lines = value(LINES);
        lines.set(i, component);
    }

    @Override
    public @NotNull String[] getLines() {
        return lines().stream().map(SoakMessageMap::mapToBukkit).toArray(String[]::new);
    }

    @Override
    public @NotNull String getLine(int i) throws IndexOutOfBoundsException {
        return getLines()[i];
    }

    @Override
    public void setLine(int i, @NotNull String s) throws IndexOutOfBoundsException {
        line(i, SoakMessageMap.toComponent(s));
    }

    @Override
    public boolean isEditable() {
        return value(EDITABLE);
    }

    @Override
    public void setEditable(boolean b) {
        setValue(EDITABLE, b);
    }

    @Override
    public boolean isGlowingText() {
        return value(GLOWING_TEXT);
    }

    @Override
    public void setGlowingText(boolean b) {
        setValue(GLOWING_TEXT, b);
    }

    @Override
    public @NotNull DyeColor getColor() {
        return this.value(DYE_COLOUR);
    }

    @Override
    public void setColor(@NotNull DyeColor dyeColor) {
        this.setValue(DYE_COLOUR, dyeColor);
    }

    @Override
    public @NotNull SignSide getSide(@NotNull Side side) {
        throw NotImplementedException.createByLazy(Side.class);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return this.basedOn().getPersistentDataContainer();
    }

    @Override
    public boolean isSnapshot() {
        return true;
    }
}
