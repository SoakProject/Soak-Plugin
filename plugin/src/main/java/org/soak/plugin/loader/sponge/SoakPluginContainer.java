package org.soak.plugin.loader.sponge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.io.File;
import java.net.URI;
import java.util.Optional;

public class SoakPluginContainer implements PluginContainer {

    private final File bukkitPluginFile;
    private final SoakPluginMetadata pluginMetadata;
    //private final URLClassLoader loader;
    private final Logger logger;
    private final Plugin plugin;
    private final SoakMainPluginWrapper mainInstance;

    public SoakPluginContainer(File bukkitPluginFile, Plugin plugin) {
        this.bukkitPluginFile = bukkitPluginFile;
        this.plugin = plugin;
        this.logger = LogManager.getLogger(plugin.getName());
        this.pluginMetadata = SoakPluginMetadata.fromPlugin(plugin);

        //temp
        this.mainInstance = new SoakMainPluginWrapper(this);
    }

    public Plugin plugin() {
        return this.plugin;
    }

    @Override
    public PluginMetadata metadata() {
        return this.pluginMetadata;
    }

    @Override
    public Logger logger() {
        return this.logger;
    }

    @Override
    public SoakMainPluginWrapper instance() {
        return this.mainInstance;
    }

    @Override
    public Optional<URI> locateResource(URI relative) {
        boolean exists = this.plugin.getResource(relative.getPath()) != null;
        if (!exists) {
            return Optional.empty();
        }

        URI localPath = this.bukkitPluginFile.toURI();
        URI ret = localPath.relativize(relative);
        return Optional.of(ret);
    }
}
