package org.soak.wrapper.entity.projectile;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakProjectileSourceMap;
import org.soak.exception.NotImplementedException;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Identifiable;

import java.util.UUID;

public abstract class AbstractProjectile<P extends org.spongepowered.api.entity.projectile.Projectile> extends AbstractEntity<P> implements Projectile {

    public AbstractProjectile(Subject subject, Audience audience, P entity) {
        super(subject, audience, entity);
    }

    @Override
    public boolean hasLeftShooter() {
        throw NotImplementedException.createByLazy(Projectile.class, "hasLeftShooter");
    }

    @Override
    public void setHasLeftShooter(boolean b) {
        throw NotImplementedException.createByLazy(Projectile.class, "setLeftShooter", boolean.class);
    }

    @Override
    public boolean hasBeenShot() {
        throw NotImplementedException.createByLazy(Projectile.class, "hasBeenShot");
    }

    @Override
    public void setHasBeenShot(boolean b) {
        throw NotImplementedException.createByLazy(Projectile.class, "setHasBeenShot", boolean.class);
    }

    @Override
    public boolean canHitEntity(org.bukkit.entity.@NotNull Entity entity) {
        throw NotImplementedException.createByLazy(Projectile.class, "canHitEntity", Entity.class);
    }

    @Override
    public void hitEntity(org.bukkit.entity.@NotNull Entity entity) {
        throw NotImplementedException.createByLazy(Projectile.class, "hitEntity", Entity.class);
    }

    @Override
    public void hitEntity(org.bukkit.entity.@NotNull Entity entity, @NotNull Vector vector) {
        throw NotImplementedException.createByLazy(Projectile.class, "hitEntity", Entity.class, Vector.class);
    }

    @Override
    public @Nullable UUID getOwnerUniqueId() {
        return this.spongeEntity()
                .get(Keys.SHOOTER)
                .filter(source -> source instanceof Entity)
                .map(shooter -> (Entity) entity)
                .map(Identifiable::uniqueId)
                .orElse(null);
    }

    @Override
    public @Nullable ProjectileSource getShooter() {
        return this.spongeEntity()
                .get(Keys.SHOOTER)
                .map(SoakProjectileSourceMap::toBukkit)
                .orElse(null);
    }

    @Override
    public void setShooter(@Nullable ProjectileSource source) {
        if (source == null) {
            this.spongeEntity().remove(Keys.SHOOTER);
            return;
        }
        this.spongeEntity().offer(Keys.SHOOTER, SoakProjectileSourceMap.toSponge(source));
    }

    @Override
    public boolean doesBounce() {
        throw NotImplementedException.createByLazy(Projectile.class, "doesBounce");
    }

    @Override
    public void setBounce(boolean doesBounce) {
        throw NotImplementedException.createByLazy(Projectile.class, "setBounce", boolean.class);
    }
}
