package org.soak.impl.event.block;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.block.SoakBlock;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.entity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.First;

public class SoakSignChangeEvent {

    private final EventSingleListenerWrapper<SignChangeEvent> singleEventListener;

    public SoakSignChangeEvent(EventSingleListenerWrapper<SignChangeEvent> listener) {
        this.singleEventListener = listener;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(ChangeSignEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ChangeSignEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ChangeSignEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ChangeSignEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ChangeSignEvent event, @First ServerPlayer player) {
        fireEvent(event, player, EventPriority.LOWEST);
    }

    private void fireEvent(ChangeSignEvent event, ServerPlayer player, EventPriority priority) {
        var bukkitBlock = new SoakBlock(event.sign().serverLocation());
        var bukkitPlayer = new SoakPlayer(player);

        var bukkitEvent = new SignChangeEvent(bukkitBlock, bukkitPlayer, event.text().all());
        SoakPlugin.server().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        var newLines = bukkitEvent.lines();
        event.text().set(newLines);

    }
}
