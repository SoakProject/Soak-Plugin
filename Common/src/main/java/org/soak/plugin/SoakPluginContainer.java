package org.soak.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.plugin.PluginContainer;

public interface SoakPluginContainer extends PluginContainer {

    @NotNull PluginContainer getTrueContainer();

    @NotNull JavaPlugin getBukkitInstance();

    @NotNull Object instance();
}
