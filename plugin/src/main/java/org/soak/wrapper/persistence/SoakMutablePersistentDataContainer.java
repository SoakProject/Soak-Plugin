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

public class SoakMutablePersistentDataContainer<Holder extends SerializableDataHolder.Mutable> extends AbstractPersistentData<Holder> implements PersistentDataContainer {

    public SoakMutablePersistentDataContainer(Holder container) {
        super(container);
    }

    @Override
    public void remove(@NotNull NamespacedKey arg0) {
        BukkitPersistentData data = this.holder.get(SoakPlugin.BUKKIT_DATA).orElseGet(BukkitPersistentData::new);
        data.removeValue(SoakResourceKeyMap.mapToSponge(arg0));
        if (this.holder.offer(SoakPlugin.BUKKIT_DATA, data).isSuccessful()) {
            return;
        }
        throw new RuntimeException("Could not offer BukkitPersistentData to " + this.holder.getClass().getSimpleName());
    }

    @Override
    public <T, Z> void set(@NotNull NamespacedKey arg0, @NotNull PersistentDataType<T, Z> arg1, @NotNull Z arg2) {
        BukkitPersistentData data = this.holder.get(SoakPlugin.BUKKIT_DATA).orElseGet(BukkitPersistentData::new);
        data.addValue(SoakResourceKeyMap.mapToSponge(arg0), SoakPersistentDataMap.toSoak(arg1), arg2);
        if (this.holder.offer(SoakPlugin.BUKKIT_DATA, data).isSuccessful()) {
            return;
        }
        throw new RuntimeException("Could not offer BukkitPersistentData to " + this.holder.getClass().getSimpleName());
    }
}