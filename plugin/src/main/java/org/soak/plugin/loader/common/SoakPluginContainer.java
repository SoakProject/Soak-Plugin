package org.soak.plugin.loader.common;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.plugin.PluginContainer;

public interface SoakPluginContainer extends PluginContainer {

    @NotNull JavaPlugin plugin();

    @Override
    @NotNull SoakPluginWrapper instance();
}
