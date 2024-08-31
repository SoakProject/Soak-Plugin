package org.soak.hook.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.soak.WrapperManager;
import org.soak.map.SoakSubjectMap;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.service.permission.Subject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpongeDynamicCommand implements Command.Raw {

    private Map<String, org.bukkit.command.Command> commandInfo() {
        return SoakManager.<WrapperManager>getManager().getServer().getCommandMap().getKnownCommands();
    }

    @Override
    public CommandResult process(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
        var command = arguments.parseString();
        var argumentsFull = arguments.remaining();
        var argumentsSplit = argumentsFull.contains(" ") ? argumentsFull.split(" ") : new String[0];

        var opCommandInfo = commandInfo().entrySet().stream().filter(info -> info.getKey().equalsIgnoreCase(command)).findAny();
        if (opCommandInfo.isEmpty()) {
            return CommandResult.error(Component.text("Unknown command of " + command));
        }
        var commandSender = SoakSubjectMap.mapToBukkit((Subject) cause.root());
        var commandExecutor = opCommandInfo.get().getValue();
        if (!commandExecutor.testPermissionSilent(commandSender)) {
            return CommandResult.error(SoakManager.<WrapperManager>getManager().getServer().permissionMessage());
        }
        var commandResult = commandExecutor.execute(commandSender, command, argumentsSplit);
        return commandResult ? CommandResult.success() : CommandResult.error(Component.text(commandExecutor.getUsage()).color(NamedTextColor.RED));
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
        var command = arguments.parseString();
        var argumentsFull = arguments.remaining();
        var argumentsSplit = argumentsFull.contains(" ") ? argumentsFull.split(" ") : new String[0];

        var opCommandInfo = commandInfo().entrySet().stream().filter(info -> info.getKey().equalsIgnoreCase(command)).findAny();
        if (opCommandInfo.isEmpty()) {
            return commandInfo().keySet().stream().filter(key -> key.toLowerCase().startsWith(command.toLowerCase())).map(CommandCompletion::of).toList();
        }
        var commandSender = SoakSubjectMap.mapToBukkit((Subject) cause.root());
        var commandExecutor = opCommandInfo.get().getValue();

        if (!commandExecutor.testPermissionSilent(commandSender)) {
            return List.of();
        }
        var tabComplete = commandExecutor.tabComplete(commandSender, command, argumentsSplit);
        return tabComplete.stream().map(CommandCompletion::of).toList();
    }

    @Override
    public boolean canExecute(CommandCause cause) {
        var commandSource = SoakSubjectMap.mapToBukkit(cause.subject());
        return commandInfo().values().stream().allMatch(info -> info.testPermissionSilent(commandSource));
    }

    @Override
    public Optional<Component> shortDescription(CommandCause cause) {
        return Optional.of(Component.text("A command to launch fake commands"));
    }

    @Override
    public Optional<Component> extendedDescription(CommandCause cause) {
        return Optional.empty();
    }

    @Override
    public Component usage(CommandCause cause) {
        return Component.text("/soakdynamic <bukkit command>");
    }
}
