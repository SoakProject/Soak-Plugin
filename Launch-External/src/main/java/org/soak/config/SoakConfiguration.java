package org.soak.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.soak.config.node.BooleanConfigNode;
import org.soak.config.node.ComponentConfigNode;
import org.soak.config.node.ConfigNode;
import org.soak.config.node.FileConfigNode;
import org.soak.plugin.external.SoakConfig;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.util.Optional;

public class SoakConfiguration implements SoakConfig {

    public static final FileConfigNode PLUGIN_FOLDER = new FileConfigNode("path", "plugin");
    public static final FileConfigNode CONFIG_FOLDER = new FileConfigNode("path", "config");
    public static final ComponentConfigNode NO_PERMISSION_MESSAGE = new ComponentConfigNode("messages", "permission", "none");
    public static final BooleanConfigNode SHOW_DEBUG_LOG = new BooleanConfigNode("messages", "debug", "show");

    private final File file;
    private final HoconConfigurationLoader loader;
    private final ConfigurationNode node;

    public SoakConfiguration(File file) throws ConfigurateException {
        this.file = file;
        this.loader = HoconConfigurationLoader.builder().file(file).build();
        this.node = this.loader.load();
    }

    public File file() {
        return file;
    }

    public HoconConfigurationLoader loader() {
        return loader;
    }

    public ConfigurationNode root() {
        return this.node;
    }

    public boolean showDebugLog() {
        return parse(SHOW_DEBUG_LOG).orElse(true);
    }

    public File pluginFolder() {
        return parse(PLUGIN_FOLDER).orElseGet(() -> new File("mods/bukkit/plugin"));
    }


    public <T> Optional<T> parse(ConfigNode<T> node) {
        return node.parse(this.node);
    }

    public <T> void set(ConfigNode<T> node, T value) throws SerializationException {
        node.set(this.node, value);
    }

    public void save() throws ConfigurateException {
        this.loader.save(this.node);
    }

    @Override
    public File getConfigPath() {
        return parse(CONFIG_FOLDER).orElseGet(() -> new File("plugins"));
    }

    @Override
    public Component getNoPermissionMessage() {
        return parse(NO_PERMISSION_MESSAGE).orElseGet(() -> Component.text("You do not have permission to do that").color(NamedTextColor.RED));
    }
}
