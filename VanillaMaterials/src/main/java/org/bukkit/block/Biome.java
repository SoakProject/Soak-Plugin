package org.bukkit.block;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakResourceKeyMap;
import org.soak.plugin.exception.NotImplementedException;
import org.spongepowered.api.registry.RegistryReference;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.biome.Biomes;

import java.util.Arrays;
import java.util.Objects;

public enum Biome {

    BADLANDS(Biomes.BADLANDS),
    BAMBOO_JUNGLE(Biomes.BAMBOO_JUNGLE),
    BASALT_DELTAS(Biomes.BASALT_DELTAS),
    BEACH(Biomes.BEACH),
    BIRCH_FOREST(Biomes.BIRCH_FOREST),
    CHERRY_GROVE(Biomes.GROVE),
    COLD_OCEAN(Biomes.COLD_OCEAN),
    CRIMSON_FOREST(Biomes.CRIMSON_FOREST),
    CUSTOM(null),
    DARK_FOREST(Biomes.DARK_FOREST),

    DEEP_COLD_OCEAN(Biomes.DEEP_COLD_OCEAN),

    DEEP_DARK(Biomes.DEEP_DARK),

    DEEP_FROZEN_OCEAN(Biomes.DEEP_FROZEN_OCEAN),

    DEEP_LUKEWARM_OCEAN(Biomes.DEEP_LUKEWARM_OCEAN),

    DEEP_OCEAN(Biomes.DEEP_OCEAN),

    DESERT(Biomes.DESERT),

    DRIPSTONE_CAVES(Biomes.DRIPSTONE_CAVES),

    END_BARRENS(Biomes.END_BARRENS),

    END_HIGHLANDS(Biomes.END_HIGHLANDS),

    END_MIDLANDS(Biomes.END_MIDLANDS),

    ERODED_BADLANDS(Biomes.ERODED_BADLANDS),

    FLOWER_FOREST(Biomes.FLOWER_FOREST),

    FOREST(Biomes.FOREST),

    FROZEN_OCEAN(Biomes.FROZEN_OCEAN),

    FROZEN_PEAKS(Biomes.FROZEN_PEAKS),

    FROZEN_RIVER(Biomes.FROZEN_RIVER),

    GROVE(Biomes.GROVE),

    ICE_SPIKES(Biomes.ICE_SPIKES),

    JAGGED_PEAKS(Biomes.JAGGED_PEAKS),

    JUNGLE(Biomes.JUNGLE),

    LUKEWARM_OCEAN(Biomes.LUKEWARM_OCEAN),

    LUSH_CAVES(Biomes.LUSH_CAVES),

    MANGROVE_SWAMP(Biomes.MANGROVE_SWAMP),

    MEADOW(Biomes.MEADOW),

    MUSHROOM_FIELDS(Biomes.MUSHROOM_FIELDS),

    NETHER_WASTES(Biomes.NETHER_WASTES),

    OCEAN(Biomes.OCEAN),

    OLD_GROWTH_BIRCH_FOREST(Biomes.OLD_GROWTH_BIRCH_FOREST),

    OLD_GROWTH_PINE_TAIGA(Biomes.OLD_GROWTH_PINE_TAIGA),

    OLD_GROWTH_SPRUCE_TAIGA(Biomes.OLD_GROWTH_SPRUCE_TAIGA),

    PLAINS(Biomes.PLAINS),

    RIVER(Biomes.RIVER),

    SAVANNA(Biomes.SAVANNA),

    SAVANNA_PLATEAU(Biomes.SAVANNA_PLATEAU),

    SMALL_END_ISLANDS(Biomes.SMALL_END_ISLANDS),

    SNOWY_BEACH(Biomes.SNOWY_BEACH),

    SNOWY_PLAINS(Biomes.SNOWY_PLAINS),

    SNOWY_SLOPES(Biomes.SNOWY_SLOPES),

    SNOWY_TAIGA(Biomes.SNOWY_TAIGA),

    SOUL_SAND_VALLEY(Biomes.SOUL_SAND_VALLEY),

    SPARSE_JUNGLE(Biomes.SPARSE_JUNGLE),

    STONY_PEAKS(Biomes.STONY_PEAKS),

    STONY_SHORE(Biomes.STONY_SHORE),

    SUNFLOWER_PLAINS(Biomes.SUNFLOWER_PLAINS),

    SWAMP(Biomes.SWAMP),

    TAIGA(Biomes.TAIGA),

    THE_END(Biomes.THE_END),

    THE_VOID(Biomes.THE_VOID),

    WARM_OCEAN(Biomes.WARM_OCEAN),

    WARPED_FOREST(Biomes.WARPED_FOREST),

    WINDSWEPT_FOREST(Biomes.WINDSWEPT_FOREST),

    WINDSWEPT_GRAVELLY_HILLS(Biomes.WINDSWEPT_GRAVELLY_HILLS),

    WINDSWEPT_HILLS(Biomes.WINDSWEPT_HILLS),

    WINDSWEPT_SAVANNA(Biomes.WINDSWEPT_SAVANNA),

    WOODED_BADLANDS(Biomes.WOODED_BADLANDS);

    private final @Nullable RegistryReference<org.spongepowered.api.world.biome.Biome> biome;

    Biome(@Nullable RegistryReference<org.spongepowered.api.world.biome.Biome> biome) {
        this.biome = biome;
    }

    public static Biome fromSponge(org.spongepowered.api.world.biome.Biome biome) {
        return Arrays
                .stream(values())
                .filter(bukkitBiome -> bukkitBiome.asSponge() != null)
                .filter(bukkitBiome -> biome.equals(bukkitBiome.asSponge()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Could not find a mapping for " + biome.key(RegistryTypes.BIOME).asString()));
    }

    public @Nullable org.spongepowered.api.world.biome.Biome asSponge() {
        if (this.biome == null) {
            return null;
        }
        return this.biome.get();
    }

    public @NotNull NamespacedKey getKey() {
        var sponge = asSponge();
        if (sponge == null) {
            return Objects.requireNonNull(NamespacedKey.fromString("soak:custom"));
        }
        return SoakResourceKeyMap.mapToBukkit(sponge.key(RegistryTypes.BIOME));
    }

    public @NotNull String translationKey() {
        throw NotImplementedException.createByLazy(Biome.class, "translationKey");
    }
}
