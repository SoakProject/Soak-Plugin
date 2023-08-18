package org.soak.impl.event.block;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.map.SoakActionMap;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.block.SoakBlock;
import org.soak.wrapper.block.SoakBlockSnapshot;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.spongepowered.api.block.transaction.Operations;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.util.blockray.RayTrace;

public class SoakBlockPlaceEvent {

    private final EventSingleListenerWrapper<BlockPlaceEvent> singleEventListener;

    public SoakBlockPlaceEvent(EventSingleListenerWrapper<BlockPlaceEvent> wrapper) {
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
        var player = new SoakPlayer(spongePlayer);
        var opRaytrace = RayTrace.block()
                .direction(spongePlayer)
                .sourceEyePosition(spongePlayer)
                .continueWhileBlock(RayTrace.onlyAir())
                .limit(7)
                .execute();
        if (opRaytrace.isEmpty()) {
            return;
        }
        var placedAgainst = new SoakBlock(opRaytrace.get().selectedObject().serverLocation());

        spongeEvent.transactions(Operations.PLACE.get()).forEach(transaction -> {
            var originalBlock = new SoakBlockSnapshot(transaction.original());
            var newBlock = new SoakBlockSnapshot(transaction.custom().orElseGet(transaction::finalReplacement));
            var usedItem = spongeEvent.context()
                    .get(EventContextKeys.USED_ITEM)
                    .map(SoakItemStackMap::toBukkit)
                    .orElseThrow(() -> new RuntimeException("Cannot get the used item to place the block"));
            var canBuild = true; //always true?
            var usedHand = spongeEvent.context()
                    .get(EventContextKeys.USED_HAND)
                    .map(SoakActionMap::toBukkit)
                    .orElseThrow(() -> new RuntimeException("Cannot get the used hand to place the block"));

            var bukkitEvent = new BlockPlaceEvent(originalBlock,
                    newBlock.getState(),
                    placedAgainst,
                    usedItem,
                    player,
                    canBuild,
                    usedHand);
            SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

            if (bukkitEvent.isCancelled()) {
                transaction.invalidate();
            }
            //TODO change can build?
        });
    }
}
