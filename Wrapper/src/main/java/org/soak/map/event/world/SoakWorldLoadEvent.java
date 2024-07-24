package org.soak.map.event.world;

import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
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
        var bukkitWorld = SoakManager.<WrapperManager>getManager().getMemoryStore().get(spongeEvent.world());
        var bukkitEvent = new WorldLoadEvent(bukkitWorld);
        SoakManager.<WrapperManager>getManager().getServer().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        //TODO -> Bukkit event is able to be cancelled, sponge is not
    }
}
