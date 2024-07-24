package org.soak.map.event.entity.player.data;

import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;

public class SoakToggleSneakEvent extends AbstractDataEvent<Boolean> {

    private final EventSingleListenerWrapper<PlayerDeathEvent> singleListenerWrapper;

    public SoakToggleSneakEvent(EventSingleListenerWrapper<PlayerDeathEvent> singleListenerWrapper) {
        this.singleListenerWrapper = singleListenerWrapper;
    }

    @Override
    public Key<? extends Value<Boolean>> keyValue() {
        return Keys.IS_SNEAKING;
    }

    @Override
    protected boolean isCorrectHolder(DataHolder.Mutable holder) {
        return holder instanceof ServerPlayer;
    }

    @Override
    protected void fireEvent(ChangeDataHolderEvent.ValueChange spongeEvent, EventPriority priority, DataHolder.Mutable spongePlayer, Boolean changedTo, Boolean changedFrom) {
        var player = SoakManager.<WrapperManager>getManager().getMemoryStore().get((ServerPlayer) spongePlayer);
        var event = new PlayerToggleSneakEvent(player, changedTo);

        SoakManager.<WrapperManager>getManager().getServer().getPluginManager().callEvent(this.singleListenerWrapper, event, priority);

        if (event.isCancelled()) {
            spongeEvent.setCancelled(event.isCancelled());
        }
    }
}
