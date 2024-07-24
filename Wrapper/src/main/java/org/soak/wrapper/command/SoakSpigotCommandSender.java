package org.soak.wrapper.command;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SoakSpigotCommandSender extends CommandSender.Spigot implements SoakSpigotMessageSender {

    private final CommandSender sender;

    public SoakSpigotCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public CommandSender sender() {
        return this.sender;
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull BaseComponent component) {
        SoakSpigotMessageSender.super.sendMessage(component);
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull BaseComponent... components) {
        SoakSpigotMessageSender.super.sendMessage(components);
    }

    @Override
    @Deprecated
    public void sendMessage(@Nullable UUID sender, @NotNull BaseComponent component) {
        SoakSpigotMessageSender.super.sendMessage(sender, component);
    }

    @Override
    @Deprecated
    public void sendMessage(@Nullable UUID sender, @NotNull BaseComponent... components) {
        SoakSpigotMessageSender.super.sendMessage(sender, components);
    }
}
