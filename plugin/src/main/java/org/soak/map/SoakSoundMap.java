package org.soak.map;

import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;

public class SoakSoundMap {

    public static net.kyori.adventure.sound.Sound.Source toAdventure(SoundCategory category) {
        if (category.equals(SoundCategory.PLAYERS)) {
            return net.kyori.adventure.sound.Sound.Source.PLAYER;
        }
        return net.kyori.adventure.sound.Sound.Source.valueOf(category.name());
    }

    public static @NonNull Key toAdventure(Sound sound) {
        return sound.key();
    }

    public static SoundType toSponge(String name) {
        var key = ResourceKey.minecraft(name);
        return SoundTypes.registry()
                .findValue(key)
                .orElseThrow(() -> new RuntimeException("No direct soundtype mapping for " + key.formatted()));
    }
}
