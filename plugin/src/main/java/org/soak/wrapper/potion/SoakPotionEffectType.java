package org.soak.wrapper.potion;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SoakPotionEffectType extends PotionEffectType {

    private double duration;
    private boolean isInstant;
    private Color color;
    private String name;

    public SoakPotionEffectType(double duration, boolean isInstant, Color color, String name, int fakeId) {
        super(fakeId);
        this.color = color;
        this.isInstant = isInstant;
        this.name = name;
        this.duration = duration;
    }

    @Override
    public double getDurationModifier() {
        return this.duration;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public boolean isInstant() {
        return this.isInstant;
    }

    @Override
    public @NotNull Color getColor() {
        return this.color;
    }
}
