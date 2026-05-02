package org.soak.map;

import net.kyori.adventure.key.Key;
import org.bukkit.JukeboxSong;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.effect.sound.music.MusicDisc;

public class SoakSoundMap {

    public static net.kyori.adventure.sound.Sound.Source toAdventure(SoundCategory category) {
        if (category.equals(SoundCategory.PLAYERS)) {
            return net.kyori.adventure.sound.Sound.Source.PLAYER;
        }
        return net.kyori.adventure.sound.Sound.Source.valueOf(category.name());
    }
}
