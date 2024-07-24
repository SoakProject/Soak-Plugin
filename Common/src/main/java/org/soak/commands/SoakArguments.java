package org.soak.commands;

import org.soak.plugin.SoakManager;
import org.soak.plugin.SoakPluginContainer;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class SoakArguments {

    public static Parameter.Value.Builder<SoakPluginContainer> soakPlugins() {
        return soakPlugins((context, pluginContainer) -> true);
    }

    public static Parameter.Value.Builder<SoakPluginContainer> soakPlugins(BiPredicate<CommandContext, SoakPluginContainer> predicate) {
        return Parameter.builder(SoakPluginContainer.class)
                .addParser((parameterKey, reader, context) -> {
                    String input = reader.parseString();
                    return SoakManager.getManager().getBukkitContainers()
                            .filter(spc -> spc.getBukkitInstance().getName().equalsIgnoreCase(input))
                            .findAny()
                            .filter(spc -> predicate.test(context, spc));
                })
                .completer((context, currentInput) -> SoakManager.getManager().getBukkitContainers()
                        .filter(soakPluginContainer -> soakPluginContainer.getBukkitInstance()
                                .getName()
                                .toLowerCase()
                                .startsWith(currentInput.toLowerCase()))
                        .filter(soakPluginContainer -> predicate.test(context, soakPluginContainer))
                        .map(spc -> CommandCompletion.of(spc.getBukkitInstance().getName()))
                        .collect(Collectors.toList()));
    }
}
