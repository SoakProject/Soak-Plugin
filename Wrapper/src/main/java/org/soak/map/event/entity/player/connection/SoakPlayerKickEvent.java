package org.soak.map.event.entity.player.connection;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.living.player.KickPlayerEvent;

public class SoakPlayerKickEvent {

    private final EventSingleListenerWrapper<PlayerKickEvent> singleListenerWrapper;

    public SoakPlayerKickEvent(EventSingleListenerWrapper<PlayerKickEvent> singleListenerWrapper) {
        this.singleListenerWrapper = singleListenerWrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(KickPlayerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(KickPlayerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(KickPlayerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(KickPlayerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(KickPlayerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }


    private void fireEvent(KickPlayerEvent event, EventPriority priority) {
        var player = SoakManager.<WrapperManager>getManager().getMemoryStore().get(event.player());
        var message = event.message();

        var bukkitEvent = new PlayerKickEvent(player, message, message); //TODO -> PlayerKickEvent.Cause
        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleListenerWrapper, bukkitEvent, priority);

        if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
        }
        if (!bukkitEvent.leaveMessage().equals(message)) {
            event.setMessage(bukkitEvent.leaveMessage());
        }
    }

}
