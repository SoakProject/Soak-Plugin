package org.soak.commands;

import org.soak.plugin.loader.sponge.SoakPluginContainer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiPredicate;

public class SoakArguments {

    public static Parameter.Value.Builder<SoakPluginContainer> soakPlugins() {
        return soakPlugins((context, pluginContainer) -> true);
    }

    public static Parameter.Value.Builder<SoakPluginContainer> soakPlugins(BiPredicate<CommandContext, SoakPluginContainer> predicate) {
        return Parameter.builder(SoakPluginContainer.class)
                .addParser((parameterKey, reader, context) -> {
                    String input = reader.parseString();
                    return Sponge.pluginManager().plugins().stream()
                            .filter(pc -> pc instanceof SoakPluginContainer)
                            .map(pc -> (SoakPluginContainer) pc)
                            .filter(spc -> spc.plugin().getName().equalsIgnoreCase(input))
                            .findAny()
                            .filter(spc -> predicate.test(context, spc));
                })
                .completer((context, currentInput) -> Sponge.pluginManager()
                        .plugins()
                        .stream()
                        .filter(pc -> pc instanceof SoakPluginContainer)
                        .map(pc -> (SoakPluginContainer) pc)
                        .filter(soakPluginContainer -> soakPluginContainer.plugin()
                                .getName()
                                .toLowerCase()
                                .startsWith(currentInput.toLowerCase()))
                        .filter(soakPluginContainer -> predicate.test(context, soakPluginContainer))
                        .map(spc -> CommandCompletion.of(spc.plugin().getName()))
                        .toList());
    }
}
