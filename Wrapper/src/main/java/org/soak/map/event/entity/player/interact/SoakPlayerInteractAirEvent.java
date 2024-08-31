package org.soak.map.event.entity.player.interact;

import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.map.SoakActionMap;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.type.Exclude;

public class SoakPlayerInteractAirEvent {

    private final EventSingleListenerWrapper<PlayerInteractEvent> singleEventListener;

    public SoakPlayerInteractAirEvent(EventSingleListenerWrapper<PlayerInteractEvent> singleEventListener) {
        this.singleEventListener = singleEventListener;
    }

    @Listener(order = Order.FIRST)
    @Exclude(InteractBlockEvent.class)
    public void firstEvent(InteractEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    @Exclude(InteractBlockEvent.class)
    public void earlyEvent(InteractEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    @Exclude(InteractBlockEvent.class)
    public void normalEvent(InteractEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    @Exclude(InteractBlockEvent.class)
    public void lateEvent(InteractEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    @Exclude(InteractBlockEvent.class)
    public void lastEvent(InteractEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOWEST);
    }

    private void fireEvent(InteractEvent spongeEvent, ServerPlayer spongePlayer, EventPriority priority) {
        var player = SoakManager.<WrapperManager>getManager().getMemoryStore().get(spongePlayer);
        var clickedFace = BlockFace.SELF;
        var spongeHand = spongeEvent.context()
                .get(EventContextKeys.USED_HAND)
                .orElseThrow(() -> new RuntimeException("Unknown hand type from event"));
        var action = SoakActionMap.toBukkit(spongeHand, true);
        var hand = SoakActionMap.toBukkit(spongeHand);
        var spongeItem = spongePlayer.itemInHand(spongeHand);
        var item = SoakItemStackMap.toBukkit(spongeItem);

        var bukkitEvent = new PlayerInteractEvent(player, action, item, null, clickedFace, hand);
        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (spongeEvent instanceof Cancellable) {
            if (bukkitEvent.useInteractedBlock() == Event.Result.DENY) {
                ((Cancellable)spongeEvent).setCancelled(true);
            }
        }

    }
}
