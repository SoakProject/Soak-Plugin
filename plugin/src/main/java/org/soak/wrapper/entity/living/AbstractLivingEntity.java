package org.soak.wrapper.entity.living;

import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import net.kyori.adventure.audience.Audience;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.entity.AbstractEntity;
import org.soak.wrapper.entity.SoakAttributable;
import org.soak.wrapper.entity.SoakDamageable;
import org.soak.wrapper.entity.SoakProjectileSource;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.service.permission.Subject;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractLivingEntity<E extends Living> extends AbstractEntity<E> implements SoakDamageable, SoakAttributable, SoakProjectileSource, LivingEntity {

    public AbstractLivingEntity(Subject subject, Audience audience, E entity) {
        super(subject, audience, entity);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "rayTraceBlocks", double.class);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double arg0, @NotNull FluidCollisionMode arg1) {
        throw NotImplementedException.createByLazy(LivingEntity.class,
                "rayTraceBlocks",
                double.class,
                FluidCollisionMode.class);
    }

    @Override
    public TargetEntityInfo getTargetEntityInfo(int arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getTargetEntityInfo", int.class, boolean.class);
    }

    @Override
    public @NotNull List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getLastTwoTargetBlocks", Set.class, int.class);
    }

    @Override
    public void playPickupItemAnimation(@NotNull Item arg0, int arg1) {
        throw NotImplementedException.createByLazy(LivingEntity.class,
                "playPickupItemAnimation",
                Item.class,
                int.class);
    }

    @Override
    public Entity getTargetEntity(int arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getTargetEntity", int.class, boolean.class);
    }

    @Override
    public double getEyeHeight() {
        return this.spongeEntity().eyeHeight().get();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getEyeHeight", boolean.class);
    }

    @Override
    public @NotNull Location getEyeLocation() {
        return this.getLocation().add(0, getEyeHeight(), 0);
    }

    @Override
    public @NotNull List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getLineOfSight", Set.class, int.class);
    }

    @Override
    public @NotNull Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getLastTwoTargetBlocks", Set.class, int.class);
    }

    @Override
    public @Nullable Block getTargetBlock(int maxDistance, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        throw NotImplementedException.createByLazy(LivingEntity.class,
                "getTargetBlockExact",
                int.class,
                FluidCollisionMode.class);
    }

    @Override
    public @Nullable BlockFace getTargetBlockFace(int maxDistance, @NotNull TargetBlockInfo.FluidMode fluidMode) {
        throw NotImplementedException.createByLazy(LivingEntity.class,
                "getTargetBlockFace",
                int.class,
                TargetBlockInfo.FluidMode.class);
    }

    @Override
    public @Nullable TargetBlockInfo getTargetBlockInfo(int maxDistance, @NotNull TargetBlockInfo.FluidMode fluidMode) {
        throw NotImplementedException.createByLazy(LivingEntity.class,
                "getTargetBlockInfo",
                int.class,
                TargetBlockInfo.FluidMode.class);
    }

    @Override
    public Block getTargetBlockExact(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getTargetBlockExact", int.class);
    }

    @Override
    public @Nullable Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        throw NotImplementedException.createByLazy(LivingEntity.class,
                "getTargetBlockExact",
                int.class,
                FluidCollisionMode.class);
    }

    @Override
    public int getRemainingAir() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getRemainingAir");
    }

    @Override
    public void setRemainingAir(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setRemainingAir", int.class);
    }

    @Override
    public int getMaximumAir() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getMaximumAir");
    }

    @Override
    public void setMaximumAir(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setMaximumAir", int.class);
    }

    @Override
    public int getArrowCooldown() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getArrowCooldown");
    }

    @Override
    public void setArrowCooldown(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setArrowCooldown", int.class);
    }

    @Override
    public int getArrowsInBody() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getArrowsInBody");
    }

    @Override
    public void setArrowsInBody(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setArrowsInBody", int.class);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getMaximumNoDamageTicks");
    }

    @Override
    public void setMaximumNoDamageTicks(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setMaximumNoDamageTicks", int.class);
    }

    @Override
    public double getLastDamage() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getLastDamage");
    }

    @Override
    public void setLastDamage(double arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setLastDamage", double.class);
    }

    @Override
    public int getNoDamageTicks() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getNoDamageTicks");
    }

    @Override
    public void setNoDamageTicks(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setNoDamageTicks", int.class);
    }

    @Override
    public Player getKiller() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getKiller");
    }

    @Override
    public void setKiller(Player arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setKiller", Player.class);
    }

    @Deprecated
    @Override
    public boolean addPotionEffect(@NotNull PotionEffect arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(LivingEntity.class,
                "addPotionEffect",
                PotionEffect.class,
                boolean.class);
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "addPotionEffect", PotionEffect.class);
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "addPotionEffects", Collection.class);
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectType arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "hasPotionEffect", PotionEffectType.class);
    }

    @Override
    public PotionEffect getPotionEffect(@NotNull PotionEffectType arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getPotionEffect", PotionEffectType.class);
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectType arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "removePotionEffect", PotionEffectType.class);
    }

    @Override
    public @NotNull Collection<PotionEffect> getActivePotionEffects() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getActivePotionEffects");
    }

    @Override
    public boolean hasLineOfSight(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "hasLineOfSight", Location.class);
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "hasLineOfSight", Entity.class);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getRemoveWhenFarAway");
    }

    @Override
    public void setRemoveWhenFarAway(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setRemoveWhenFarAway", boolean.class);
    }

    @Override
    public EntityEquipment getEquipment() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getEquipment");
    }

    @Override
    public boolean getCanPickupItems() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getCanPickupItems");
    }

    @Override
    public void setCanPickupItems(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setCanPickupItems", boolean.class);
    }

    @Override
    public boolean isLeashed() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isLeashed");
    }

    @Override
    public @NotNull Entity getLeashHolder() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getLeashHolder");
    }

    @Override
    public boolean setLeashHolder(Entity arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setLeashHolder", Entity.class);
    }

    @Override
    public boolean isGliding() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isGliding");
    }

    @Override
    public void setGliding(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setGliding", boolean.class);
    }

    @Override
    public boolean isSwimming() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isSwimming");
    }

    @Override
    public void setSwimming(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setSwimming", boolean.class);
    }

    @Override
    public boolean isRiptiding() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isRiptiding");
    }

    @Override
    public boolean isSleeping() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isSleeping");
    }

    @Override
    public void setAI(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setAI", boolean.class);
    }

    @Override
    public boolean hasAI() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "hasAI");
    }

    @Override
    public void attack(@NotNull Entity arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "attack", Entity.class);
    }

    @Override
    public void swingMainHand() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "swingMainHand");
    }

    @Override
    public void swingOffHand() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "swingOffHand");
    }

    @Override
    public boolean isCollidable() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isCollidable");
    }

    @Override
    public void setCollidable(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setCollidable", boolean.class);
    }

    @Override
    public @NotNull Set<UUID> getCollidableExemptions() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getCollidableExemptions");
    }

    @Override
    public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getMemory", MemoryKey.class);
    }

    @Override
    public @NotNull EntityCategory getCategory() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getCategory");
    }

    @Override
    public boolean isInvisible() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isInvisible");
    }

    @Override
    public void setInvisible(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setInvisible", boolean.class);
    }

    @Override
    public int getArrowsStuck() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getArrowsStuck");
    }

    @Override
    public void setArrowsStuck(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setArrowsStuck", int.class);
    }

    @Override
    public int getShieldBlockingDelay() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getShieldBlockingDelay");
    }

    @Override
    public void setShieldBlockingDelay(int arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setShieldBlockingDelay", int.class);
    }

    @Override
    public ItemStack getActiveItem() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getActiveItem");
    }

    @Override
    public void clearActiveItem() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "clearActiveItem");
    }

    @Override
    public int getItemUseRemainingTime() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getItemUseRemainingTime");
    }

    @Override
    public int getHandRaisedTime() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getHandRaisedTime");
    }

    @Override
    public boolean isHandRaised() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isHandRaised");
    }

    @Override
    public @NotNull EquipmentSlot getHandRaised() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getHandRaised");
    }

    @Override
    public boolean isJumping() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "isJumping");
    }

    @Override
    public void setJumping(boolean arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setJumping", boolean.class);
    }

    @Override
    public float getHurtDirection() {
        throw NotImplementedException.createByLazy(LivingEntity.class, "getHurtDirection");
    }

    @Override
    public void setHurtDirection(float arg0) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setHurtDirection", float.class);
    }

    @Override
    public void setMemory(@NotNull MemoryKey arg0, Object arg1) {
        throw NotImplementedException.createByLazy(LivingEntity.class, "setMemory", MemoryKey.class, Object.class);
    }
}
