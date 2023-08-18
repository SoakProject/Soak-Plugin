package org.soak.impl.event.world;

import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.world.LoadWorldEvent;

public class SoakWorldLoadEvent {

    private final EventSingleListenerWrapper<WorldLoadEvent> singleEventListener;


    public SoakWorldLoadEvent(EventSingleListenerWrapper<WorldLoadEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(LoadWorldEvent event) {
        fireEvent(event, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(LoadWorldEvent event) {
        fireEvent(event, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(LoadWorldEvent event) {
        fireEvent(event, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(LoadWorldEvent event) {
        fireEvent(event, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(LoadWorldEvent event) {
        fireEvent(event, EventPriority.LOWEST);
    }

    private void fireEvent(LoadWorldEvent spongeEvent, EventPriority priority) {
        var bukkitWorld = new SoakWorld(spongeEvent.world());
        var bukkitEvent = new WorldLoadEvent(bukkitWorld);
        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        //TODO -> Bukkit event is able to be cancelled, sponge is not
    }
}
