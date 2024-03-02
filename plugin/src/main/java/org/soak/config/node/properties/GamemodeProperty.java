package org.soak.config.node.properties;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import java.util.Optional;

public class GamemodeProperty implements PropertiesNode<GameMode> {

    private GameMode mode;

    @Override
    public String name() {
        return "gamemode";
    }

    @Override
    public Optional<GameMode> value() {
        return Optional.ofNullable(this.mode);
    }

    @Override
    public GameMode defaultValue() {
        return GameModes.SURVIVAL.get();
    }

    @Override
    public void setMemoryValue(String value) {
        ResourceKey key;
        if (value.contains(":")) {
            key = ResourceKey.resolve(value);
        } else {
            key = ResourceKey.sponge(value);
        }
        this.mode = GameModes.registry().value(key);
    }
}
