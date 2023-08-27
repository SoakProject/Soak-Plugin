package org.soak.impl.event.block.piston;

import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.map.SoakDirectionMap;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.entity.Piston;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.NotifyNeighborBlockEvent;

import java.util.stream.Collectors;

public class SoakPistonExtendEvent {

    private final EventSingleListenerWrapper<BlockPistonExtendEvent> singleEventListener;

    public SoakPistonExtendEvent(EventSingleListenerWrapper<BlockPistonExtendEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(NotifyNeighborBlockEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(NotifyNeighborBlockEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(NotifyNeighborBlockEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(NotifyNeighborBlockEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(NotifyNeighborBlockEvent spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }

    public void fireEvent(NotifyNeighborBlockEvent event, EventPriority priority) {
        var opPiston = event.cause().first(Piston.class);
        if (opPiston.isEmpty()) {
            return;
        }
        var piston = opPiston.get();
        //The piston here is the head position. When retracting this may show the block that retracted rather than the piston
        //the pistons body is stored in the piston. Ill contact Sponge for a none implementation specific way to get this
        BlockState state;
        try {
            var classType = Class.forName("net.minecraft.world.level.block.entity.BlockEntity");
            var blockStateField = classType.getDeclaredField("blockState");
            blockStateField.setAccessible(true);
            state = (BlockState) blockStateField.get(piston);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        var isExtended = piston.extending().get();
        if(!isExtended){
            return;
        }
        var pistonDirection = state.get(Keys.DIRECTION).orElseThrow(() -> new RuntimeException("Cannot get direction"));
        var pistonLocation = piston.serverLocation().relativeToBlock(pistonDirection.opposite());
        var pistonBody = BlockSnapshot.builder().blockState(state).world(pistonLocation.world().properties()).position(pistonLocation.blockPosition()).build();

        var bukkitPiston = new SoakBlockSnapshot(pistonBody);
        var bukkitDirection = SoakDirectionMap.toBukkit(pistonDirection);
        var movedBlocks = event.tickets().stream().map(ticket -> (Block) new SoakBlockSnapshot(ticket.target())).collect(Collectors.toList());

        var bukkitEvent = new BlockPistonExtendEvent(bukkitPiston, movedBlocks, bukkitDirection);
        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);
    }

}
