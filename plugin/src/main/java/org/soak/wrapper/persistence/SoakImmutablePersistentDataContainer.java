package org.soak.wrapper.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakPersistentDataMap;
import org.soak.map.SoakResourceKeyMap;
import org.soak.plugin.SoakPlugin;
import org.soak.impl.data.BukkitPersistentData;
import org.spongepowered.api.data.SerializableDataHolder;

import java.util.Optional;

public class SoakImmutablePersistentDataContainer<H extends SerializableDataHolder.Immutable<?>> extends AbstractPersistentData<H> implements PersistentDataContainer {

    public SoakImmutablePersistentDataContainer(H container) {
        super(container);
    }

    @Override
    public void remove(@NotNull NamespacedKey arg0) {
        BukkitPersistentData data = this.holder.get(SoakPlugin.BUKKIT_DATA).orElseGet(BukkitPersistentData::new);
        data.removeValue(SoakResourceKeyMap.mapToSponge(arg0));
        Optional<?> opWith = this.holder.with(SoakPlugin.BUKKIT_DATA, data);
        if (opWith.isPresent()) {
            this.holder = (H) opWith.get();
            return;
        }
        throw new RuntimeException("Could not offer BukkitPersistentData to " + this.holder.getClass().getSimpleName());
    }

    @Override
    public <T, Z> void set(@NotNull NamespacedKey arg0, @NotNull PersistentDataType<T, Z> arg1, @NotNull Z arg2) {
        BukkitPersistentData data = this.holder.get(SoakPlugin.BUKKIT_DATA).orElseGet(BukkitPersistentData::new);
        data.addValue(SoakResourceKeyMap.mapToSponge(arg0), SoakPersistentDataMap.toSoak(arg1), arg2);
        Optional<?> opWith = this.holder.with(SoakPlugin.BUKKIT_DATA, data);
        if (opWith.isPresent()) {
            this.holder = (H) opWith.get();
            return;
        }
        throw new RuntimeException("Could not offer BukkitPersistentData to " + this.holder.getClass().getSimpleName());
    }

}