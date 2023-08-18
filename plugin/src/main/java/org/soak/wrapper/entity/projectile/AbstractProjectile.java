package org.soak.wrapper.entity.projectile;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakProjectileSourceMap;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.service.permission.Subject;

public abstract class AbstractProjectile<P extends org.spongepowered.api.entity.projectile.Projectile> extends AbstractEntity<P> implements Projectile {

    public AbstractProjectile(Subject subject, Audience audience, P entity) {
        super(subject, audience, entity);
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
