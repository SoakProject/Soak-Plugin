package org.soak.impl.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.PluginCommand;
import org.soak.map.SoakMessageMap;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.event.EventContextKeys;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BukkitRawCommand implements Command.Raw {

    private final PluginCommand command;


    public BukkitRawCommand(PluginCommand command) {
        this.command = command;
    }

    @Override
    public CommandResult process(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
        return null;
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
        if (this.command.getTabCompleter() == null) {
            return Collections.emptyList();
        }
        String command = cause.context().get(EventContextKeys.COMMAND).orElse("");
        String[] args = arguments.input().split(" ");

        List<String> commands = this.command.getTabCompleter().onTabComplete(null, this.command, command, args);
        if (commands == null) {
            return Collections.emptyList();
        }
        return commands.stream().map(CommandCompletion::of).collect(Collectors.toList());
    }

    @Override
    public boolean canExecute(CommandCause cause) {
        return true;
    }

    @Override
    public Optional<Component> shortDescription(CommandCause cause) {
        return extendedDescription(cause);
    }

    @Override
    public Optional<Component> extendedDescription(CommandCause cause) {
        return Optional.of(SoakMessageMap.mapToComponent(this.command.getDescription()));
    }

    @Override
    public Component usage(CommandCause cause) {
        return SoakMessageMap.mapToComponent(this.command.getUsage());
    }
}
