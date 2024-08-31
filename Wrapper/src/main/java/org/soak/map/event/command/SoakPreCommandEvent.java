package org.soak.map.event.command;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;

public class SoakPreCommandEvent {

    private final EventSingleListenerWrapper<PlayerCommandPreprocessEvent> singleEventListener;

    public SoakPreCommandEvent(EventSingleListenerWrapper<PlayerCommandPreprocessEvent> singleEventListener) {
        this.singleEventListener = singleEventListener;
    }

    @Listener(order = Order.FIRST)
    public void firstEvent(ExecuteCommandEvent.Pre spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ExecuteCommandEvent.Pre spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ExecuteCommandEvent.Pre spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ExecuteCommandEvent.Pre spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ExecuteCommandEvent.Pre spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }


    private void fireEvent(ExecuteCommandEvent.Pre event, EventPriority priority) {
        var root = event.commandCause().root();
        if (!(root instanceof ServerPlayer)) {
            return;
        }
        var serverPlayer = (ServerPlayer) root;

        var command = event.command() + " " + event.arguments();
        var player = SoakManager.<WrapperManager>getManager().getMemoryStore().get(serverPlayer);

        var bukkitEvent = new PlayerCommandPreprocessEvent(player, command);
        SoakManager.<WrapperManager>getManager().getServer().getSoakPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);

        if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
        }

        var newCommand = bukkitEvent.getMessage();
        var newCommandSplit = newCommand.split(" ", 2);
        var newRoot = newCommandSplit[0];
        String newArguments = "";
        if (newCommandSplit.length == 2) {
            newArguments = newCommandSplit[1];
        }

        if (!command.equals(newCommand)) {
            event.setCommand(newRoot);
            event.setArguments(newArguments);
        }

        if (!bukkitEvent.getPlayer().equals(player)) {
            event.setCancelled(true);
            var newBukkitPlayer = (SoakPlayer) bukkitEvent.getPlayer();
            try {
                Sponge.server().commandManager().process(newBukkitPlayer.spongeEntity(), newArguments);
            } catch (CommandException e) {
                SoakManager.getManager().displayError(e, this.singleEventListener.plugin());
            }
        }
    }
}
