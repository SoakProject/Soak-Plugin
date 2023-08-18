package org.bukkit.craftbukkit;

import net.minecraft.server.level.WorldServer;

public interface CraftWorld {

    WorldServer getHandle();
}
