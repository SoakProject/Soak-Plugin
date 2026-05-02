package org.soak.map;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.attribute.type.AttributeType;
import org.spongepowered.api.entity.attribute.type.AttributeTypes;
import org.spongepowered.api.registry.DefaultedRegistryType;
import org.spongepowered.api.registry.DefaultedRegistryValue;

public class SoakRegistryMap {

    public static <T extends Keyed, K extends DefaultedRegistryValue<K>> T toBukkit(Registry<T> bukkitRegistry, DefaultedRegistryType<K> registryType, K spongeValue) {
        return bukkitRegistry.get(SoakResourceKeyMap.mapToBukkit(spongeValue.key(registryType)));
    }

    public static <T extends Keyed, K extends DefaultedRegistryValue<K>> T toBukkit(RegistryKey<T> bukkitRegistryKey, DefaultedRegistryType<K> registryType, K spongeValue) {
        return toBukkit(RegistryAccess.registryAccess().getRegistry(bukkitRegistryKey), registryType, spongeValue);
    }

    public static <T extends Keyed, K extends DefaultedRegistryValue<K>> K toSponge(org.spongepowered.api.registry.Registry<K> registryKey, T value) {
        return registryKey.findValue(SoakResourceKeyMap.mapToSponge(value.key())).orElseThrow(() -> new RuntimeException("Cannot find '" + value.key().asMinimalString() + "' from registry: " + registryKey.type().root().asMinimalString()));
    }

    public static AttributeType toSponge(@NotNull Attribute attribute) {
        return toSponge(AttributeTypes.registry(), attribute);
    }

    public static BlockType toSpongeBlock(Material material) {
        return toSponge(material.asBlockType());
    }

    public static BlockType toSponge(org.bukkit.block.BlockType type) {
        return toSponge(BlockTypes.registry(), type);
    }
}
