package org.soak.plugin.loader.common;

import org.bukkit.plugin.Plugin;
import org.spongepowered.plugin.PluginContainer;

public interface SoakPluginContainer extends PluginContainer {

    Plugin plugin();

    @Override
    SoakPluginWrapper instance();
}
