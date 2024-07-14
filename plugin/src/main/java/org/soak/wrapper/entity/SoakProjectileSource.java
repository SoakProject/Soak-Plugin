package org.soak.wrapper.entity;

import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.exception.NotImplementedException;

public interface SoakProjectileSource extends ProjectileSource {

    org.spongepowered.api.entity.Entity spongeEntity();

    @Override
    default <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile) {
        throw NotImplementedException.createByLazy(ProjectileSource.class, "launchProjectile", Class.class);
    }

    @Override
    default <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity) {
        throw NotImplementedException.createByLazy(ProjectileSource.class, "launchProjectile", Class.class, Vector.class);
    }
}
