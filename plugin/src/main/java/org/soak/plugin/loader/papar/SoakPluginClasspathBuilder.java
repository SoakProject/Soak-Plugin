package org.soak.plugin.loader.papar;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.library.ClassPathLibrary;
import org.jetbrains.annotations.NotNull;

public class SoakPluginClasspathBuilder implements PluginClasspathBuilder {
    @Override
    public @NotNull PluginClasspathBuilder addLibrary(@NotNull ClassPathLibrary classPathLibrary) {
        return null;
    }

    @Override
    public @NotNull PluginProviderContext getContext() {
        return null;
    }
}
