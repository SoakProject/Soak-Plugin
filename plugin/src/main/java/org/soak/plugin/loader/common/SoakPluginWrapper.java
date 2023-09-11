package org.soak.plugin.loader.common;

import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.Plugin;
import org.soak.impl.command.BukkitRawCommand;
import org.soak.plugin.SoakPlugin;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;

import java.util.Collection;
import java.util.LinkedHashSet;

public class SoakPluginWrapper {

    public static final String CMD_DESCRIPTION = "description";
    public static final String CMD_USAGE = "usage";
    private final SoakPluginContainer pluginContainer;
    private final Collection<org.bukkit.command.Command> commands = new LinkedHashSet<>();

    public SoakPluginWrapper(SoakPluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

    public org.soak.plugin.loader.common.SoakPluginContainer container() {
        return this.pluginContainer;
    }

    public Collection<org.bukkit.command.Command> commands() {
        return this.commands;
    }

    @Listener
    public void onCommandRegister(RegisterCommandEvent<Command.Raw> event) {
        Plugin plugin = this.pluginContainer.plugin();
        this.commands.addAll(PluginCommandYamlParser.parse(plugin));
        this.commands.forEach(cmd -> {
            event.register(this.pluginContainer,
                    new BukkitRawCommand(this.pluginContainer, cmd),
                    cmd.getName(),
                    cmd.getAliases().toArray(String[]::new));
        });
    }

    @Listener
    public void onPluginLoad(StartingEngineEvent<Server> event) {
        Plugin plugin = this.pluginContainer.plugin();
        try {
            plugin.onLoad();
        } catch (Throwable e) {
            SoakPlugin.plugin().displayError(e, plugin);
        }
    }


    //issue
    //Bukkit plugins assume everything is loaded when onEnable is run, this is because Craftbukkit loads everything before onEnable is used ....
    //using StartedEngineEvent despite the timing known to be incorrect
    @Listener
    public void onPluginEnable(StartedEngineEvent<Server> event) {
        Plugin plugin = this.pluginContainer.plugin();
        try {
            plugin.getPluginLoader().enablePlugin(plugin);
        } catch (Throwable e) {
            SoakPlugin.plugin().displayError(e, plugin);
        }
    }

    @Listener
    public void onPluginDisable(StoppingEngineEvent<Server> event) {
        Plugin plugin = this.pluginContainer.plugin();
        try {
            plugin.getPluginLoader().disablePlugin(plugin, true);
        } catch (Throwable e) {
            SoakPlugin.plugin().displayError(e, plugin);
        }
    }
}