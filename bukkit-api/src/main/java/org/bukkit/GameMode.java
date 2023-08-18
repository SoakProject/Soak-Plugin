package org.bukkit;

import org.spongepowered.api.ResourceKey;

import java.util.Optional;

public enum GameMode {
    ADVENTURE,

    CREATIVE,
    SPECTATOR,
    SURVIVAL;

    public org.spongepowered.api.entity.living.player.gamemode.GameMode sponge(){
        throw new RuntimeException("Template only. Look at VanillaMaterials");
    }

    public static GameMode sponge(org.spongepowered.api.entity.living.player.gamemode.GameMode gamemode) {
        throw new RuntimeException("Template only. Look at VanillaMaterials");
    }

    public static Optional<GameMode> fromId(ResourceKey key) {
        throw new RuntimeException("Template only. Look at VanillaMaterials");

    }

    @Deprecated
    public static GameMode getByValue(int value) {
        throw new RuntimeException("Template only. Look at VanillaMaterials");
    }

    @Deprecated
    public int getValue() {
        throw new RuntimeException("Template only. Look at VanillaMaterials");
    }

}
