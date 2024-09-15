package org.soak.plugin;

import io.papermc.paper.plugin.configuration.PluginMeta;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.soak.Compatibility;
import org.soak.WrapperManager;
import org.soak.io.SoakServerProperties;
import org.soak.plugin.paper.loader.SoakPluginClassLoader;
import org.soak.plugin.paper.meta.SoakPluginMetaBuilder;
import org.soak.utils.Singleton;
import org.soak.utils.SoakMemoryStore;
import org.soak.wrapper.v1_19_R4.NMSBounceSoakServer;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.PluginResource;
import org.spongepowered.plugin.builtin.jvm.JVMPluginContainer;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.stream.Stream;

public class AbstractSpongePluginMain implements SoakInternalManager, WrapperManager {

    private static final ArtifactVersion soakVersion = new DefaultArtifactVersion("0.0.1");
    final Collection<Command> commands = new LinkedTransferQueue<>();
    final InternalSoakPluginContainer soakPluginContainer = new InternalSoakPluginContainer(this);
    final PluginContainer container;
    private final Singleton<JavaPlugin> pluginCreator;
    private final Logger logger;
    private final Compatibility compatibility;
    private final SoakServerProperties serverProperties = new SoakServerProperties();
    private final ConsoleHandler consoleHandler = new ConsoleHandler();
    private final SoakMemoryStore memoryStore = new SoakMemoryStore();

    @Nullable
    private FileSystem fileSystem;

    public AbstractSpongePluginMain(Supplier<JavaPlugin> plugin, Logger logger, PluginContainer container) {
        this.pluginCreator = new Singleton<>(plugin);
        this.logger = logger;
        this.container = container;
        this.compatibility = new Compatibility();
        GlobalSoakData.MANAGER_INSTANCE = this;
        Bukkit.setServer(new NMSBounceSoakServer(Sponge::server));
        Sponge.eventManager().registerListeners(container, new SoakWrapperListener(this));
    }

    void loadPlugin() {
        JavaPlugin javaPlugin = getPlugin();
        SoakPluginClassLoader.setupPlugin(javaPlugin, this.container.metadata().id(), getPluginFile(), new File(getPluginFolder(), "config.yml"), getPluginFolder(), this::getPluginMeta, () -> this.getClass().getClassLoader());
    }

    private File getPluginFile() {
        if (this.container instanceof JVMPluginContainer jvmPlugin) {
            try {
                var field = JVMPluginContainer.class.getDeclaredField("candidate");
                field.setAccessible(true);
                PluginCandidate<? extends PluginResource> candidate = (PluginCandidate<? extends PluginResource>) field.get(jvmPlugin);
                field.setAccessible(false);

                var file = candidate.resource().path().toFile();

                var javaPluginClass = getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
                var env = new HashMap<String, String>();
                env.put("create", "true");
                fileSystem = FileSystems.newFileSystem(javaPluginClass, env);

                return file;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        //todo - forge
        throw new RuntimeException("Not implemented: " + this.container.getClass().getName());
    }

    private PluginMeta getPluginMeta() {
        return new SoakPluginMetaBuilder().from(this.container).setMain(getPlugin().getClass().getTypeName()).build();
    }

    private File getPluginFolder() {
        return Sponge.configManager().pluginConfig(this.container).configPath().toFile();
    }

    public JavaPlugin getPlugin() {
        return this.pluginCreator.get();
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public Stream<SoakPluginContainer> getBukkitContainers() {
        return Stream.of(this.soakPluginContainer);
    }

    @Override
    public PluginContainer getOwnContainer() {
        return this.container;
    }

    @Override
    public String getMinecraftVersion() {
        return Sponge.platform().minecraftVersion().toString();
    }

    @Override
    public SoakMemoryStore getMemoryStore() {
        return this.memoryStore;
    }

    @Override
    public SoakServerProperties getServerProperties() {
        return this.serverProperties;
    }

    @Override
    public Collection<Command> getBukkitCommands(Plugin plugin) {
        if (plugin.equals(this.getPlugin())) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(this.commands);
    }

    @Override
    public ConsoleHandler getConsole() {
        return this.consoleHandler;
    }

    @Override
    public ArtifactVersion getVersion() {
        return soakVersion;
    }

    @Override
    public Compatibility getCompatibility() {
        return compatibility;
    }
}
