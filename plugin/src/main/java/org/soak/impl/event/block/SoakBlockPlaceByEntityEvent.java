package org.soak.impl.event.block;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

public class SoakBlockPlaceByEntityEvent {

    private final EventSingleListenerWrapper<BlockPlaceEvent> singleEventListener;

    public SoakBlockPlaceByEntityEvent(EventSingleListenerWrapper<BlockPlaceEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(ChangeBlockEvent.All spongeEvent, @First Entity player) {
        fireEvent(spongeEvent, player, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ChangeBlockEvent.All spongeEvent, @First Entity player) {
        fireEvent(spongeEvent, player, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ChangeBlockEvent.All spongeEvent, @First Entity player) {
        fireEvent(spongeEvent, player, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ChangeBlockEvent.All spongeEvent, @First Entity player) {
        fireEvent(spongeEvent, player, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ChangeBlockEvent.All spongeEvent, @First Entity player) {
        fireEvent(spongeEvent, player, EventPriority.LOWEST);
    }

    private void fireEvent(ChangeBlockEvent.All spongeEvent, Entity spongeEntity, EventPriority priority) {
        if (spongeEntity instanceof Player) {
            return;
        }
        var entity = AbstractEntity.wrap(spongeEntity);

        spongeEvent.transactions(Operations.PLACE.get()).forEach(transaction -> {
            var originalBlock = new SoakBlockSnapshot(transaction.original());
            var newBlock = new SoakBlockSnapshot(transaction.custom().orElseGet(transaction::finalReplacement));

            var bukkitEvent = new EntityBlockFormEvent(entity, originalBlock,
                    newBlock.getState());
            SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

            if (bukkitEvent.isCancelled()) {
                transaction.invalidate();
            }
        });
    }
}
