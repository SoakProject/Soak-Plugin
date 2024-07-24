package org.soak.plugin.paper.yaml;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;

public interface PluginYamlValue<V> {

    @NotNull Object[] name();

    @NotNull V value(@NotNull ConfigurationNode node) throws ConfigurateException;

}
