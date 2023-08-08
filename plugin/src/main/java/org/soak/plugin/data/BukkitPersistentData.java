package org.soak.plugin.data;

import org.soak.plugin.data.type.BukkitDataType;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataSerializable;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.LinkedTransferQueue;

public class BukkitPersistentData implements DataSerializable {

    public static int CONTENT_VERSION = 1;
    Collection<BukkitData<?>> data = new LinkedTransferQueue<>();

    public <T> Optional<T> getValue(ResourceKey key) {
        return this.data.parallelStream().filter(data -> data.getKey().equals(key)).findAny().map(data -> (T) data.getValue());
    }

    public void removeValue(ResourceKey key) {
        this.data.parallelStream().filter(data -> data.getKey().equals(key)).forEach(data -> this.data.remove(data));

    }


    public <T> void addValue(ResourceKey key, BukkitDataType<T> type, T value) {
        var data = new BukkitData<>(key, value, type);
        this.data.add(data);
    }

    public void addValue(BukkitData<?> data) {
        this.data.add(data);
    }

    @Override
    public int contentVersion() {
        return CONTENT_VERSION;
    }

    @Override
    public DataContainer toContainer() {
        var container = DataContainer.createNew();
        for (BukkitData<?> data : this.data) {
            container = data.set(container, DataQuery.of(data.getKey().namespace(), data.getKey().value()));
        }
        return container;
    }
}
