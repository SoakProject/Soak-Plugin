package org.soak.map.event.block;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

public class SoakBlockBreakEvent {

    private final EventSingleListenerWrapper<BlockBreakEvent> singleEventListener;

    public SoakBlockBreakEvent(EventSingleListenerWrapper<BlockBreakEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(ChangeBlockEvent.All spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ChangeBlockEvent.All spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ChangeBlockEvent.All spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ChangeBlockEvent.All spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ChangeBlockEvent.All spongeEvent, @First ServerPlayer player) {
        fireEvent(spongeEvent, player, EventPriority.LOWEST);
    }

    private void fireEvent(ChangeBlockEvent.All spongeEvent, ServerPlayer spongePlayer, EventPriority priority) {
        var player = SoakManager.<WrapperManager>getManager().getMemoryStore().get(spongePlayer);
        spongeEvent.transactions(Operations.BREAK.get()).forEach(transaction -> {
            var block = new SoakBlockSnapshot(transaction.original());
            var bukkitEvent = new BlockBreakEvent(block, player);
            SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

            if (bukkitEvent.isCancelled()) {
                transaction.invalidate();
            }
            //TODO cancel drops
        });
    }
}
