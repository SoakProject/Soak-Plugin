package org.soak.map;

import org.bukkit.command.CommandSender;
import org.soak.wrapper.command.SoakCommandSender;
import org.soak.wrapper.command.SoakConsoleCommandSender;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;

public class SoakSubjectMap {

    public static Subject mapToSubject(CommandSender sender) {
        if (!(sender instanceof SoakCommandSender soakSender)) {
            throw new RuntimeException("Command sender is not SoakCommandSender");
        }
        return soakSender.getSubject();
    }

    public static CommandSender mapToBukkit(Subject sender) {
        if (sender instanceof SystemSubject system) {
            return mapToBukkit(system);
        }
        if (sender instanceof ServerPlayer player) {
            return new SoakPlayer(player);
        }
        throw new IllegalStateException("Unknown mapping for " + sender.getClass().getName());
    }

    public static SoakConsoleCommandSender mapToBukkit(SystemSubject sender) {
        return new SoakConsoleCommandSender(sender);
    }

}
