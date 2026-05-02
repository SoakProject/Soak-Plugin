package org.soak.map;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.cause.entity.SpawnType;
import org.spongepowered.api.event.cause.entity.SpawnTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Map;
import java.util.function.Predicate;

public class SoakSpawnReasonMap {

    public static Map<CreatureSpawnEvent.SpawnReason, Predicate<Cause>> toBukkit(SpawnType type) {
        if (SpawnTypes.SPAWN_EGG.get().equals(type)) {
            return Map.of(
                    CreatureSpawnEvent.SpawnReason.EGG,
                    cause -> cause
                            .first(Item.class)
                            .map(item -> item.item().get().type().equals(ItemTypes.EGG.get()))
                            .orElse(false),
                    CreatureSpawnEvent.SpawnReason.SPAWNER_EGG,
                    cause -> cause
                            .first(Item.class)
                            .map(item -> {
                                var itemType = item.item().get().type();
                                return !itemType.equals(ItemTypes.EGG.get());
                            }).orElse(false));
        }
        throw new RuntimeException("Unknown SpawnType of " + type.key(RegistryTypes.SPAWN_TYPE).asString());
    }

    public static CreatureSpawnEvent.SpawnReason toBukkit(SpawnType type, Cause cause) {
        return toBukkit(type)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().test(cause))
                .map(Map.Entry::getKey)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Unknown spawn type and cause combo"));
    }

}
