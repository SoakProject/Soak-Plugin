package org.soak.wrapper.potion;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;

import java.util.Map;

public class SoakPotionEffectType extends PotionEffectType {

    private double duration;
    private boolean isInstant;
    private Color color;
    private String name;
    private String translationKey;

    public SoakPotionEffectType(double duration, boolean isInstant, Color color, String name, String translationKey, int fakeId, NamespacedKey key) {
        super(fakeId, key);
        this.color = color;
        this.isInstant = isInstant;
        this.name = name;
        this.duration = duration;
        this.translationKey = translationKey;
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

    @Override
    public @NotNull Map<Attribute, AttributeModifier> getEffectAttributes() {
        throw NotImplementedException.createByLazy(PotionEffectType.class, "getEffectAttributes");
    }

    @Override
    public double getAttributeModifierAmount(@NotNull Attribute attribute, int i) {
        throw NotImplementedException.createByLazy(PotionEffectType.class, "getAttributeModifierAmount", Attribute.class, int.class);
    }

    @NotNull
    @Override
    public Category getEffectCategory() {
        throw NotImplementedException.createByLazy(PotionEffectType.class, "getEffectCategory");
    }

    @Override
    public @NotNull String translationKey() {
        return this.translationKey;
    }
}
