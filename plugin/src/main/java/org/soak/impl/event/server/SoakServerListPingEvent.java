package org.soak.impl.event.server;

import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.StatusResponse;

public class SoakServerListPingEvent {

    private final EventSingleListenerWrapper<ServerListPingEvent> singleEventListener;

    public SoakServerListPingEvent(EventSingleListenerWrapper<ServerListPingEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(ClientPingServerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ClientPingServerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ClientPingServerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ClientPingServerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ClientPingServerEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }

    private void fireEvent(ClientPingServerEvent spongeEvent, EventPriority priority) {
        var address = spongeEvent.client().address();

        ServerListPingEvent bEvent = new ServerListPingEvent(
                address.getHostName(),
                address.getAddress(),
                spongeEvent.response().description(),
                spongeEvent
                        .response()
                        .players()
                        .map(StatusResponse.Players::online)
                        .orElse(0),
                spongeEvent
                        .response()
                        .players()
                        .map(StatusResponse.Players::max)
                        .orElse(0));

        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bEvent, priority);
    }

}
