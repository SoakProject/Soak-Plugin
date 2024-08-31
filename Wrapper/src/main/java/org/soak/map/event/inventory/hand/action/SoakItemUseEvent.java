package org.soak.map.event.inventory.hand.action;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;

public class SoakItemUseEvent {

    private final EventSingleListenerWrapper<PlayerItemConsumeEvent> singleEventListener;


    public SoakItemUseEvent(EventSingleListenerWrapper<PlayerItemConsumeEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(UseItemStackEvent.Finish spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(UseItemStackEvent.Finish spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(UseItemStackEvent.Finish spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(UseItemStackEvent.Finish spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(UseItemStackEvent.Finish spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.LOWEST);
    }

    private void fireEvent(UseItemStackEvent.Finish event, ServerPlayer spongePlayer, EventPriority priority) {
        var player = SoakManager.<WrapperManager>getManager().getMemoryStore().get(spongePlayer);
        var item = SoakItemStackMap.toBukkit(event.itemStackInUse());

        var bukkitEvent = new PlayerItemConsumeEvent(player, item);
        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
        }
        //TODO set item being consumed
        //TODO set item replacement (effects)
    }
}
