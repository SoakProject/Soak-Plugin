package org.soak.map.event.block.portal;

import org.bukkit.block.BlockState;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.PortalCreateEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;

import java.util.List;

public class SoakEndPortalCreateEvent {

    private final EventSingleListenerWrapper<PortalCreateEvent> singleEventListener;

    public SoakEndPortalCreateEvent(EventSingleListenerWrapper<PortalCreateEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(ChangeBlockEvent.All spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ChangeBlockEvent.All spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ChangeBlockEvent.All spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ChangeBlockEvent.All spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ChangeBlockEvent.All spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }

    private void fireEvent(ChangeBlockEvent.All spongeEvent, EventPriority priority) {
        var entity = spongeEvent.cause().first(Entity.class).map(AbstractEntity::wrap).orElse(null);
        List<BlockState> blocks = spongeEvent
                .transactions(Operations.PLACE.get())
                .filter(transaction -> transaction.finalReplacement().state().type().equals(BlockTypes.END_PORTAL.get()))
                .map(Transaction::finalReplacement)
                .map(SoakBlockSnapshot::new)
                .map(soak -> soak.getState())
                .toList();
        if (blocks.isEmpty()) {
            return;
        }
        var world = blocks.get(0).getWorld();

        var event = new PortalCreateEvent(blocks, world, entity, PortalCreateEvent.CreateReason.END_PLATFORM);
        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleEventListener, event, priority);

        if (event.isCancelled()) {
            spongeEvent.setCancelled(true);
        }

    }
}
