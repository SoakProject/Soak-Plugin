package org.soak.wrapper.entity;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.soak.exception.NotImplementedException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSources;

public interface SoakDamageable extends Damageable {

    org.spongepowered.api.entity.Entity spongeEntity();

    @Override
    default void setAbsorptionAmount(double arg0) {
        this.spongeEntity().offer(Keys.ABSORPTION, arg0);
    }

    @Override
    default double getAbsorptionAmount() {
        return this.spongeEntity().get(Keys.ABSORPTION).orElse(0.0);
    }

    @Override
    default void damage(double arg0) {
        this.spongeEntity().damage(arg0, DamageSources.GENERIC);
    }

    @Override
    default void damage(double arg0, Entity arg1) {
        throw NotImplementedException.createByLazy(Damageable.class, "damage", double.class, Entity.class);
    }

    @Override
    default double getHealth() {
        return this.spongeEntity().get(Keys.HEALTH).orElse(0.0);
    }

    @Override
    default void setHealth(double arg0) {
        this.spongeEntity().offer(Keys.HEALTH, 0.0);
    }

    @Deprecated
    @Override
    default double getMaxHealth() {
        return this.spongeEntity().get(Keys.MAX_HEALTH).orElse(0.0);
    }

    @Deprecated
    @Override
    default void setMaxHealth(double arg0) {
        this.spongeEntity().offer(Keys.MAX_HEALTH, arg0);
    }

    @Deprecated
    @Override
    default void resetMaxHealth() {
        throw NotImplementedException.createByLazy(Damageable.class, "resetMaxHealth");
    }
}
