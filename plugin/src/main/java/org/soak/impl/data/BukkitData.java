package org.soak.impl.data;

import org.soak.impl.data.type.BukkitDataType;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;

public class BukkitData<T> {

    public static final DataQuery PLUGIN = DataQuery.of("plugin");
    public static final DataQuery PATH = DataQuery.of("path");
    public static final DataQuery TYPE = DataQuery.of("type");
    public static final DataQuery VALUE = DataQuery.of("value");
    private final ResourceKey path;
    private final BukkitDataType<T> type;
    private final T value;

    public BukkitData(ResourceKey key, T value, BukkitDataType<T> type) {
        this.path = key;
        this.value = value;
        this.type = type;
    }

    public DataContainer set(DataContainer container, DataQuery from) {
        container = container.set(from.then(PLUGIN), path.namespace());
        container = container.set(from.then(PATH), path.value());
        container = container.set(from.then(TYPE), this.type.typeName());
        return this.type.set(container, from.then(VALUE), this.value);
    }

    public T getValue() {
        return this.value;
    }

    public BukkitDataType<T> getType() {
        return this.type;
    }

    public ResourceKey getKey() {
        return this.path;
    }

}
