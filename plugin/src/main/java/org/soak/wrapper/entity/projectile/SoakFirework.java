package org.soak.wrapper.entity.projectile;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.inventory.meta.SoakFireworkMeta;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.entity.projectile.explosive.FireworkRocket;
import org.spongepowered.api.service.permission.Subject;

import java.util.UUID;

public class SoakFirework extends AbstractProjectile<FireworkRocket> implements Firework {

    public SoakFirework(Subject subject, Audience audience, FireworkRocket entity) {
        super(subject, audience, entity);
    }

    @Override
    public @NotNull FireworkMeta getFireworkMeta() {
        //TODO double check this
        return new SoakFireworkMeta(this.spongeEntity());
    }

    @Override
    public void setFireworkMeta(@NotNull FireworkMeta arg0) {
        //this is done in the item meta .... may need to fix this
    }

    @Override
    public void detonate() {
        this.spongeEntity().detonate();
    }

    @Override
    public boolean isShotAtAngle() {
        throw NotImplementedException.createByLazy(Firework.class, "isShotAtAngle");
    }

    @Override
    public void setShotAtAngle(boolean arg0) {
        throw NotImplementedException.createByLazy(Firework.class, "setShotAtAngle", boolean.class);
    }

    @Override
    public UUID getSpawningEntity() {
        return this.spongeEntity().creator().map(Value::get).orElse(null);
    }

    @Override
    public LivingEntity getBoostedEntity() {
        throw NotImplementedException.createByLazy(Firework.class, "getBoostedEntity");
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
