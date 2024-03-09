package org.soak.impl.event.entity.move;

import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPortalEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.map.SoakLocationMap;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;

public class SoakPortalTeleportEntityEvent {

    private final EventSingleListenerWrapper<EntityPortalEvent> singleEventListener;

    public SoakPortalTeleportEntityEvent(EventSingleListenerWrapper<EntityPortalEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(ChangeEntityWorldEvent.Reposition event) {
        fireEvent(event, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ChangeEntityWorldEvent.Reposition event) {
        fireEvent(event, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ChangeEntityWorldEvent.Reposition event) {
        fireEvent(event, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ChangeEntityWorldEvent.Reposition event) {
        fireEvent(event, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ChangeEntityWorldEvent.Reposition event) {
        fireEvent(event, EventPriority.LOWEST);
    }

    private void fireEvent(ChangeEntityWorldEvent.Reposition event, EventPriority priority) {
        var spongeToLocation = event.entity().world().location(event.destinationPosition());
        var spongeFromLocation = event.entity().world().location(event.originalPosition());

        var bukkitEntity = AbstractEntity.wrap(event.entity());
        var bukkitFromLocation = SoakLocationMap.toBukkit(spongeFromLocation);
        var bukkitToLocation = SoakLocationMap.toBukkit(spongeToLocation);

        var bukkitEvent = new EntityPortalEvent(bukkitEntity, bukkitFromLocation, bukkitToLocation);
        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (bukkitEvent.getTo() != null && !bukkitEvent.getTo().equals(bukkitToLocation)) {
            spongeToLocation = SoakLocationMap.toSponge(bukkitEvent.getTo());
            event.setDestinationPosition(spongeToLocation.position());
        }
        if (bukkitEvent.isCancelled() && !bukkitEvent.getFrom().equals(bukkitFromLocation)) {
            spongeFromLocation = SoakLocationMap.toSponge(bukkitEvent.getFrom());
            event.setDestinationPosition(spongeFromLocation.position());
        } else if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }
}
