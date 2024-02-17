package org.soak.plugin.loader.papar;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.mosestream.MoseStream;
import org.soak.plugin.loader.papar.loader.SoakPluginClassLoader;
import org.soak.plugin.loader.papar.meta.SoakPluginMetaBuilder;
import org.soak.plugin.loader.papar.yaml.AbstractPluginYamlValue;
import org.soak.plugin.loader.papar.yaml.PluginYamlValues;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class SoakPluginProviderContext implements PluginProviderContext {

    private File pluginFile;
    private PluginMeta descriptionFile;
    private ComponentLogger logger;
    private SoakPluginClassLoader loader;

    public SoakPluginProviderContext(File pluginFile) {
        this.pluginFile = pluginFile;
    }

    public void init() throws IOException, InvalidDescriptionException {
        JarFile file = new JarFile(this.pluginFile);
        var entry = file.getEntry("paper-plugin.yml");
        if (entry != null) {
            readPaper(file, entry);
            init2();
            return;
        }
        entry = file.getEntry("plugin.yml");
        var inputStream = file.getInputStream(entry);
        this.descriptionFile = new PluginDescriptionFile(inputStream);
        init2();
    }

    private void init2() throws MalformedURLException {
        this.logger = ComponentLogger.logger(this.descriptionFile.getName());
        this.loader = new SoakPluginClassLoader(this);
    }

    public Class<?> mainClass() throws ClassNotFoundException {
        return this.loader.loadClass(this.descriptionFile.getMainClass());
    }

    private void readPaper(JarFile file, ZipEntry entry) throws IOException {
        var inputStream = file.getInputStream(entry);
        var br = new BufferedReader(new InputStreamReader(inputStream));
        var yamlString = br.lines().collect(Collectors.joining("\n"));
        var yaml = YamlConfigurationLoader.builder().buildAndLoadString(yamlString);

        var pluginName = PluginYamlValues.NAME.value(yaml);
        var pluginVersion = PluginYamlValues.VERSION.value(yaml);
        var pluginEntrypoint = PluginYamlValues.MAIN.value(yaml);

        var permissionsNode = yaml
                .node("permissions")
                .childrenList();
        var permissions = MoseStream
                .stream(permissionsNode)
                .map(node -> new AbstractPluginYamlValue
                        .PermissionPluginYamlValue()
                        .value(node))
                .toList();

        this.descriptionFile = new SoakPluginMetaBuilder()
                .setName(pluginName)
                .setVersion(pluginVersion)
                .setMain(pluginEntrypoint)
                .setPermissions(permissions)
                .build();

    }


    @Override
    public @NotNull PluginMeta getConfiguration() {
        return this.descriptionFile;
    }

    @Override
    public @NotNull Path getDataDirectory() {
        return new File("config/" + this.descriptionFile.getName().toLowerCase() + "/").toPath();
    }

    @Override
    public @NotNull ComponentLogger getLogger() {
        return this.logger;
    }

    @Override
    public @NotNull Path getPluginSource() {
        return this.pluginFile.toPath();
    }
}
