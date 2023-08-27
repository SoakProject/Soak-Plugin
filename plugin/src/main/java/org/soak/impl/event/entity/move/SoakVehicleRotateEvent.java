package org.soak.impl.event.entity.move;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.entity.vehicle.Vehicle;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.RotateEntityEvent;
import org.spongepowered.math.vector.Vector3d;

public class SoakVehicleRotateEvent {

    private final EventSingleListenerWrapper<PlayerMoveEvent> singleEventListener;

    public SoakVehicleRotateEvent(EventSingleListenerWrapper<PlayerMoveEvent> listener) {
        this.singleEventListener = listener;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(RotateEntityEvent event) {
        fireEvent(event, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(RotateEntityEvent event) {
        fireEvent(event, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(RotateEntityEvent event) {
        fireEvent(event, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(RotateEntityEvent event) {
        fireEvent(event, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(RotateEntityEvent event) {
        fireEvent(event, EventPriority.LOWEST);
    }

    private void fireEvent(RotateEntityEvent event, EventPriority priority) {
        var entity = event.entity();
        if (!(entity instanceof Vehicle)) {
            return;
        }
        var bukkitPlayer = (org.bukkit.entity.Vehicle) AbstractEntity.wrap(entity);
        var originalPosition = bukkitPlayer.getLocation();
        var originalRotation = event.fromRotation();
        originalPosition.setPitch((float) originalRotation.x());
        originalPosition.setYaw((float) originalRotation.y());

        var newPosition = originalPosition.clone();
        var newRotation = event.fromRotation();
        newPosition.setPitch((float) newRotation.x());
        newPosition.setYaw((float) newRotation.y());


        var bukkitEvent = new VehicleMoveEvent(bukkitPlayer, originalPosition, newPosition);
        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        var to = bukkitEvent.getTo();
        var pitch = to.getPitch();
        var yaw = to.getYaw();
        var eventRotation = new Vector3d(pitch, yaw, newRotation.z());
        if (!eventRotation.equals(newRotation)) {
            event.setToRotation(new Vector3d(to.getPitch(), to.getYaw(), newRotation.z()));
        }
    }
}
