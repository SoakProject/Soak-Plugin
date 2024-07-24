package org.soak.wrapper.command;

import net.kyori.adventure.identity.Identity;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakMessageMap;

import java.util.UUID;

public interface SoakSpigotMessageSender {

    CommandSender sender();

    @Deprecated
    default void sendMessage(@NotNull BaseComponent component) {
        sender().sendMessage(SoakMessageMap.toComponent(component));
    }

    @Deprecated
    default void sendMessage(@NotNull BaseComponent... components) {
        sender().sendMessage(SoakMessageMap.toComponent(components));
    }

    @Deprecated
    default void sendMessage(@Nullable UUID sender, @NotNull BaseComponent component) {
        if (sender == null) {
            sendMessage(component);
            return;
        }
        sender().sendMessage(Identity.identity(sender), SoakMessageMap.toComponent(component));
    }

    @Deprecated
    default void sendMessage(@Nullable UUID sender, @NotNull BaseComponent... components) {
        if (sender == null) {
            sendMessage(components);
            return;
        }
        sender().sendMessage(Identity.identity(sender), SoakMessageMap.toComponent(components));
    }
}
