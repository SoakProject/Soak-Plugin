package org.soak.map;

import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.registry.DefaultedRegistryReference;

public class SoakGameModeMap {

    public static DefaultedRegistryReference<GameMode> toSponge(org.bukkit.GameMode mode) {
        return switch (mode) {
            case ADVENTURE -> GameModes.ADVENTURE;
            case CREATIVE -> GameModes.CREATIVE;
            case SPECTATOR -> GameModes.SPECTATOR;
            case SURVIVAL -> GameModes.SURVIVAL;
        };
    }

    public static org.bukkit.GameMode toBukkit(GameMode mode) {
        return org.bukkit.GameMode.sponge(mode);
    }
}
