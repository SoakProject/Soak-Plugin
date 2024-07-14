package org.soak.wrapper.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.SoakPlugin;

import java.util.List;

public class SoakCommandMap extends SimpleCommandMap {

    public SoakCommandMap() {
        super(Bukkit.getServer());
    }

    @Override
    public void registerAll(@NotNull String fallbackPrefix, @NotNull List<Command> commands) {
        SoakPlugin.plugin().logger().warn("A plugin attempted to register a command dynamically. This hasn't been possible without hacking since MC 1.13. Ignoring register");
    }

    @Override
    public boolean register(@NotNull String fallbackPrefix, @NotNull Command command) {
        SoakPlugin.plugin().logger().warn("A plugin attempted to register a command dynamically. This hasn't been possible without hacking since MC 1.13. Ignoring register of CMD: '" + command.getName() + "', Fallback: '" + fallbackPrefix + "'");
        return false;
    }

    @Override
    public boolean register(@NotNull String label, @NotNull String fallbackPrefix, @NotNull Command command) {
        SoakPlugin.plugin().logger().warn("A plugin attempted to register a command dynamically. This hasn't been possible without hacking since MC 1.13. Ignoring register of CMD: '" + command.getName() + "', Fallback: '" + fallbackPrefix + "', Label: '" + label + "'");
        return false;
    }

    @Override
    public synchronized void clearCommands() {
        SoakPlugin.plugin().logger().warn("A plugin attempted to clear all commands");
    }
}
