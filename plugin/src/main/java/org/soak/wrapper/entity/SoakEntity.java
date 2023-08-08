package org.soak.wrapper.entity;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakDirectionMap;
import org.soak.map.SoakMessageMap;
import org.soak.map.SoakVectorMap;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.SoakWorld;
import org.soak.wrapper.command.SoakCommandSender;
import org.soak.wrapper.persistence.SoakMutablePersistentDataContainer;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class SoakEntity<E extends org.spongepowered.api.entity.Entity> extends SoakCommandSender implements Entity {

    private final E entity;

    public SoakEntity(Subject subject, Audience audience, E entity) {
        super(subject, audience);
        this.entity = entity;
    }

    public E spongeEntity() {
        return this.entity;
    }

    @Override
    public void setMetadata(@NotNull String arg0, @NotNull MetadataValue arg1) {
        throw NotImplementedException.createByLazy(Metadatable.class, "setMetadata", String.class, MetadataValue.class);
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        throw NotImplementedException.createByLazy(Metadatable.class, "getMetadata", String.class);
    }

    @Override
    public boolean hasMetadata(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Metadatable.class, "hasMetadata", String.class);
    }

    @Override
    public void removeMetadata(@NotNull String arg0, @NotNull Plugin arg1) {
        throw NotImplementedException.createByLazy(Metadatable.class, "removeMetadata", String.class, Plugin.class);
    }

    @Override
    public Component customName() {
        return this.entity.customName().map(Value::get).orElse(null);
    }

    @Override
    public void customName(Component arg0) {
        this.entity.customName().ifPresent(mut -> mut.set(arg0));
    }

    @Override
    @Deprecated
    public String getCustomName() {
        return SoakMessageMap.mapToBukkit(customName());
    }

    @Override
    public void setCustomName(String arg0) {
        this.customName(SoakMessageMap.mapToComponent(arg0));
    }

    @Override
    public void remove() {
        this.entity.remove();
    }

    @Override
    public boolean isEmpty() {
        throw NotImplementedException.createByLazy(Entity.class, "isEmpty");
    }

    @Override
    public @NotNull Location getLocation() {
        throw NotImplementedException.createByLazy(Entity.class, "getLocation");
    }

    @Override
    public Location getLocation(Location arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "getLocation", Location.class);
    }

    @Override
    public @NotNull EntityType getType() {
        throw NotImplementedException.createByLazy(Entity.class, "getType");
    }

    @Override
    public @NotNull Chunk getChunk() {
        throw NotImplementedException.createByLazy(Entity.class, "getChunk");
    }

    @Override
    public @NotNull World getWorld() {
        return new SoakWorld((ServerWorld) this.entity.world());
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.entity.uniqueId();
    }

    @Override
    public Entity.@NotNull Spigot spigot() {
        throw NotImplementedException.createByLazy(Entity.class, "spigot");
    }

    @Override
    public boolean teleport(@NotNull Entity arg0, PlayerTeleportEvent.@NotNull TeleportCause arg1) {
        throw NotImplementedException.createByLazy(Entity.class, "teleport", Entity.class, PlayerTeleportEvent.TeleportCause.class);
    }

    @Override
    public boolean teleport(@NotNull Entity arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "teleport", Entity.class);
    }

    @Override
    public boolean teleport(@NotNull Location arg0, PlayerTeleportEvent.@NotNull TeleportCause arg1) {
        throw NotImplementedException.createByLazy(Entity.class, "teleport", Location.class, PlayerTeleportEvent.TeleportCause.class);
    }

    @Override
    public boolean teleport(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "teleport", Location.class);
    }

    @Override
    public @NotNull Vector getVelocity() {
        return SoakVectorMap.toBukkit(this.entity.velocity().get());
    }

    @Override
    public void setVelocity(@NotNull Vector arg0) {
        this.entity.offer(Keys.VELOCITY, SoakVectorMap.to3d(arg0));
    }

    @Override
    public double getHeight() {
        return this.entity.boundingBox().map(box -> box.size().z()).orElse(-1.0);
    }

    @Override
    public double getWidth() {
        return this.entity.boundingBox().map(box -> box.size().x()).orElse(-1.0);
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        Optional<AABB> opBoundingBox = this.entity.boundingBox();
        if (opBoundingBox.isEmpty()) {
            throw new RuntimeException("Entity of " + this.getType().name() + " does not have a bounding box");
        }
        AABB box = opBoundingBox.get();
        return BoundingBox.of(SoakVectorMap.toBukkit(box.min()), SoakVectorMap.toBukkit(box.max()));
    }

    @Override
    public boolean isOnGround() {
        return this.entity.onGround().get();
    }

    @Override
    public boolean isInWater() {
        throw NotImplementedException.createByLazy(Entity.class, "isInWater");
    }

    @Override
    public @NotNull List<Entity> getNearbyEntities(double x, double y, double z) {
        throw NotImplementedException.createByLazy(Entity.class, "getNearbyEntities", double.class, double.class, double.class);
    }

    @Override
    public int getEntityId() {
        throw NotImplementedException.createByLazy(Entity.class, "getEntityId");
    }

    @Override
    public int getFireTicks() {
        return this.entity.fireTicks().map(mut -> (int) mut.get().ticks()).orElse(0);
    }

    @Override
    public void setFireTicks(int arg0) {
        this.entity.offer(Keys.FIRE_TICKS, Ticks.of(arg0));
    }

    @Override
    public int getMaxFireTicks() {
        throw NotImplementedException.createByLazy(Entity.class, "getMaxFireTicks");
    }

    @Override
    public boolean isDead() {
        return this.entity.isRemoved();
    }

    @Override
    public boolean isValid() {
        throw NotImplementedException.createByLazy(Entity.class, "isValid");
    }

    @Override
    public boolean isPersistent() {
        return this.entity.get(Keys.IS_PERSISTENT).orElse(false);
    }

    @Override
    public void setPersistent(boolean arg0) {
        this.entity.offer(Keys.IS_PERSISTENT, arg0);
    }

    @Deprecated
    @Override
    public Entity getPassenger() {
        @NotNull List<Entity> passengers = getPassengers();
        if (passengers.isEmpty()) {
            return null;
        }
        return passengers.get(0);
    }

    @Deprecated
    @Override
    public boolean setPassenger(@NotNull Entity arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "setPassenger", Entity.class);
    }

    @Override
    public @NotNull List<Entity> getPassengers() {
        throw NotImplementedException.createByLazy(Entity.class, "getPassengers");
    }

    @Override
    public boolean addPassenger(@NotNull Entity arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "addPassenger", Entity.class);
    }

    @Override
    public boolean removePassenger(@NotNull Entity arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "removePassenger", Entity.class);
    }

    @Override
    public boolean eject() {
        throw NotImplementedException.createByLazy(Entity.class, "eject");
    }

    @Override
    public float getFallDistance() {
        return this.entity.fallDistance().get().floatValue();
    }

    @Override
    public void setFallDistance(float arg0) {
        this.entity.fallDistance().set((double) arg0);
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        throw NotImplementedException.createByLazy(Entity.class, "getLastDamageCause");
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "setLastDamageCause", EntityDamageEvent.class);
    }

    @Override
    public int getTicksLived() {
        throw NotImplementedException.createByLazy(Entity.class, "getTicksLived");
    }

    @Override
    public void setTicksLived(int arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "setTicksLived", int.class);
    }

    @Override
    public void playEffect(@NotNull EntityEffect arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "playEffect", EntityEffect.class);
    }

    @Override
    public boolean isInsideVehicle() {
        return this.entity.vehicle().isPresent();
    }

    @Override
    public boolean leaveVehicle() {
        return this.entity.vehicle().map(mut -> {
            mut.set(null);
            return true;
        }).orElse(false);
    }

    @Override
    public Entity getVehicle() {
        throw NotImplementedException.createByLazy(Entity.class, "getVehicle");
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.entity.customNameVisible().get();
    }

    @Override
    public void setCustomNameVisible(boolean arg0) {
        this.entity.customNameVisible().set(arg0);
    }

    @Override
    public boolean isGlowing() {
        return this.entity.glowing().get();
    }

    @Override
    public void setGlowing(boolean arg0) {
        this.entity.glowing().set(arg0);
    }

    @Override
    public boolean isInvulnerable() {
        return this.entity.invulnerable().get();
    }

    @Override
    public void setInvulnerable(boolean arg0) {
        this.entity.invulnerable().set(arg0);
    }

    @Override
    public boolean isSilent() {
        return this.entity.silent().get();
    }

    @Override
    public void setSilent(boolean arg0) {
        this.entity.silent().set(arg0);
    }

    @Override
    public boolean hasGravity() {
        return this.entity.gravityAffected().get();
    }

    @Override
    public void setGravity(boolean arg0) {
        this.entity.gravityAffected().set(arg0);
    }

    @Override
    public int getPortalCooldown() {
        throw NotImplementedException.createByLazy(Entity.class, "getPortalCooldown");
    }

    @Override
    public void setPortalCooldown(int arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "setPortalCooldown", int.class);
    }

    @Override
    public @NotNull Set<String> getScoreboardTags() {
        throw NotImplementedException.createByLazy(Entity.class, "getScoreboardTags");
    }

    @Override
    public boolean addScoreboardTag(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "addScoreboardTag", String.class);
    }

    @Override
    public boolean removeScoreboardTag(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Entity.class, "removeScoreboardTag", String.class);
    }

    @Override
    public @NotNull PistonMoveReaction getPistonMoveReaction() {
        throw NotImplementedException.createByLazy(Entity.class, "getPistonMoveReaction");
    }

    @Override
    public @NotNull BlockFace getFacing() {
        var rotation = this.entity.rotation();
        var direction = Direction.closestHorizontal(rotation);
        return SoakDirectionMap.toBukkit(direction);
    }

    @Override
    public @NotNull Pose getPose() {
        throw NotImplementedException.createByLazy(Entity.class, "getPose");
    }

    @Override
    public Location getOrigin() {
        throw NotImplementedException.createByLazy(Entity.class, "getOrigin");
    }

    @Override
    public boolean fromMobSpawner() {
        throw NotImplementedException.createByLazy(Entity.class, "fromMobSpawner");
    }

    @Override
    public CreatureSpawnEvent.@NotNull SpawnReason getEntitySpawnReason() {
        throw NotImplementedException.createByLazy(Entity.class, "getEntitySpawnReason");
    }

    @Override
    public boolean isInRain() {
        throw NotImplementedException.createByLazy(Entity.class, "isInRain");
    }

    @Override
    public boolean isInBubbleColumn() {
        throw NotImplementedException.createByLazy(Entity.class, "isInBubbleColumn");
    }

    @Override
    public boolean isInWaterOrRain() {
        throw NotImplementedException.createByLazy(Entity.class, "isInWaterOrRain");
    }

    @Override
    public boolean isInWaterOrBubbleColumn() {
        throw NotImplementedException.createByLazy(Entity.class, "isInWaterOrBubbleColumn");
    }

    @Override
    public boolean isInWaterOrRainOrBubbleColumn() {
        throw NotImplementedException.createByLazy(Entity.class, "isInWaterOrRainOrBubbleColumn");
    }

    @Override
    public boolean isInLava() {
        throw NotImplementedException.createByLazy(Entity.class, "isInLava");
    }

    @Override
    public boolean isTicking() {
        throw NotImplementedException.createByLazy(Entity.class, "isTicking");
    }

    @Override
    public void setRotation(float arg0, float arg1) {
        throw NotImplementedException.createByLazy(Entity.class, "setRotation", float.class, float.class);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return new SoakMutablePersistentDataContainer<>(this.entity);
    }
}