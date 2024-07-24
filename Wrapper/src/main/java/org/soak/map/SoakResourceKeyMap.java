package org.soak.map;

import org.bukkit.NamespacedKey;
import org.spongepowered.api.ResourceKey;

public class SoakResourceKeyMap {

    public static ResourceKey mapToSponge(NamespacedKey key) {
        return ResourceKey.of(key.namespace(), key.getKey());
    }

    public static NamespacedKey mapToBukkit(ResourceKey key) {
        return NamespacedKey.fromString(key.formatted());
    }
}
