package org.bukkit.entity;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakResourceKeyMap;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.entity.SoakEntity;
import org.soak.wrapper.entity.projectile.SoakFirework;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityCategories;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.entity.projectile.explosive.FireworkRocket;
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.function.Function;
import java.util.stream.Stream;

public enum EntityType implements Keyed, Translatable {
    ALLAY(EntityTypes.ALLAY),
    AREA_EFFECT_CLOUD(EntityTypes.AREA_EFFECT_CLOUD),
    ARMOR_STAND(EntityTypes.ARMOR_STAND),
    ARROW(EntityTypes.ARROW),
    BAT(EntityTypes.BAT),
    BEE(EntityTypes.BEE),
    BLAZE(EntityTypes.BLAZE),
    BLOCK_DISPLAY(EntityTypes.BLOCK_DISPLAY),
    BOAT(EntityTypes.BOAT),
    CAMEL(EntityTypes.CAMEL),
    CAT(EntityTypes.CAT),
    CAVE_SPIDER(EntityTypes.CAVE_SPIDER),
    CHICKEN(EntityTypes.CHICKEN),
    COD(EntityTypes.COD),
    COW(EntityTypes.COW),
    CREEPER(EntityTypes.CREEPER),
    DOLPHIN(EntityTypes.DOLPHIN),
    DONKEY(EntityTypes.DONKEY),
    DRAGON_FIREBALL(EntityTypes.DRAGON_FIREBALL),
    DROPPED_ITEM(EntityTypes.ITEM),
    DROWNED(EntityTypes.DROWNED),
    EGG(EntityTypes.EGG),
    ELDER_GUARDIAN(EntityTypes.ELDER_GUARDIAN),
    ENDER_CRYSTAL(EntityTypes.END_CRYSTAL),
    ENDER_DRAGON(EntityTypes.ENDER_DRAGON),
    ENDER_PEARL(EntityTypes.ENDER_PEARL),
    //ENDER_SIGNAL(EntityTypes.), <- WHAT IS THIS?
    ENDERMAN(EntityTypes.ENDERMAN),
    ENDERMITE(EntityTypes.ENDERMITE),
    EVOKER(EntityTypes.EVOKER),
    EVOKER_FANGS(EntityTypes.EVOKER_FANGS),
    EXPERIENCE_ORB(EntityTypes.EXPERIENCE_ORB),
    FALLING_BLOCK(EntityTypes.FALLING_BLOCK),
    FIREBALL(EntityTypes.FIREBALL),
    FIREWORK(EntityTypes.FIREWORK_ROCKET, (FireworkRocket rocket) -> new SoakFirework(Sponge.systemSubject(), Sponge.systemSubject(), rocket)),
    FISHING_HOOK(EntityTypes.FISHING_BOBBER),
    FOX(EntityTypes.FOX),
    FROG(EntityTypes.FROG),
    GHAST(EntityTypes.GHAST),
    GLOW_SQUID(EntityTypes.GLOW_SQUID),
    GIANT(EntityTypes.GIANT),
    GUARDIAN(EntityTypes.GUARDIAN),
    HOGLIN(EntityTypes.HOGLIN),
    HORSE(EntityTypes.HORSE),
    HUSK(EntityTypes.HUSK),
    ILLUSIONER(EntityTypes.ILLUSIONER),
    INTERACTION(EntityTypes.INTERACTION),
    IRON_GOLEM(EntityTypes.IRON_GOLEM),
    ITEM_DISPLAY(EntityTypes.ITEM_DISPLAY),
    ITEM_FRAME(EntityTypes.ITEM_FRAME),
    LEASH_HITCH(EntityTypes.LEASH_KNOT),
    LIGHTNING(EntityTypes.LIGHTNING_BOLT),
    LLAMA(EntityTypes.LLAMA),
    LLAMA_SPIT(EntityTypes.LLAMA_SPIT),
    MAGMA_CUBE(EntityTypes.MAGMA_CUBE),
    MINECART(EntityTypes.MINECART),
    MINECART_CHEST(EntityTypes.CHEST_MINECART),
    MINECART_COMMAND(EntityTypes.COMMAND_BLOCK_MINECART),
    MINECART_FURNACE(EntityTypes.FURNACE_MINECART),
    MINECART_HOPPER(EntityTypes.HOPPER_MINECART),
    MINECART_MOB_SPAWNER(EntityTypes.SPAWNER_MINECART),
    MINECART_TNT(EntityTypes.TNT_MINECART),
    MULE(EntityTypes.MULE),
    MUSHROOM_COW(EntityTypes.COW), //this hasn't existed in Minecraft as a type for a while. Its a variant of the cow
    OCELOT(EntityTypes.OCELOT),
    PAINTING(EntityTypes.PAINTING),
    PANDA(EntityTypes.PANDA),
    PARROT(EntityTypes.PARROT),
    PHANTOM(EntityTypes.PHANTOM),
    PIG(EntityTypes.PIG),
    PIGLIN(EntityTypes.PIGLIN),
    PIGLIN_BRUTE(EntityTypes.PIGLIN_BRUTE),
    PILLAGER(EntityTypes.PILLAGER),
    PLAYER(EntityTypes.PLAYER, (ServerPlayer player) -> SoakPlugin.plugin().getMemoryStore().get(player)),
    POLAR_BEAR(EntityTypes.POLAR_BEAR),
    PRIMED_TNT(EntityTypes.TNT),
    PUFFERFISH(EntityTypes.PUFFERFISH),
    RABBIT(EntityTypes.RABBIT),
    RAVAGER(EntityTypes.RAVAGER),
    SALMON(EntityTypes.SALMON),
    SHEEP(EntityTypes.SHEEP),
    SHULKER(EntityTypes.SHULKER),
    SHULKER_BULLET(EntityTypes.SHULKER_BULLET),
    SILVERFISH(EntityTypes.SILVERFISH),
    SKELETON(EntityTypes.SKELETON),
    SKELETON_HORSE(EntityTypes.SKELETON_HORSE),
    SLIME(EntityTypes.SLIME),
    SMALL_FIREBALL(EntityTypes.SMALL_FIREBALL),
    SNOWBALL(EntityTypes.SNOWBALL),
    SNOWMAN(EntityTypes.SNOW_GOLEM),
    SPECTRAL_ARROW(EntityTypes.SPECTRAL_ARROW),
    SPIDER(EntityTypes.SPIDER),
    SPLASH_POTION(EntityTypes.POTION),
    SQUID(EntityTypes.SQUID),
    STRAY(EntityTypes.STRAY),
    STRIDER(EntityTypes.STRIDER),
    TADPOLE(EntityTypes.TADPOLE),
    TEXT_DISPLAY(EntityTypes.TEXT_DISPLAY),
    THROWN_EXP_BOTTLE(EntityTypes.EXPERIENCE_BOTTLE),
    TRADER_LLAMA(EntityTypes.TRADER_LLAMA),
    TRIDENT(EntityTypes.TRIDENT),
    TROPICAL_FISH(EntityTypes.TROPICAL_FISH),
    TURTLE(EntityTypes.TURTLE),
    VEX(EntityTypes.VEX),
    VILLAGER(EntityTypes.VILLAGER),
    VINDICATOR(EntityTypes.VINDICATOR),
    WANDERING_TRADER(EntityTypes.WANDERING_TRADER),
    WARDON(EntityTypes.WARDEN),
    WITCH(EntityTypes.WITCH),
    WITHER(EntityTypes.WITHER),
    WITHER_SKELETON(EntityTypes.WITHER_SKELETON),
    WITHER_SKULL(EntityTypes.WITHER_SKULL),
    WOLF(EntityTypes.WOLF),
    ZOGLIN(EntityTypes.ZOGLIN),
    ZOMBIE(EntityTypes.ZOMBIE),
    ZOMBIE_HORSE(EntityTypes.ZOMBIE_HORSE),
    ZOMBIE_VILLAGER(EntityTypes.ZOMBIE_VILLAGER),
    ZOMBIFIED_PIGLIN(EntityTypes.ZOMBIFIED_PIGLIN),

    UNKNOWN(null);

    private final @Nullable DefaultedRegistryReference<? extends org.spongepowered.api.entity.EntityType<?>> spongeType;
    private final @NotNull Function<? extends org.spongepowered.api.entity.Entity, Entity> create;

    @Deprecated
    EntityType(@Nullable DefaultedRegistryReference<? extends org.spongepowered.api.entity.EntityType<?>> spongeType) {
        this(spongeType, entity -> new SoakEntity<>(Sponge.systemSubject(), Sponge.systemSubject(), entity));
    }

    <E extends org.spongepowered.api.entity.Entity> EntityType(@Nullable DefaultedRegistryReference<? extends org.spongepowered.api.entity.EntityType<?>> spongeType, Function<E, Entity> create) {
        this.spongeType = spongeType;
        this.create = create;
    }

    public static Stream<EntityType> stream() {
        return Stream.of(values()).filter(type -> type != UNKNOWN);
    }

    @Deprecated
    @Contract("null -> null")
    @Nullable
    public static EntityType fromName(@Nullable String name) {
        return stream().filter(type -> type.name().equals(name)).findAny().orElse(null);
    }

    @Deprecated
    @Nullable
    public static EntityType fromId(int id) {
        throw NotImplementedException.createByLazy(EntityType.class, "fromId", int.class);
    }

    public static EntityType fromSponge(org.spongepowered.api.entity.EntityType<?> type) {
        return stream()
                .filter(bType -> type.equals(bType.asSponge()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No mapping for EntityType of " + type.key(RegistryTypes.ENTITY_TYPE).formatted()));
    }

    public <E extends Entity> E postCreate(org.spongepowered.api.entity.Entity entity) {
        throw new RuntimeException("incorrect implementation");
    }

    @Deprecated
    public String getName() {
        if (this.spongeType == null) {
            return "Unknown";
        }
        return PlainTextComponentSerializer.plainText().serialize(this.spongeType.get().asComponent());
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        if (this.spongeType == null) {
            return new NamespacedKey("soak", "unknown");
        }
        return SoakResourceKeyMap.mapToBukkit(this.spongeType.get().key(RegistryTypes.ENTITY_TYPE));
    }

    @Nullable
    public Class<? extends Entity> getEntityClass() {
        throw NotImplementedException.createByLazy(EntityType.class, "getEntityClass");
    }

    public Class<? extends Entity> getSoakEntityClass() {
        throw NotImplementedException.createByLazy(EntityType.class, "getSoakEntityClass");
    }

    @Deprecated
    public short getTypeId() {
        throw NotImplementedException.createByLazy(EntityType.class, "getTypeId");
    }

    public boolean isSpawnable() {
        if (this.spongeType == null) {
            return false;
        }
        return this.spongeType.get().isSummonable();
    }

    public boolean isAlive() {
        if (this.spongeType == null) {
            return false;
        }
        var category = this.spongeType.get().category();
        return !category.equals(EntityCategories.AMBIENT.get()) && !category.equals(EntityCategories.WATER_AMBIENT.get()) && !category.equals(EntityCategories.MISC.get());
    }

    @Override
    public @NotNull String translationKey() {
        if (this.spongeType == null) {
            return "";
        }
        return ((Translatable) this.spongeType.get().asComponent()).translationKey();
    }

    public @Nullable org.spongepowered.api.entity.EntityType<?> asSponge() {
        if (this.spongeType == null) {
            return null;
        }
        return this.spongeType.get();
    }
}
