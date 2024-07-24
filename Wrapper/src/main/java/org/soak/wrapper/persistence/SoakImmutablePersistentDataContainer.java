package org.soak.wrapper.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.soak.data.sponge.SoakKeys;
import org.soak.exception.NotImplementedException;
import org.soak.data.BukkitPersistentData;
import org.soak.map.SoakPersistentDataMap;
import org.soak.map.SoakResourceKeyMap;
import org.spongepowered.api.data.SerializableDataHolder;

import java.io.IOException;
import java.util.Optional;

public class SoakImmutablePersistentDataContainer<H extends SerializableDataHolder.Immutable<?>> extends AbstractPersistentData<H> implements PersistentDataContainer {

    public SoakImmutablePersistentDataContainer(H container) {
        super(container);
    }

    @Override
    public void remove(@NotNull NamespacedKey arg0) {
        BukkitPersistentData data = this.holder.get(SoakKeys.BUKKIT_DATA).orElseGet(BukkitPersistentData::new);
        data.removeValue(SoakResourceKeyMap.mapToSponge(arg0));
        Optional<?> opWith = this.holder.with(SoakKeys.BUKKIT_DATA, data);
        if (opWith.isPresent()) {
            this.holder = (H) opWith.get();
            return;
        }
        throw new RuntimeException("Could not offer BukkitPersistentData to " + this.holder.getClass().getSimpleName());
    }

    @Override
    public boolean has(@NotNull NamespacedKey namespacedKey) {
        BukkitPersistentData data = this.holder.get(SoakKeys.BUKKIT_DATA).orElseGet(BukkitPersistentData::new);
        return data.getValue(SoakResourceKeyMap.mapToSponge(namespacedKey)).isPresent();
    }

    @Override
    public byte @NotNull [] serializeToBytes() throws IOException {
        throw NotImplementedException.createByLazy(PersistentDataContainer.class, "serializeToBytes");
    }

    @Override
    public void readFromBytes(byte @NotNull [] bytes, boolean b) throws IOException {
throw NotImplementedException.createByLazy(PersistentDataContainer.class, "readFromBytes", byte.class, boolean.class);
    }

    @Override
    public <T, Z> void set(@NotNull NamespacedKey arg0, @NotNull PersistentDataType<T, Z> arg1, @NotNull Z arg2) {
        BukkitPersistentData data = this.holder.get(SoakKeys.BUKKIT_DATA).orElseGet(BukkitPersistentData::new);
        data.addValue(SoakResourceKeyMap.mapToSponge(arg0), SoakPersistentDataMap.toSoak(arg1), arg2);
        Optional<?> opWith = this.holder.with(SoakKeys.BUKKIT_DATA, data);
        if (opWith.isPresent()) {
            this.holder = (H) opWith.get();
            return;
        }
        throw new RuntimeException("Could not offer BukkitPersistentData to " + this.holder.getClass().getSimpleName());
    }

}