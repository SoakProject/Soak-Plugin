package org.soak.commands.soak;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.soak.plugin.loader.sponge.SoakPluginContainer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;

import java.util.Comparator;

public class SoakCommand {

    private static Command.Parameterized createPluginsCommand() {
        return Command
                .builder()
                .executor(context -> {
                    var plugins = Sponge
                            .pluginManager()
                            .plugins()
                            .stream()
                            .filter(pc -> pc instanceof SoakPluginContainer)
                            .map(pc -> (SoakPluginContainer) pc)
                            .map(SoakPluginContainer::plugin)
                            .sorted(Comparator.comparing(plugin -> ((JavaPlugin) plugin).isEnabled()).thenComparing(plugin -> ((JavaPlugin) plugin).getName()))
                            .map(plugin -> Component.text(plugin.getName()).color(plugin.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED))
                            .toList();

                    Component pluginsLine = Component.join(JoinConfiguration.separator(Component.text(",    ")), plugins);

                    context.cause().sendMessage(Identity.nil(), Component
                            .text("(" + plugins.size() + ")")
                            .color(TextColor.color(25, 255, 25))
                            .append(Component.text(" Bukkit plugins found").color(NamedTextColor.WHITE)));
                    context.cause().sendMessage(Identity.nil(), pluginsLine);
                    return CommandResult.success();
                })
                .build();
    }

    public static Command.Parameterized createSoakCommand() {
        return Command.builder().addChild(createPluginsCommand(), "plugins", "pl").build();
    }
}
