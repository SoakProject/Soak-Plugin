package org.soak.impl.event.inventory.action.furnance;

import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.block.SoakBlock;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.entity.CookingEvent;

public class SoakBurnItemEvent {

    private final EventSingleListenerWrapper<FurnaceBurnEvent> singleEventListener;

    public SoakBurnItemEvent(EventSingleListenerWrapper<FurnaceBurnEvent> event) {
        this.singleEventListener = event;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(CookingEvent.ConsumeFuel spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(CookingEvent.ConsumeFuel spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(CookingEvent.ConsumeFuel spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(CookingEvent.ConsumeFuel spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(CookingEvent.ConsumeFuel spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }


    private void fireEvent(CookingEvent.ConsumeFuel event, EventPriority priority) {
        if (event.fuel().isEmpty()) {
            //How does this happen?
            return;
        }
        var furnace = new SoakBlock(event.blockEntity()
                .locatableBlock()
                .location()
                .onServer()
                .orElseThrow(() -> new RuntimeException("Location for server could not be created")));
        var consumedItem = SoakItemStackMap.toBukkit(event.fuel().get());
        var burnTime = event.fuel().get().get(Keys.BURN_TIME); //probably not right
        var bukkitEvent = new FurnaceBurnEvent(furnace, consumedItem, burnTime.orElse(0));

        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
        }

    }
}
