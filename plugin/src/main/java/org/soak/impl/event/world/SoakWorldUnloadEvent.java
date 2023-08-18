package org.soak.impl.event.world;

import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldUnloadEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.world.UnloadWorldEvent;

public class SoakWorldUnloadEvent {

    private final EventSingleListenerWrapper<WorldUnloadEvent> singleEventListener;


    public SoakWorldUnloadEvent(EventSingleListenerWrapper<WorldUnloadEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(UnloadWorldEvent event) {
        fireEvent(event, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(UnloadWorldEvent event) {
        fireEvent(event, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(UnloadWorldEvent event) {
        fireEvent(event, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(UnloadWorldEvent event) {
        fireEvent(event, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(UnloadWorldEvent event) {
        fireEvent(event, EventPriority.LOWEST);
    }

    private void fireEvent(UnloadWorldEvent spongeEvent, EventPriority priority) {
        var bukkitWorld = new SoakWorld(spongeEvent.world());
        var bukkitEvent = new WorldUnloadEvent(bukkitWorld);
        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        //TODO -> Bukkit event is able to be cancelled, sponge is not
    }
}
