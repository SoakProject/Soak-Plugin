package org.soak.plugin;

import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.soak.Compatibility;
import org.soak.WrapperManager;
import org.soak.command.BukkitRawCommand;
import org.soak.data.sponge.PortalCooldownCustomData;
import org.soak.data.sponge.SoakKeys;
import org.soak.io.SoakServerProperties;
import org.soak.utils.SoakMemoryStore;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataStore;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.plugin.PluginContainer;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.ConsoleHandler;
import java.util.stream.Stream;

public class AbstractSpongePluginMain implements SoakInternalManager, WrapperManager {

    private static final ArtifactVersion soakVersion = new DefaultArtifactVersion("0.0.1");
    private final JavaPlugin plugin;
    private final Collection<Command> commands = new LinkedTransferQueue<>();
    private final InternalSoakPluginContainer soakPluginContainer = new InternalSoakPluginContainer(this);
    private final Logger logger;
    private final PluginContainer container;
    private final Compatibility compatibility;
    private final SoakServerProperties serverProperties = new SoakServerProperties();
    private final ConsoleHandler consoleHandler = new ConsoleHandler();
    private final SoakMemoryStore memoryStore = new SoakMemoryStore();

    public AbstractSpongePluginMain(JavaPlugin plugin, Logger logger, PluginContainer container) {
        this.plugin = plugin;
        this.logger = logger;
        this.container = container;
        this.compatibility = new Compatibility();

    }

    public JavaPlugin getPlugin() {
        return this.plugin;
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
        if (plugin.equals(this.plugin)) {
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

    @Listener
    public void onPluginCommandRegister(RegisterCommandEvent<org.spongepowered.api.command.Command.Raw> event) {
        var bukkitCommands = PluginCommandYamlParser.parse(plugin);
        this.commands.addAll(bukkitCommands);
        bukkitCommands.forEach(cmd -> event.register(container, new BukkitRawCommand(soakPluginContainer, cmd), cmd.getName(), cmd.getAliases().toArray(String[]::new)));
    }

    @Listener
    public void onPluginLoad(StartingEngineEvent<Server> event) {
        logger.warn("On Engine starting");
        try {
            plugin.onLoad();
        } catch (Throwable e) {
            SoakManager.getManager().displayError(e, plugin);
        }
    }

    //issue
    //Bukkit plugins assume everything is loaded when onEnable is run, this is because Craftbukkit loads everything before onEnable is used ....
    //using StartedEngineEvent despite the timing known to be incorrect
    @Listener
    public void onPluginEnable(StartedEngineEvent<Server> event) {
        logger.warn("On Engine started");
        try {
            plugin.onEnable();
        } catch (Throwable e) {
            SoakManager.getManager().displayError(e, plugin);
        }
    }


    @Listener
    public void dataRegister(RegisterDataEvent event) {
        DataStore dataStore = DataStore.of(SoakKeys.BUKKIT_DATA,
                DataQuery.of("soak"),
                ItemStack.class,
                ItemStackSnapshot.class); //TODO -> find more
        DataRegistration registration = DataRegistration.builder()
                .dataKey(SoakKeys.BUKKIT_DATA)
                .store(dataStore)
                .build();

        event.register(registration);

        SoakKeys.init(event);
    }

    @Listener(order = Order.FIRST)
    public void startingPlugin(StartingEngineEvent<Server> event) {
        SoakRegister.startEnchantmentTypes(this.logger);
        SoakRegister.startPotionEffects(this.logger);
        PortalCooldownCustomData.createTickScheduler();
    }

    @Listener
    public void stoppingPlugin(StoppingEngineEvent<Server> event) {
        plugin.onDisable();
    }

    @Listener(order = Order.LAST)
    public void endingPlugin(StoppingEngineEvent<Server> event) {
        Sponge.server().scheduler().executor(this.container).shutdown();
        Sponge.asyncScheduler().executor(this.container).shutdown();
        /*MoseStream.stream(plugins)
                .map(plugin -> SoakPlugin
                        .server()
                        .getPluginManager()
                        .getContext(plugin.getBukkitInstance()))
                .forEach(context -> {
                    var loader = context.loader();
                    try {
                        logger.debug("Closing: " + context.getConfiguration().getName());
                        loader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        plugins.forEach(SoakPluginInjector::removePluginFromPlatform);*/

        var thread = new Thread(() -> {
            while (Thread.getAllStackTraces().keySet().stream().anyMatch(mainThread -> mainThread.getName().equals("server thread"))) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //for some reason threads get blocked when soak loads plugins. this forces the shutoff.
            //TODO -> figure out why threads get blocked
            logger.debug("Using ublocking fix from Soak");
            System.exit(0);
        });
        thread.setName("unblocker");
        thread.start();
    }
}
