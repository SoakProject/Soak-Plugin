package org.soak.map;

import org.spongepowered.api.item.FireworkEffect;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class SoakFireworkEffectMap {

    public static org.bukkit.FireworkEffect toBukkit(FireworkEffect spongeEffect) {
        return org
                .bukkit
                .FireworkEffect
                .builder()
                .flicker(spongeEffect.flickers())
                .trail(spongeEffect.hasTrail())
                .withColor(spongeEffect.colors())
                .withFade(spongeEffect.fadeColors())
                .build();
    }

    public static Optional<FireworkEffect> findSponge(org.bukkit.FireworkEffect bukkitEffect, Collection<FireworkEffect> spongeEffects) {
        return spongeEffects
                .parallelStream()
                .filter(effect -> effect.flickers() == bukkitEffect.hasFlicker())
                .filter(effect -> effect.hasTrail() == bukkitEffect.hasTrail())
                .filter(effect -> effect.colors().stream().allMatch(color -> bukkitEffect.getColors().stream().anyMatch(bukkitColor -> bukkitColor.asRGB() == color.rgb())))
                .filter(effect -> effect.fadeColors().stream().allMatch(color -> bukkitEffect.getColors().stream().anyMatch(bukkitColor -> bukkitColor.asRGB() == color.rgb())))
                .findFirst();
    }

    public static FireworkEffect buildSponge(org.bukkit.FireworkEffect bukkitEffect) {
        return FireworkEffect
                .builder()
                .flicker(bukkitEffect.hasFlicker())
                .trail(bukkitEffect.hasTrail())
                .fades(bukkitEffect.getFadeColors().stream().map(co -> org.spongepowered.api.util.Color.ofRgb(co.asRGB())).collect(Collectors.toSet()))
                .colors(bukkitEffect.getColors().stream().map(co -> org.spongepowered.api.util.Color.ofRgb(co.asRGB())).collect(Collectors.toSet()))
                .build();
    }
}
