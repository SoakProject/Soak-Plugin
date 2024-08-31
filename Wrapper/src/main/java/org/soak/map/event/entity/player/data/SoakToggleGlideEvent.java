package org.soak.map.event.entity.player.data;

import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;

public class SoakToggleGlideEvent extends AbstractDataEvent<Boolean> {

    private final EventSingleListenerWrapper<PlayerDeathEvent> singleListenerWrapper;

    public SoakToggleGlideEvent(EventSingleListenerWrapper<PlayerDeathEvent> wrapper) {
        this.singleListenerWrapper = wrapper;
    }

    @Override
    public Key<? extends Value<Boolean>> keyValue() {
        return Keys.IS_ELYTRA_FLYING;
    }

    @Override
    protected boolean isCorrectHolder(DataHolder.Mutable holder) {
        return holder instanceof Living;
    }

    @Override
    protected void fireEvent(ChangeDataHolderEvent.ValueChange spongeEvent, EventPriority priority, DataHolder.Mutable spongeHolder, Boolean changedTo, Boolean changedFrom) {
        var living = AbstractEntity.wrap((Living) spongeHolder);
        var event = new EntityToggleGlideEvent(living, changedTo);

        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleListenerWrapper, event, priority);

        if (event.isCancelled()) {
            spongeEvent.setCancelled(event.isCancelled());
        }
    }
}
