package org.soak.wrapper.entity.living.human;

import net.kyori.adventure.identity.Identity;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakMessageMap;
import org.soak.wrapper.command.SoakSpigotMessageSender;

import java.net.InetSocketAddress;
import java.util.UUID;

public class SoakSpigotPlayer extends Player.Spigot implements SoakSpigotMessageSender {

    private final SoakPlayer player;

    public SoakSpigotPlayer(SoakPlayer player) {
        this.player = player;
    }

    @Override
    public CommandSender sender() {
        return this.player;
    }

    @Override
    public int getPing() {
        return player.getPing();
    }

    @Override
    public @NotNull InetSocketAddress getRawAddress() {
        return player.getAddress();
    }

    @Override
    public void respawn() {
        player.spongeEntity().respawn();
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull ChatMessageType position, @NotNull BaseComponent component) {
        this.player.spongeEntity()
                .sendMessage(SoakMessageMap.toComponent(component), SoakMessageMap.toComponent(position));
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull ChatMessageType position, @NotNull BaseComponent... components) {
        this.player.spongeEntity()
                .sendMessage(SoakMessageMap.toComponent(components), SoakMessageMap.toComponent(position));
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull ChatMessageType position, @Nullable UUID sender, @NotNull BaseComponent component) {
        if (sender == null) {
            sendMessage(position, component);
            return;
        }
        this.player.spongeEntity()
                .sendMessage(Identity.identity(sender),
                        SoakMessageMap.toComponent(component),
                        SoakMessageMap.toComponent(position));
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull ChatMessageType position, @Nullable UUID sender, @NotNull BaseComponent... components) {
        if (sender == null) {
            sendMessage(position, components);
            return;
        }
        this.player.spongeEntity()
                .sendMessage(Identity.identity(sender),
                        SoakMessageMap.toComponent(components),
                        SoakMessageMap.toComponent(position));
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
