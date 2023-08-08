package org.bukkit.entity;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum EntityType implements Keyed, Translatable {

    AREA_EFFECT_CLOUD,
    ARMOR_STAND,
    ARROW,
    BAT,
    BEE,
    BLAZE,
    BOAT,
    CAT,
    CAVE_SPIDER,
    CHICKEN,
    COD,
    COW,
    CREEPER,
    DOLPHIN,
    DONKEY,
    DRAGON_FIREBALL,
    DROPPED_ITEM,
    DROWNED,
    EGG,
    ELDER_GUARDIAN,
    ENDER_CRYSTAL,
    ENDER_DRAGON,
    ENDER_PEARL,
    ENDER_SIGNAL,
    ENDERMAN,
    ENDERMITE,
    EVOKER,
    EVOKER_FANGS,
    EXPERIENCE_ORB,
    FALLING_BLOCK,
    FIREBALL,
    FIREWORK,
    FISHING_HOOK,
    FOX,
    GHAST,
    GIANT,
    GUARDIAN,
    HOGLIN,
    HORSE,
    HUSK,
    ILLUSIONER,
    IRON_GOLEM,
    ITEM_FRAME,
    LEASH_HITCH,
    LIGHTNING,
    LLAMA,
    LLAMA_SPIT,
    MAGMA_CUBE,
    MINECART,
    MINECART_CHEST,
    MINECART_COMMAND,
    MINECART_FURNACE,
    MINECART_HOPPER,
    MINECART_MOB_SPAWNER,
    MINECART_TNT,
    MULE,
    MUSHROOM_COW,
    OCELOT,
    PAINTING,
    PANDA,
    PARROT,
    PHANTOM,
    PIG,
    PIGLIN,
    PIGLIN_BRUTE,
    PILLAGER,
    PLAYER,
    POLAR_BEAR,
    PRIMED_TNT,
    PUFFERFISH,
    RABBIT,
    RAVAGER,
    SALMON,
    SHEEP,
    SHULKER,
    SHULKER_BULLET,
    SILVERFISH,
    SKELETON,
    SKELETON_HORSE,
    SLIME,
    SMALL_FIREBALL,
    SNOWBALL,
    SNOWMAN,
    SPECTRAL_ARROW,
    SPIDER,
    SPLASH_POTION,
    SQUID,
    STRAY,
    STRIDER,
    THROWN_EXP_BOTTLE,
    TRADER_LLAMA,
    TRIDENT,
    TROPICAL_FISH,
    TURTLE,
    UNKNOWN,
    VEX,
    VILLAGER,
    VINDICATOR,
    WANDERING_TRADER,
    WITCH,
    WITHER,
    WITHER_SKELETON,
    WITHER_SKULL,
    WOLF,
    ZOGLIN,
    ZOMBIE,
    ZOMBIE_HORSE,
    ZOMBIE_VILLAGER,
    ZOMBIFIED_PIGLIN;

    private EntityType() {
        throw new RuntimeException("Incorrect implementation");
    }

    @Deprecated
    @Contract("null -> null")
    @Nullable
    public static EntityType fromName(@Nullable String name) {
        throw new RuntimeException("Incorrect implementation");

    }

    @Deprecated
    @Nullable
    public static EntityType fromId(int id) {
        throw new RuntimeException("Incorrect implementation");
    }

    @Deprecated
    @Nullable
    public String getName() {
        throw new RuntimeException("Incorrect implementation");
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        throw new RuntimeException("Incorrect implementation");
    }

    @Nullable
    public Class<? extends Entity> getEntityClass() {
        throw new RuntimeException("Incorrect implementation");
    }

    @Deprecated
    public short getTypeId() {
        throw new RuntimeException("Incorrect implementation");
    }

    public boolean isSpawnable() {
        throw new RuntimeException("Incorrect implementation");
    }

    public boolean isAlive() {
        throw new RuntimeException("Incorrect implementation");
    }

    @Override
    public @NotNull String translationKey() {
        throw new RuntimeException("Incorrect implementation");
    }

    public @NotNull org.spongepowered.api.entity.EntityType<?> asSponge(){
        throw new RuntimeException("Incorrect implementation");
    }

    public static EntityType fromSponge(org.spongepowered.api.entity.EntityType<?> type){
        throw new RuntimeException("Incorrect implementation");
    }
}
