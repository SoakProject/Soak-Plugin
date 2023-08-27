package org.soak.impl.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.map.SoakDirectionMap;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.block.SoakBlock;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.spongepowered.api.block.entity.Piston;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.NotifyNeighborBlockEvent;

import java.util.stream.Collectors;

public class SoakBlockFlowExpandEvent {

    private final EventSingleListenerWrapper<BlockBreakEvent> singleEventListener;

    public SoakBlockFlowExpandEvent(EventSingleListenerWrapper<BlockBreakEvent> wrapper) {
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
        Block source = null;
        spongeEvent.transactions(Operations.LIQUID_SPREAD.get()).forEach(transaction -> { //used modify .... may not be correct
            var newBlock = new SoakBlockSnapshot(transaction.custom().orElseGet(transaction::finalReplacement));
            var bukkitEvent = new BlockFromToEvent(source, newBlock); //todo get source block
            SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

            if (bukkitEvent.isCancelled()) {
                transaction.invalidate();
            }
        });
    }
}

