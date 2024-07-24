package org.soak.map.event.block;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.soak.wrapper.block.SoakBlock;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.util.Direction;

import java.util.stream.Stream;

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
        spongeEvent.transactions(Operations.LIQUID_SPREAD.get()).forEach(transaction -> { //used modify .... may not be correct
            var updatingBlock = transaction.custom().orElseGet(transaction::finalReplacement);
            var newLevel = updatingBlock.getInt(Keys.FLUID_LEVEL).orElseThrow(() -> new IllegalStateException("Fluid update happened but no fluid level could be found"));
            if(newLevel >= 8){ //max level
                return;
            }
            var spongeSourceBlock = Stream.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP)
                    .map(direction -> updatingBlock.location().orElseThrow().relativeToBlock(direction))
                    .filter(loc -> loc.get(Keys.FLUID_LEVEL)
                            .map(level -> level > newLevel)
                            .orElse(false))
                    .findAny().orElseThrow(() -> new IllegalStateException("Cannot find the fluid source of the updating block"));

            var newBlock = new SoakBlockSnapshot(updatingBlock);
            var sourceBlock = new SoakBlock(spongeSourceBlock);
            var bukkitEvent = new BlockFromToEvent(sourceBlock, newBlock); //todo get source block
            SoakManager.<WrapperManager>getManager().getServer().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

            if (bukkitEvent.isCancelled()) {
                transaction.invalidate();
            }
        });
    }
}

