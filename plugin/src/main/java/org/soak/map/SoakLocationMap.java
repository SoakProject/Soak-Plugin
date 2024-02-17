package org.soak.map;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Objects;

public class SoakLocationMap {

    public static ServerLocation toSponge(@NotNull Location location) {
        var bukktiWorld = (SoakWorld) Objects.requireNonNullElseGet(location.getWorld(), () -> new IllegalArgumentException("Location provided must contain a world"));
        return bukktiWorld.sponge().location(location.x(), location.y(), location.z());
    }

    public static Location toBukkit(@NotNull org.spongepowered.api.world.Location<? extends World<?, ?>, ?> location) {
        var bukkitWorld = SoakPlugin
                .plugin()
                .getMemoryStore()
                .get(location
                        .onServer()
                        .map(org.spongepowered.api.world.Location::world)
                        .orElseThrow(() -> new RuntimeException("Soak requires server")));
        return new Location(bukkitWorld, location.x(), location.y(), location.z());
    }
}
