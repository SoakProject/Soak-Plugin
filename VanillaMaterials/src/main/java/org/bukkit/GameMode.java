package org.bukkit;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Arrays;
import java.util.Optional;

public enum GameMode {

    ADVENTURE(GameModes.ADVENTURE),

    CREATIVE(GameModes.CREATIVE),
    SPECTATOR(GameModes.SPECTATOR),
    SURVIVAL(GameModes.SURVIVAL);

    private DefaultedRegistryReference<org.spongepowered.api.entity.living.player.gamemode.GameMode> mode;

    private GameMode(DefaultedRegistryReference<org.spongepowered.api.entity.living.player.gamemode.GameMode> mode) {
        this.mode = mode;
    }

    public static GameMode sponge(org.spongepowered.api.entity.living.player.gamemode.GameMode gamemode) {
        return Arrays.stream(values())
                .filter(mode -> mode.sponge().equals(gamemode))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find gamemode " + gamemode.key(
                        RegistryTypes.GAME_MODE).formatted()));
    }

    public static Optional<GameMode> fromId(ResourceKey key) {
        return Arrays.stream(values())
                .filter(mode -> mode.sponge().key(RegistryTypes.GAME_MODE).equals(key))
                .findAny();
    }

    @Deprecated
    public static GameMode getByValue(int value) {
        switch (value) {
            case 0 -> {
                return SURVIVAL;
            }
            case 1 -> {
                return CREATIVE;
            }
            case 2 -> {
                return ADVENTURE;
            }
            case 3 -> {
                return SPECTATOR;
            }
            default -> {
                return null;
            }
        }
    }

    public org.spongepowered.api.entity.living.player.gamemode.GameMode sponge() {
        return this.mode.get();
    }

    @Deprecated
    public int getValue() {
        switch (this) {
            case ADVENTURE -> {
                return 2;
            }
            case SPECTATOR -> {
                return 3;
            }
            case CREATIVE -> {
                return 1;
            }
            case SURVIVAL -> {
                return 0;
            }
            default -> {
                return -1;
            }
        }
    }
}
