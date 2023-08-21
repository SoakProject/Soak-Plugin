package org.soak.impl.event.entity.player.combat;

import org.bukkit.Location;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.math.vector.Vector3d;

public class SoakPlayerRespawnEvent {

    private final EventSingleListenerWrapper<PlayerRespawnEvent> singleListenerWrapper;

    public SoakPlayerRespawnEvent(EventSingleListenerWrapper<PlayerRespawnEvent> wrapper) {
        this.singleListenerWrapper = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(RespawnPlayerEvent.Recreate spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(RespawnPlayerEvent.Recreate spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(RespawnPlayerEvent.Recreate spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(RespawnPlayerEvent.Recreate spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(RespawnPlayerEvent.Recreate spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }


    private void fireEvent(RespawnPlayerEvent.Recreate event, EventPriority priority) {
        var player = SoakPlugin.plugin().getMemoryStore().get(event.entity());
        var newWorld = SoakPlugin.plugin().getMemoryStore().get(event.destinationWorld());
        var newLocation = new Location(newWorld,
                event.destinationPosition().x(),
                event.destinationPosition().y(),
                event.destinationPosition().z());

        var bukkitEvent = new PlayerRespawnEvent(player, newLocation, event.isBedSpawn());
        SoakPlugin.server().getPluginManager().callEvent(this.singleListenerWrapper, bukkitEvent, priority);

        var newSetLocation = bukkitEvent.getRespawnLocation();
        if (!newLocation.equals(newSetLocation)) {
            if (newLocation.getWorld().equals(newSetLocation.getWorld())) {
                event.setDestinationPosition(new Vector3d(newSetLocation.getX(),
                        newSetLocation.getY(),
                        newSetLocation.getZ()));
                return;
            }
            event.setCancelled(true);
            var spongeNewWorld = ((SoakWorld) newSetLocation.getWorld()).sponge();
            var spongeNewLocation = spongeNewWorld.location(newSetLocation.getX(),
                    newSetLocation.getY(),
                    newSetLocation.getZ());
            //this may need a schedule on it, depending on if its a death respawn
            event.entity().setLocation(spongeNewLocation);
        }
    }
}
