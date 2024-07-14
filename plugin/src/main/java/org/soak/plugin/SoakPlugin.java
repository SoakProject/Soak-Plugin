package org.soak.plugin;

import com.google.inject.Inject;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.mosestream.MoseStream;
import org.soak.Compatibility;
import org.soak.commands.soak.SoakCommand;
import org.soak.config.SoakConfiguration;
import org.soak.config.SoakServerProperties;
import org.soak.exception.NMSUsageException;
import org.soak.fix.forge.ForgeFixCommons;
import org.soak.impl.data.BukkitPersistentData;
import org.soak.impl.data.sponge.PortalCooldownCustomData;
import org.soak.impl.data.sponge.SoakKeys;
import org.soak.map.SoakResourceKeyMap;
import org.soak.plugin.loader.Locator;
import org.soak.plugin.loader.common.AbstractSoakPluginContainer;
import org.soak.plugin.loader.common.SoakPluginContainer;
import org.soak.plugin.loader.common.SoakPluginInjector;
import org.soak.utils.SoakMemoryStore;
import org.soak.utils.log.CustomLoggerFormat;
import org.soak.wrapper.SoakServer;
import org.soak.wrapper.enchantment.SoakEnchantment;
import org.soak.wrapper.plugin.SoakPluginManager;
import org.soak.wrapper.potion.SoakPotionEffectType;
import org.soak.wrapper.v1_19_R4.NMSBounceSoakServer;
import org.spongepowered.api.Platform;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataStore;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.plugin.PluginContainer;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.spongepowered.plugin.builtin.jvm.Plugin("soak")
public class SoakPlugin {

    public static Key<Value<BukkitPersistentData>> BUKKIT_DATA;
    private static SoakPlugin plugin;
    private final SoakConfiguration configuration;
    private final PluginContainer container;
    private final Logger logger;
    private final Compatibility compatibility;
    private final SoakMemoryStore memoryStore = new SoakMemoryStore();

    private final SoakServerProperties serverProperties = new SoakServerProperties();
    private final ConsoleHandler consoleHandler = new ConsoleHandler();


    @Inject
    public SoakPlugin(PluginContainer pluginContainer, Logger logger) {
        plugin = this;
        this.container = pluginContainer;
        this.logger = logger;
        this.compatibility = new Compatibility();
        try {
            Path path = Sponge.configManager().pluginConfig(this.container).configPath();
            this.configuration = new SoakConfiguration(path.toFile());
            if (!this.configuration.file().exists()) {
                this.configuration.save();
            }
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public static SoakPlugin plugin() {
        return plugin;
    }

    public static SoakServer server() {
        return (SoakServer) Bukkit.getServer();
    }

    public ConsoleHandler getConsole() {
        return this.consoleHandler;
    }

    public SoakMemoryStore getMemoryStore() {
        return this.memoryStore;
    }

    //try not using
    public SoakServerProperties getServerProperties() {
        return this.serverProperties;
    }

    @Listener
    public void registerCommands(RegisterCommandEvent<Command.Parameterized> event) {
        event.register(this.container, SoakCommand.createSoakCommand(), "soak");
    }

    @Listener
    public void dataRegister(RegisterDataEvent event) {
        ResourceKey bukkitDataKey = ResourceKey.of(this.container, "plugin_data");

        BUKKIT_DATA = Key.builder().elementType(BukkitPersistentData.class).key(bukkitDataKey).build();

        DataStore dataStore = DataStore.of(BUKKIT_DATA,
                DataQuery.of("soak"),
                ItemStack.class,
                ItemStackSnapshot.class); //TODO -> find more
        DataRegistration registration = DataRegistration.builder()
                .dataKey(BUKKIT_DATA)
                .store(dataStore)
                .build();

        event.register(registration);

        SoakKeys.init(event);
    }

    @Listener(order = Order.FIRST)
    public void startingPlugin(StartingEngineEvent<Server> event) {
        startEnchantmentTypes();
        startPotionEffects();
        PortalCooldownCustomData.createTickScheduler();
    }

    @Listener(order = Order.LAST)
    public void endingPlugin(StoppingEngineEvent<Server> event) {
        var plugins = this.getPlugins().toList();
        plugins.forEach(plugin -> {
            Sponge.server().scheduler().executor(plugin).shutdown();
            Sponge.asyncScheduler().executor(plugin).shutdown();
        });

        Sponge.server().scheduler().executor(container).shutdown();
        Sponge.asyncScheduler().executor(container).shutdown();

        plugins.forEach(container -> {
            //ensures shutdown
            container.plugin().onDisable();
        });
        MoseStream.stream(plugins)
                .map(plugin -> SoakPlugin
                        .server()
                        .getPluginManager()
                        .getContext(plugin.plugin()))
                .forEach(context -> {
                    var loader = context.loader();
                    try {
                        logger.debug("Closing: " + context.getConfiguration().getName());
                        loader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        plugins.forEach(SoakPluginInjector::removePluginFromPlatform);

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

    public Compatibility getCompatibility() {
        return this.compatibility;
    }

    private void startEnchantmentTypes() {
        this.logger.info("Registering Enchantment Types");
        Registry<EnchantmentType> registry = RegistryTypes.ENCHANTMENT_TYPE.get();
        registry.stream().map(SoakEnchantment::new).forEach(Enchantment::registerEnchantment);
        Enchantment.stopAcceptingRegistrations();

    }

    private void startPotionEffects() {
        this.logger.info("Registering Potion Effect Types");
        Registry<PotionEffectType> registry = RegistryTypes.POTION_EFFECT_TYPE.get();

        Map<String, Integer> map = Arrays.stream(org.bukkit.potion.PotionEffectType.class.getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> Modifier.isFinal(field.getModifiers()))
                .filter(field -> field.getType().equals(org.bukkit.potion.PotionEffectType.class))
                .collect(Collectors.toMap(field -> {
                    String name = field.getName();
                    if (name.equals("SLOW")) {
                        return "SLOWNESS";
                    }
                    if (name.equals("FAST_DIGGING")) {
                        return "HASTE";
                    }
                    if (name.equals("SLOW_DIGGING")) {
                        return "MINING FATIGUE";
                    }
                    if (name.equals("HEAL")) {
                        return "INSTANT HEALTH";
                    }
                    if (name.equals("JUMP")) {
                        return "JUMP BOOST";
                    }
                    if (name.equals("DAMAGE_RESISTANCE")) {
                        return "RESISTANCE";
                    }
                    if (name.equals("INCREASE_DAMAGE")) {
                        return "INSTANT DAMAGE";
                    }
                    if (name.equals("CONFUSION")) {
                        return "NAUSEA";
                    }
                    return name.replaceAll("_", " ");
                }, field -> {
                    try {
                        //noinspection deprecation
                        return ((org.bukkit.potion.PotionEffectType) field.get(null)).getId();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }));
        registry
                .stream()
                .map(spongeType -> {
                    double duration = -1; //need to work this one out
                    boolean isInstant = spongeType.isInstant();
                    Color color = Color.RED; //need to work this one out
                    String name = PlainTextComponentSerializer.plainText().serialize(spongeType.asComponent());
                    int id = map.getOrDefault(name.toUpperCase(), 0);
                    String translationkey = ((TranslatableComponent) spongeType.asComponent()).key();
                    NamespacedKey key = SoakResourceKeyMap.mapToBukkit(spongeType.key(RegistryTypes.POTION_EFFECT_TYPE));
                    return new SoakPotionEffectType(duration, isInstant, color, name, translationkey, id, key);
                })
                .forEach(effect -> {
                    while (true) {
                        try {
                            org.bukkit.potion.PotionEffectType.registerPotionEffectType(effect);
                            break;
                        } catch (IllegalArgumentException e) {
                            //noinspection deprecation
                            effect = new SoakPotionEffectType(effect.getDurationModifier(),
                                    effect.isInstant(),
                                    effect.getColor(),
                                    effect.getName(),
                                    effect.translationKey(),
                                    effect.getId() + 1,
                                    effect.getKey());
                        }
                    }
                });

        org.bukkit.potion.PotionEffectType.stopAcceptingRegistrations();
    }

    @Listener
    public void construct(ConstructPluginEvent event) {
        if (ForgeFixCommons.isRequired()) {
            try {
                ForgeFixCommons.installApacheCommons();
                this.logger.info("Forced install of Apache 2");
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        this.consoleHandler.setFormatter(new CustomLoggerFormat());

        SoakServer server = new NMSBounceSoakServer(Sponge::server);
        SoakPluginManager pluginManager = server.getPluginManager();
        pluginManager.loadPlugins(configuration.file());
        //noinspection deprecation
        Bukkit.setServer(server);

        Collection<File> files = Locator.files();
        for (File file : files) {
            JavaPlugin plugin;
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

            SoakPluginContainer container = new AbstractSoakPluginContainer(file, plugin);
            SoakPluginInjector.injectPlugin(container);
            Sponge.eventManager().registerListeners(container, container.instance(), MethodHandles.lookup());
        }
    }

    public Stream<SoakPluginContainer> getPlugins() {
        return Sponge.pluginManager()
                .plugins()
                .stream()
                .filter(container -> container instanceof SoakPluginContainer)
                .map(container -> (SoakPluginContainer) container);
    }

    public SoakPluginContainer getPlugin(@NotNull Plugin plugin) {
        return getPlugins().filter(pl -> pl.plugin().equals(plugin))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find plugin, is it registered?"));
    }

    public void displayError(Throwable e, File pluginFile) {
        displayError(e, Map.of("Plugin file", pluginFile.getPath()));
    }

    public void displayError(Throwable e, Plugin plugin, Map.Entry<String, String>... additions) {
        Map<String, String> pluginData = new HashMap<>();
        pluginData.put("Plugin name", plugin.getName());
        if (plugin instanceof SoakPlugin) {
            pluginData.put("Plugin file", ((SoakPlugin) plugin).configuration.file().getPath());
        }
        for (Map.Entry<String, String> entry : additions) {
            pluginData.put(entry.getKey(), entry.getValue());
        }

        displayError(e, pluginData);
    }

    private void displayError(Throwable e, Map<String, String> pluginData) {
        while (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
        }
        Throwable readEx = e;
        while(true){
            if(readEx instanceof InvocationTargetException ex){
                readEx = ex.getTargetException();
                continue;
            }
            if(readEx instanceof ExceptionInInitializerError ex){
                readEx = ex.getException();
            }
            if(readEx instanceof RuntimeException runtime){
                var cause = runtime.getCause();
                if(cause instanceof InvocationTargetException || cause instanceof ExceptionInInitializerError){
                    readEx = cause;
                    continue;
                }
            }
            break;
        }


        this.logger.error("|------------------------|");
        pluginData.forEach((key, value) -> {
            this.logger.error("|- " + key + ": " + value);
        });
        this.logger.error("|- Soak version: " + this.container.metadata().version().toString());
        this.logger.error("|- Compatibility: " + this.compatibility.getName());
        this.logger.error("|- Compatibility version: " + this.compatibility.getVersion());
        this.logger.error("|- Compatibility Minecraft version: " + this.compatibility.getTargetMinecraftVersion());
        this.logger.error("|- Minecraft version: " + Sponge.platform().minecraftVersion().name());
        this.logger.error("|- Sponge API version: " + Sponge.platform()
                .container(Platform.Component.API)
                .metadata()
                .version());

        if (readEx instanceof ClassCastException) {
            if (readEx.getMessage().contains("org.bukkit.plugin.SimplePluginManager")) {
                this.logger.error(
                        "|- Common Error Note: Starting on Paper hardfork 1.19.4, SimplePluginManager is being disconnected. This will not be added to soak");
            }
        }
        if (readEx instanceof NMSUsageException nmsUsage) {
            this.logger.error("|- Common Error Note: A plugin attempted to use NMS which is not supported by soak. This can only be fixed by the plugin developer");
            nmsUsage.getDeveloperNotes().ifPresent(message -> {
                this.logger.error("|- For Plugin Developer: " + message);
            });
        }
        if (readEx instanceof NoClassDefFoundError || readEx instanceof ClassCastException || readEx instanceof ClassNotFoundException) {
            if (e.getMessage().contains("net/minecraft/") || e.getMessage().contains("net.minecraft.")) {
                this.logger.error("|- Common Error Note: Error is caused due to " + this.container.metadata().id() + " using NMS. This is something Soak does not prioritise on fixing.");
                this.logger.error("   It is up to plugin devs to use the Spigot/Paper API to create a work around for when NMS cannot be used.");
            }
        }

        this.logger.error("|------------------------|");
        this.logger.error("Error", e);
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

    public boolean isNMSBounceIncluded() {
        try {
            Class.forName("net.minecraft.network.chat.IChatBaseComponent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
