package org.soak.wrapper.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SoakCommandMap extends SimpleCommandMap {


    public SoakCommandMap() {
        super(Bukkit.getServer());
    }

    @Override
    public boolean register(@NotNull String label, @NotNull String fallbackPrefix, @NotNull Command command) {
        return registerCommand(command, fallbackPrefix, label);
    }

    private boolean registerCommand(@NotNull Command command, @NotNull String fallback, @Nullable String label) {
        this.knownCommands.put(Objects.requireNonNullElseGet(label, command::getLabel), command);
        return command.register(this);
    }
}
