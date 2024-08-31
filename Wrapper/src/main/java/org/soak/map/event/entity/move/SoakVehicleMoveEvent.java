package org.soak.map.event.entity.move;

import org.bukkit.Location;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.entity.vehicle.Vehicle;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.math.vector.Vector3d;

public class SoakVehicleMoveEvent {

    private final EventSingleListenerWrapper<PlayerMoveEvent> singleEventListener;

    public SoakVehicleMoveEvent(EventSingleListenerWrapper<PlayerMoveEvent> listener) {
        this.singleEventListener = listener;
    }

    @Listener(order = Order.FIRST)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void firstEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void earlyEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void normalEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void lateEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void lastEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.LOWEST);
    }

    private void fireEvent(MoveEntityEvent event, EventPriority priority) {
        var entity = event.entity();
        if (!(entity instanceof Vehicle)) {
            return;
        }

        var bukkitVehicle = (org.bukkit.entity.Vehicle) AbstractEntity.wrap(entity);
        var spongeOriginalPosition = event.originalPosition();
        var newPositionWorld = bukkitVehicle.getWorld();
        var originalPosition = new Location(newPositionWorld,
                spongeOriginalPosition.x(),
                spongeOriginalPosition.y(),
                spongeOriginalPosition.z());
        var originalRotation = entity.rotation();
        originalPosition.setPitch((float) originalRotation.x());
        originalPosition.setYaw((float) originalRotation.y());

        var spongeNewPosition = event.destinationPosition();
        var newPosition = new Location(newPositionWorld,
                spongeNewPosition.x(),
                spongeNewPosition.y(),
                spongeNewPosition.z());
        newPosition.setPitch(originalPosition.getPitch());
        newPosition.setYaw(originalPosition.getYaw());

        var bukkitEvent = new VehicleMoveEvent(bukkitVehicle, originalPosition, newPosition);
        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);
        var to = bukkitEvent.getTo();
        event.setDestinationPosition(new Vector3d(to.getX(), to.getY(), to.getZ()));
        Vector3d newRotation = originalRotation;
        if (originalRotation.x() != to.getPitch()) {
            newRotation = new Vector3d(to.getPitch(), newRotation.y(), newRotation.z());
        }
        if (originalRotation.y() != to.getYaw()) {
            newRotation = new Vector3d(newRotation.x(), to.getYaw(), newRotation.z());
        }
        if (!newRotation.equals(originalRotation)) {
            entity.setRotation(newRotation);
        }

        //change .... getFrom????
        //guess of implementation ->
        //when the event is cancelled the player will teleport back to this specified position


        //but why???? surly setting to "to" position is easier than cancelling the event and setting "from"????
    }
}
