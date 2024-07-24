package org.soak.plugin.paper.loader;

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
import org.soak.plugin.SoakManager;
import org.soak.plugin.paper.SoakPluginProviderContext;
import org.soak.plugin.paper.meta.SoakPluginMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipFile;

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
        logger.addHandler(SoakManager.getManager().getConsole());
        try {
            File pluginFile = this.context.getPluginSource().toFile();
            File configFile = new File(this.context.getDataDirectory().toFile(), "config.yml");
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
            try {
                copyYaml(configuration, pluginFile);
            } catch (IOException e) {
                //no default config
            }
            applyValue("isEnabled", javaPlugin, true);
            //applyValue("loader", javaPlugin, this);
            applyValue("server", javaPlugin, Bukkit.getServer());
            applyValue("file", javaPlugin, pluginFile);
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
        var config = this.getConfiguration();
        return SoakManager
                .getManager()
                .getBukkitContainers()
                .map(pc -> pc.getBukkitInstance())
                .filter(jp -> jp.getPluginMeta().equals(config))
                .findAny()
                .orElse(null);
    }

    private void copyYaml(YamlConfiguration configuration, File file) throws IOException {
        ZipFile zip = new ZipFile(file);
        var configEntry = zip.getEntry("config.yml");
        if (configEntry == null) {
            return;
        }
        var configIS = zip.getInputStream(configEntry);
        var isReader = new InputStreamReader(configIS);

        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(isReader);
        isReader.close();
        configIS.close();
        zip.close();
        configuration.addDefaults(defaultConfig);
    }

    @Override
    public @Nullable PluginClassLoaderGroup getGroup() {
        return null;
    }
}
