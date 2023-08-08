package org.soak.wrapper.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;
import org.spongepowered.api.data.DataHolder;

import java.util.Set;

public abstract class AbstractPersistentData<DH extends DataHolder> implements PersistentDataContainer {

    protected DH holder;

    protected AbstractPersistentData(DH holder) {
        this.holder = holder;
    }

    public DH getHolder() {
        return this.holder;
    }

    @Override
    public @NotNull Set<NamespacedKey> getKeys() {
        throw NotImplementedException.createByLazy(PersistentDataContainer.class, "getKeys");
    }

    @Override
    public <T, Z> boolean has(@NotNull NamespacedKey arg0, @NotNull PersistentDataType<T, Z> arg1) {
        throw NotImplementedException.createByLazy(PersistentDataContainer.class, "has", NamespacedKey.class, PersistentDataType.class);
    }

    @Override
    public @NotNull PersistentDataAdapterContext getAdapterContext() {
        throw NotImplementedException.createByLazy(PersistentDataContainer.class, "getAdapterContext");
    }

    @Override
    public <T, Z> Z get(@NotNull NamespacedKey arg0, @NotNull PersistentDataType<T, Z> arg1) {
        throw NotImplementedException.createByLazy(PersistentDataContainer.class, "get", NamespacedKey.class, PersistentDataType.class);
    }

    @Override
    public boolean isEmpty() {
        throw NotImplementedException.createByLazy(PersistentDataContainer.class, "isEmpty");
    }

    @Override
    public <T, Z> @NotNull Z getOrDefault(@NotNull NamespacedKey arg0, @NotNull PersistentDataType<T, Z> arg1, @NotNull Z arg2) {
        throw NotImplementedException.createByLazy(PersistentDataContainer.class, "getOrDefault", NamespacedKey.class, PersistentDataType.class, Object.class);
    }
}
