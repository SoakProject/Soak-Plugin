package org.soak.map;

import org.bukkit.command.CommandSender;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.command.SoakCommandSender;
import org.soak.wrapper.command.SoakConsoleCommandSender;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;

public class SoakSubjectMap {

    public static Subject mapToSubject(CommandSender sender) {
        if (!(sender instanceof SoakCommandSender)) {
            throw new RuntimeException("Command sender is not SoakCommandSender");
        }
        return ((SoakCommandSender) sender).getSubject();
    }

    public static CommandSender mapToBukkit(Subject sender) {
        if (sender instanceof SystemSubject) {
            return mapToBukkit((SystemSubject) sender);
        }
        if (sender instanceof ServerPlayer) {
            return SoakPlugin.plugin().getMemoryStore().get((ServerPlayer) sender);
        }
        throw new IllegalStateException("Unknown mapping for " + sender.getClass().getName());
    }

    public static SoakConsoleCommandSender mapToBukkit(SystemSubject sender) {
        return new SoakConsoleCommandSender(sender);
    }

}
