package org.soak.data.type;

import org.bukkit.persistence.PersistentDataType;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataView;

import java.util.Optional;

public interface BukkitDataType<T> {

    Optional<T> get(DataView view, DataQuery from);

    String typeName();

    Class<?> typeClass();

    default boolean isType(Class<?> classType) {
        return classType.isAssignableFrom(typeClass());
    }

    DataContainer set(DataContainer container, DataQuery from, T value);

    PersistentDataType<?, T> toBukkit();
}
