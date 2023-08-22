package org.soak.plugin;

import com.google.inject.Inject;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.soak.Compatibility;
import org.soak.commands.soak.SoakCommand;
import org.soak.config.SoakServerProperties;
import org.soak.impl.data.BukkitPersistentData;
import org.soak.plugin.config.SoakConfiguration;
import org.soak.plugin.loader.Locator;
import org.soak.plugin.loader.sponge.SoakPluginContainer;
import org.soak.plugin.loader.sponge.injector.SoakPluginInjector;
import org.soak.utils.SoakMemoryStore;
import org.soak.wrapper.SoakServer;
import org.soak.wrapper.enchantment.SoakEnchantment;
import org.soak.wrapper.plugin.SoakPluginManager;
import org.soak.wrapper.potion.SoakPotionEffectType;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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

    @Inject
    public SoakPlugin(PluginContainer pluginContainer, Logger logger) {
        plugin = this;
        this.container = pluginContainer;
        this.logger = logger;
        this.compatibility = new Compatibility();
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

    public static SoakServer server() {
        return (SoakServer) Bukkit.getServer();
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
    }

    @Listener(order = Order.FIRST)
    public void startingPlugin(StartingEngineEvent<Server> event) {
        startEnchantmentTypes();
        startPotionEffects();
    }

    @Listener(order = Order.LAST)
    public void endingPlugin(StoppingEngineEvent<Server> event) {
        this.getPlugins().forEach(plugin -> {
            Sponge.server().scheduler().executor(plugin).shutdown();
            Sponge.asyncScheduler().executor(plugin).shutdown();
        });

        Sponge.server().scheduler().executor(container).shutdown();
        Sponge.asyncScheduler().executor(container).shutdown();
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
                    return new SoakPotionEffectType(duration, isInstant, color, name, id);
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
                                    effect.getId() + 1);
                        }
                    }
                });

        org.bukkit.potion.PotionEffectType.stopAcceptingRegistrations();
    }

    @Listener
    public void construct(ConstructPluginEvent event) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$s] %5$s %n");
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
        if (plugin instanceof SoakPlugin soakPlugin) {
            pluginData.put("Plugin file", soakPlugin.configuration.file().getPath());
        }
        for (Map.Entry<String, String> entry : additions) {
            pluginData.put(entry.getKey(), entry.getValue());
        }

        displayError(e, pluginData);
    }

    private void displayError(Throwable e, Map<String, String> pluginData) {
        if (e instanceof InvocationTargetException targetException) {
            e = targetException.getTargetException();
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

        if (e instanceof ClassCastException castException) {
            if (castException.getMessage().contains("org.bukkit.plugin.SimplePluginManager")) {
                this.logger.error(
                        "|- Common Error Note: Starting on Paper hardfork 1.19.4, SimplePluginManager is being disconnected. This will not be added to soak");
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

}
