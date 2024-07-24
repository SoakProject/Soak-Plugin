package org.soak.map.event.entity.player.interact.move;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.RotateEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.math.vector.Vector3d;

public class SoakPlayerRotateEvent {

    private final EventSingleListenerWrapper<PlayerMoveEvent> singleEventListener;

    public SoakPlayerRotateEvent(EventSingleListenerWrapper<PlayerMoveEvent> listener) {
        this.singleEventListener = listener;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(RotateEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(RotateEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(RotateEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(RotateEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(RotateEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOWEST);
    }

    private void fireEvent(RotateEntityEvent event, ServerPlayer spongePlayer, EventPriority priority) {
        var bukkitPlayer = SoakManager.<WrapperManager>getManager().getMemoryStore().get(spongePlayer);
        var originalPosition = bukkitPlayer.getLocation();
        var originalRotation = event.fromRotation();
        originalPosition.setPitch((float) originalRotation.x());
        originalPosition.setYaw((float) originalRotation.y());

        var newPosition = originalPosition.clone();
        var newRotation = event.fromRotation();
        newPosition.setPitch((float) newRotation.x());
        newPosition.setYaw((float) newRotation.y());


        var bukkitEvent = new PlayerMoveEvent(bukkitPlayer, originalPosition, newPosition);
        SoakManager.<WrapperManager>getManager().getServer().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        var to = bukkitEvent.getTo();
        var pitch = to.getPitch();
        var yaw = to.getYaw();
        var eventRotation = new Vector3d(pitch, yaw, newRotation.z());
        if (!eventRotation.equals(newRotation)) {
            event.setToRotation(new Vector3d(to.getPitch(), to.getYaw(), newRotation.z()));
        }

        //change .... getFrom????
        //guess of implementation ->
        //when the event is cancelled the player will teleport back to this specified position


        //but why???? surly setting to "to" position is easier than cancelling the event and setting "from"????
    }
}
