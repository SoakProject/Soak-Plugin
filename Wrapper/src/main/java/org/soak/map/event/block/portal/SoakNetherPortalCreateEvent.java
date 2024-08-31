package org.soak.map.event.block.portal;

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
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.item.ItemTypes;

public class SoakNetherPortalCreateEvent {

    private final EventSingleListenerWrapper<PortalCreateEvent> singleEventListener;

    public SoakNetherPortalCreateEvent(EventSingleListenerWrapper<PortalCreateEvent> wrapper) {
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
        var blocks = spongeEvent
                .transactions(Operations.PLACE.get())
                .filter(transaction -> transaction.finalReplacement().state().type().equals(BlockTypes.NETHER_PORTAL.get()))
                .map(Transaction::finalReplacement)
                .map(blockSnapshot -> new SoakBlockSnapshot(blockSnapshot).getState())
                .toList();
        if(blocks.isEmpty()){
            return;
        }
        var world = blocks.get(0).getWorld();
        var cause = spongeEvent
                .context()
                .get(EventContextKeys.USED_ITEM)
                .filter(item ->
                        item.type().equals(ItemTypes.FLINT_AND_STEEL.get()) ||
                                item.type().equals(ItemTypes.FIRE_CHARGE.get()))
                .map(snapshot -> PortalCreateEvent.CreateReason.FIRE)
                .orElse(PortalCreateEvent.CreateReason.NETHER_PAIR);

        var event = new PortalCreateEvent(blocks, world, entity, cause);
        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleEventListener, event, priority);

        if (event.isCancelled()) {
            spongeEvent.setCancelled(true);
        }

    }
}
