package org.soak.plugin.loader.papar.loader;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.loader.papar.SoakPluginProviderContext;
import org.soak.plugin.loader.papar.meta.SoakPluginMeta;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class SoakPluginClassLoader extends URLClassLoader implements ConfiguredPluginClassLoader {

    private final SoakPluginProviderContext context;

    public SoakPluginClassLoader(SoakPluginProviderContext context) throws MalformedURLException {
        super(new URL[]{context.getPluginSource().toUri().toURL()}, SoakPluginClassLoader.class.getClassLoader());
        this.context = context;
    }


    @Override
    public PluginMeta getConfiguration() {
        return this.context.getConfiguration();
    }

    @Override
    public Class<?> loadClass(@NotNull String name, boolean resolve, boolean checkGlobal, boolean checkLibs) throws ClassNotFoundException {

        //TODO GLOBAL AND LIBS
        return loadClass(name, resolve);
    }

    @Override
    public void init(JavaPlugin javaPlugin) {
        var logger = PaperPluginLogger.getLogger(this.getConfiguration().getName());
        logger.setUseParentHandlers(false);
        logger.addHandler(SoakPlugin.plugin().getConsole());
        try {
            File configFile = new File(this.context.getDataDirectory().toFile(), "config.yml");
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
            applyValue("isEnabled", javaPlugin, true);
            //applyValue("loader", javaPlugin, this);
            applyValue("server", javaPlugin, Bukkit.getServer());
            applyValue("file", javaPlugin, this.context.getPluginSource().toFile());
            if (this.getConfiguration() instanceof PluginDescriptionFile pluginDescriptionFile) {
                applyValue("description", javaPlugin, pluginDescriptionFile);
            } else if (this.getConfiguration() instanceof SoakPluginMeta meta) {
                applyValue("description", javaPlugin, meta.toDescription());
            } else {
                throw new RuntimeException("Unknown meta class: " + this.getConfiguration().getClass().getName());
            }
            applyValue("pluginMeta", javaPlugin, this.getConfiguration());
            applyValue("dataFolder", javaPlugin, this.context.getDataDirectory().toFile());
            applyValue("classLoader", javaPlugin, this);
            applyValue("newConfig", javaPlugin, configuration);
            applyValue("configFile", javaPlugin, configFile);
            applyValue("logger", javaPlugin, logger);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyValue(String fieldName, JavaPlugin plugin, Object value) throws NoSuchFieldException, IllegalAccessException {
        var field = JavaPlugin.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(plugin, value);
        field.setAccessible(false);
    }

    @Override
    public @Nullable JavaPlugin getPlugin() {
        return SoakPlugin.server().getPluginManager().getPlugin(this.getConfiguration());
    }

    @Override
    public @Nullable PluginClassLoaderGroup getGroup() {
        return null;
    }
}
