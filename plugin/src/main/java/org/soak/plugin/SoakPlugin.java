package org.soak.plugin;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.soak.plugin.config.SoakConfiguration;
import org.soak.plugin.loader.Locator;
import org.soak.plugin.loader.sponge.SoakPluginContainer;
import org.soak.plugin.loader.sponge.injector.SoakPluginInjector;
import org.soak.wrapper.SoakServer;
import org.soak.wrapper.plugin.SoakPluginManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.plugin.PluginContainer;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

@org.spongepowered.plugin.builtin.jvm.Plugin("soak")
public class SoakPlugin {

    private static SoakPlugin plugin;

    private final SoakConfiguration configuration;
    private final PluginContainer container;
    private final Logger logger;

    @Inject
    public SoakPlugin(PluginContainer pluginContainer, Logger logger) {
        plugin = this;
        this.container = pluginContainer;
        this.logger = logger;
        try {
            Path path = Sponge.configManager().pluginConfig(this.container).directory();
            this.configuration = new SoakConfiguration(path.toFile());
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public static SoakPlugin plugin() {
        return plugin;
    }

    @Listener
    public void construct(ConstructPluginEvent event) {
        SoakServer server = new SoakServer(Sponge::server);
        SoakPluginManager pluginManager = server.getPluginManager();
        //noinspection deprecation
        JavaPluginLoader loader = new JavaPluginLoader(server);
        pluginManager.registerLoader(loader);
        Bukkit.setServer(server);

        Collection<File> files = Locator.files();
        for (File file : files) {
            Plugin plugin;
            try {
                //noinspection deprecation
                plugin = pluginManager.loadPlugin(file);
            } catch (Throwable e) {
                displayError(e, file);
                continue;
            }
            if (plugin == null) {
                this.logger.error("Failed to load '" + file.getName() + "'. Unknown error");
                continue;
            }

            SoakPluginContainer container = new SoakPluginContainer(file, plugin);
            SoakPluginInjector.injectPlugin(container);
            Sponge.eventManager().registerListeners(container, container.instance());
        }
    }

    public void displayError(Throwable e, File pluginFile) {
        displayError(e, Map.of("plugin file", pluginFile.getPath()));
    }

    private void displayError(Throwable e, Map<String, String> pluginData) {
        this.logger.error("|------------------------|");
        pluginData.forEach((key, value) -> {
            this.logger.error("|- " + key + ": " + value);
        });

        if (e instanceof ClassCastException castException) {
            if (castException.getMessage().contains("org.bukkit.plugin.SimplePluginManager")) {
                this.logger.error("|- Common Error Note: Starting on Paper hardfork 1.19.4, SimplePluginManager is being disconnected. This will not be added to soak");
            }
        }

        this.logger.error("|------------------------|");
        e.printStackTrace();
    }

    public PluginContainer container() {
        return this.container;
    }

    public SoakConfiguration config() {
        return this.configuration;
    }

    public Logger logger() {
        return this.logger;
    }

}
