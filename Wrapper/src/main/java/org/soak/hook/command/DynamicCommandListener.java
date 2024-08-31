package org.soak.hook.command;

import org.soak.WrapperManager;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.ExecuteCommandEvent;

public class DynamicCommandListener {

    @Listener
    public void runCommand(ExecuteCommandEvent.Pre event) {
        String command = event.command();
        var knownCommands = SoakManager.<WrapperManager>getManager().getServer().getCommandMap().getKnownCommands();
        var isKnownCommand = knownCommands.entrySet().stream().anyMatch(entry -> entry.getKey().equalsIgnoreCase(command));
        if (!isKnownCommand) {
            return;
        }
        event.setCommand("soakdynamic");
        event.setArguments(command + " " + event.arguments());
    }
}
