package org.soak.map;

import org.bukkit.persistence.PersistentDataType;
import org.soak.plugin.data.type.BukkitDataType;
import org.soak.plugin.data.type.BukkitDataTypes;

public class SoakPersistentDataMap {

    public static <T> BukkitDataType<T> toSoak(PersistentDataType<?, T> dataType) {
        return BukkitDataTypes.TYPES
                .get()
                .parallelStream()
                .filter(type -> type.typeClass().equals(dataType.getPrimitiveType()))
                .findAny()
                .map(type -> (BukkitDataType<T>) type)
                .orElseThrow(() -> new RuntimeException("No direct mapping for " + dataType.getPrimitiveType().getSimpleName()));
    }
}
