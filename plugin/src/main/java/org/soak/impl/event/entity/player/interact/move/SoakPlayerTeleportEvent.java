package org.soak.impl.event.entity.player.interact.move;

import org.bukkit.Location;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.math.vector.Vector3d;

public class SoakPlayerTeleportEvent {

    private final EventSingleListenerWrapper<PlayerTeleportEvent> singleEventListener;

    public SoakPlayerTeleportEvent(EventSingleListenerWrapper<PlayerTeleportEvent> listener) {
        this.singleEventListener = listener;
    }

    @Listener(order = Order.FIRST)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void firstEvent(MoveEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void earlyEvent(MoveEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void normalEvent(MoveEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void lateEvent(MoveEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void lastEvent(MoveEntityEvent event, @Getter("entity") ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOWEST);
    }

    private void fireEvent(MoveEntityEvent event, ServerPlayer spongePlayer, EventPriority priority) {
        var distance = event.originalPosition().distanceSquared(event.destinationPosition());
        var isFlying = spongePlayer.get(Keys.IS_ELYTRA_FLYING).orElse(false);
        if (distance <= (isFlying ? 300 : 100)) {
            //if too small then count as movement

            //TODO find a better way to do this
            //these values are found based on the "moved to quickly" message as of the open source minecraft community
            //https://wiki.vg/Protocol#Set_Player_Position
            return;
        }


        var bukkitPlayer = new SoakPlayer(spongePlayer);
        var spongeOriginalPosition = event.originalPosition();
        var newPositionWorld = bukkitPlayer.getWorld();
        var originalPosition = new Location(newPositionWorld,
                spongeOriginalPosition.x(),
                spongeOriginalPosition.y(),
                spongeOriginalPosition.z());
        var originalRotation = spongePlayer.rotation();
        originalPosition.setDirection(new Vector(originalRotation.x(), originalRotation.y(), originalRotation.z()));


        var spongeNewPosition = event.destinationPosition();
        if (event instanceof ChangeEntityWorldEvent.Reposition repoEvent) {
            newPositionWorld = new SoakWorld(repoEvent.destinationWorld());
        }
        var newPosition = new Location(newPositionWorld,
                spongeNewPosition.x(),
                spongeNewPosition.y(),
                spongeNewPosition.z());

        var bukkitEvent = new PlayerMoveEvent(bukkitPlayer, originalPosition, newPosition);
        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        var to = bukkitEvent.getTo();
        event.setDestinationPosition(new Vector3d(to.getX(), to.getY(), to.getZ()));

        //change .... getFrom????
        //guess of implementation ->
        //when the event is cancelled the player will teleport back to this specified position


        //but why???? surly setting to "to" position is easier than cancelling the event and setting "from"????
    }
}
