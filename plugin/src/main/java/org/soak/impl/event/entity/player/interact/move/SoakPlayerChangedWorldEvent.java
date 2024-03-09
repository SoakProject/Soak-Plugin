package org.soak.impl.event.entity.player.interact.move;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;

public class SoakPlayerChangedWorldEvent {

    private final EventSingleListenerWrapper<PlayerChangedWorldEvent> singleEventListener;

    public SoakPlayerChangedWorldEvent(EventSingleListenerWrapper<PlayerChangedWorldEvent> listener) {
        this.singleEventListener = listener;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(RespawnPlayerEvent.SelectWorld event) {
        fireEvent(event, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(RespawnPlayerEvent.SelectWorld event) {
        fireEvent(event, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(RespawnPlayerEvent.SelectWorld event) {
        fireEvent(event, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(RespawnPlayerEvent.SelectWorld event) {
        fireEvent(event, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(RespawnPlayerEvent.SelectWorld event) {
        fireEvent(event, EventPriority.LOWEST);
    }

    private void fireEvent(RespawnPlayerEvent.SelectWorld event, EventPriority priority) {
        var spongePlayer = event.entity();
        var spongeFromWorld = spongePlayer.world();

        var bukkitPlayer = SoakPlugin.plugin().getMemoryStore().get(spongePlayer);
        var bukkitWorld = SoakPlugin.plugin().getMemoryStore().get(spongeFromWorld);
        PlayerChangedWorldEvent bukkitEvent = new PlayerChangedWorldEvent(bukkitPlayer, bukkitWorld);

        var soakPlugin = (SoakPlugin) singleEventListener.plugin();
        Sponge.server().scheduler().executor(soakPlugin.container()).execute(() -> SoakPlugin.server().getPluginManager().callEvent(singleEventListener, bukkitEvent, priority));
    }
}
