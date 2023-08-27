package org.soak.map;

import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.projectile.source.BlockProjectileSource;
import org.spongepowered.api.projectile.source.ProjectileSource;

public class SoakProjectileSourceMap {

    public static ProjectileSource toSponge(org.bukkit.projectiles.ProjectileSource source) {
        if (source instanceof org.bukkit.projectiles.BlockProjectileSource) {
            throw new RuntimeException("No mapping for BlockProjectileSource");
        }
        if (source instanceof AbstractEntity<?>) {
            return ((AbstractEntity<?>) source).spongeEntity();
        }
        throw new RuntimeException("No mapping for " + source.getClass().getName());
    }

    public static org.bukkit.projectiles.ProjectileSource toBukkit(ProjectileSource source) {
        if (source instanceof BlockProjectileSource) {
            throw new RuntimeException("No mapping for BlockProjectileSource");
        }
        if (source instanceof Entity) {
            return (org.bukkit.projectiles.ProjectileSource) AbstractEntity.wrap((Entity) source);
        }
        throw new RuntimeException("No mapping for " + source.getClass().getName());
    }
}
