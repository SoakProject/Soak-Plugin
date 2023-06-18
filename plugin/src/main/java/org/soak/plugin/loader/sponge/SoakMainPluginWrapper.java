package org.soak.plugin.loader.sponge;

import org.bukkit.plugin.Plugin;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

public class SoakMainPluginWrapper {

    public static final String CMD_DESCRIPTION = "description";
    public static final String CMD_USAGE = "usage";
    private final SoakPluginContainer pluginContainer;

    public SoakMainPluginWrapper(SoakPluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

    public SoakPluginContainer container() {
        return this.pluginContainer;
    }

    @Listener
    public void onCommandRegister(RegisterCommandEvent<Command.Parameterized> event) {
        Plugin plugin = this.pluginContainer.plugin();
        plugin.getDescription().getCommands().forEach((cmdName, value) -> {
            String usage = (String) value.get(CMD_USAGE);
            String description = (String) value.get(CMD_DESCRIPTION);


        });
    }
}
