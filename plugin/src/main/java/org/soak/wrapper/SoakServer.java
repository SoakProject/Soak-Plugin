package org.soak.wrapper;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.entity.ai.MobGoals;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.datapack.DatapackManager;
import io.papermc.paper.tag.EntitySetTag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.packs.DataPackManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakMessageMap;
import org.soak.map.SoakResourceKeyMap;
import org.soak.map.item.SoakRecipeMap;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.plugin.loader.common.SoakPluginContainer;
import org.soak.plugin.utils.Singleton;
import org.soak.plugin.utils.Unfinal;
import org.soak.plugin.utils.log.CustomLoggerFormat;
import org.soak.utils.FakeRegistryHelper;
import org.soak.utils.GenericHelper;
import org.soak.utils.InventoryHelper;
import org.soak.utils.TagHelper;
import org.soak.wrapper.command.SoakConsoleCommandSender;
import org.soak.wrapper.inventory.SoakInventory;
import org.soak.wrapper.inventory.SoakItemFactory;
import org.soak.wrapper.plugin.SoakPluginManager;
import org.soak.wrapper.profile.SoakPlayerProfile;
import org.soak.wrapper.scheduler.SoakBukkitScheduler;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.tag.BlockTypeTags;
import org.spongepowered.api.tag.EntityTypeTags;
import org.spongepowered.api.tag.FluidTypeTags;
import org.spongepowered.api.tag.ItemTypeTags;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SoakServer implements SimpServer {

    private final Supplier<org.spongepowered.api.Server> serverSupplier;
    private final Singleton<SoakPluginManager> pluginManager = new Singleton<>(() -> new SoakPluginManager(Sponge::pluginManager));
    private final Singleton<SoakUnsafeValues> unsafeValues = new Singleton<>(SoakUnsafeValues::new);
    private final Singleton<SoakItemFactory> itemFactory = new Singleton<>(SoakItemFactory::new);
    private final Singleton<SoakBukkitScheduler> scheduler = new Singleton<>(SoakBukkitScheduler::new);

    private final Collection<Recipe> recipes = new LinkedTransferQueue<>();
    private final java.util.logging.Logger logger;

    public SoakServer(Supplier<org.spongepowered.api.Server> serverSupplier) {
        this.logger = java.util.logging.Logger.getLogger("soak");
        this.logger.setUseParentHandlers(false);
        this.logger.setLevel(Level.INFO);
        var consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomLoggerFormat());
        this.logger.addHandler(consoleHandler);
        this.serverSupplier = serverSupplier;
    }

    public org.spongepowered.api.Server spongeServer() {
        return this.serverSupplier.get();
    }

    @Override
    public <T extends Keyed> Tag<T> getTag(@NotNull String registry, @NotNull NamespacedKey tag, @NotNull Class<T> clazz) {
        ResourceKey key = SoakResourceKeyMap.mapToSponge(tag);
        if (clazz.getName().equals(Material.class.getName()) && registry.equals(Tag.REGISTRY_BLOCKS)) {
            var opTag = FakeRegistryHelper.<org.spongepowered.api.tag.Tag<BlockType>>getFields(BlockTypeTags.class, org.spongepowered.api.tag.Tag.class)
                    .stream()
                    .filter(spongeTag -> spongeTag.key().equals(key))
                    .findAny();
            if (opTag.isPresent()) {
                return (Tag<T>) (Object) new MaterialSetTag(tag, TagHelper.getBlockTypes(opTag.get()).map(Material::getBlockMaterial).collect(Collectors.toList()));
            }
        }
        if (clazz.getName().equals(Material.class.getName()) && registry.equals(Tag.REGISTRY_ITEMS)) {
            var opTag = FakeRegistryHelper.<org.spongepowered.api.tag.Tag<ItemType>>getFields(ItemTypeTags.class, org.spongepowered.api.tag.Tag.class)
                    .stream()
                    .filter(spongeTag -> spongeTag.key().equals(key))
                    .findAny();
            if (opTag.isPresent()) {
                return (Tag<T>) (Object) new MaterialSetTag(tag, TagHelper.getItemTypes(opTag.get()).map(Material::getItemMaterial).collect(Collectors.toList()));
            }
        }
        if (clazz.getName().equals(EntityType.class.getName()) && registry.equals(Tag.REGISTRY_ENTITY_TYPES)) {
            var opTag = FakeRegistryHelper.<org.spongepowered.api.tag.Tag<org.spongepowered.api.entity.EntityType<?>>>getFields(EntityTypeTags.class, org.spongepowered.api.tag.Tag.class)
                    .stream()
                    .filter(spongeTag -> spongeTag.key().equals(key))
                    .findAny();
            if (opTag.isPresent()) {
                return (Tag<T>) (Object) new EntitySetTag(tag, TagHelper.getEntityTypes(opTag.get()).map(EntityType::fromSponge).collect(Collectors.toList()));
            }
        }


        //overrides
        if (registry.equals(Tag.REGISTRY_ITEMS) && tag.asString().equals("minecraft:coral_blocks")) {
            return (Tag<T>) (Object) new MaterialSetTag(tag,
                    ItemTypes.registry()
                            .stream()
                            .filter(item -> item.key(RegistryTypes.ITEM_TYPE).value().contains("coral_blocks"))
                            .map(item -> Material.getItemMaterial(item))
                            .collect(Collectors.toList()));
        }

        if (registry.equals(Tag.REGISTRY_BLOCKS) && tag.asString()
                .equals("minecraft:wool_carpets")) {
            Set<Material> itemTypes = TagHelper.getBlockTypes(BlockTypeTags.WOOL_CARPETS).map(Material::getBlockMaterial).collect(Collectors.toSet());
            return (Tag<T>) (Object) new MaterialSetTag(tag, itemTypes);
        }

        if (registry.equals(Tag.REGISTRY_ITEMS) && tag.asString().equals("minecraft:wool_carpets")) {
            Set<Material> itemTypes = TagHelper.getItemTypes(ItemTypeTags.WOOL_CARPETS).map(Material::getItemMaterial).collect(Collectors.toSet());
            return (Tag<T>) (Object) new MaterialSetTag(tag, itemTypes);

        }

        if (registry.equals(Tag.REGISTRY_FLUIDS) && tag.asString().equals("minecraft:water")) {
            return (Tag<T>) (Object) new SoakFluidTag(FluidTypeTags.WATER);
        }

        if (registry.equals(Tag.REGISTRY_FLUIDS) && tag.asString().equals("minecraft:lava")) {
            return (Tag<T>) (Object) new SoakFluidTag(FluidTypeTags.LAVA);
        }

        if (registry.equals(Tag.REGISTRY_ITEMS) && tag.asString().equals("minecraft:crops")) {
            var items = ItemTypes.registry()
                    .stream()
                    .filter(item -> org.spongepowered.api.item.inventory.ItemStack.of(item)
                            .get(Keys.REPLENISHED_FOOD)
                            .isPresent())
                    .map(Material::getItemMaterial)
                    .collect(Collectors.toSet());
            return (Tag<T>) (Object) new MaterialSetTag(tag, items);
        }
        if (registry.equals(Tag.REGISTRY_ITEMS) && tag.asString().equals("minecraft:furnace_materials")) {
            var items = ItemTypes.registry()
                    .stream()
                    .filter(item -> org.spongepowered.api.item.inventory.ItemStack.of(item)
                            .get(Keys.MAX_COOK_TIME)
                            .isPresent())
                    .map(Material::getItemMaterial)
                    .collect(Collectors.toSet());
            return (Tag<T>) (Object) new MaterialSetTag(tag, items);
        }

        System.err.println("No tag found of registry: '" + registry + "' Type: " + clazz.getSimpleName() + " id: " + tag.asString());
        return null;
    }

    @Override
    public @NotNull Spigot spigot() {
        throw NotImplementedException.createByLazy(Server.class, "spigot");
    }

    @Override
    public @NotNull File getWorldContainer() {
        throw NotImplementedException.createByLazy(Server.class, "getWorldContainer");
    }

    @Override
    public @NotNull File getPluginsFolder() {
        return new File("bukkit/plugins");
    }

    @Override
    public @NotNull String getName() {
        return SoakPlugin.plugin().container().metadata().id();
    }

    @Override
    public @NotNull String getVersion() {
        return "(MC: " + getMinecraftVersion() + ") - " + SoakPlugin.plugin()
                .container()
                .metadata()
                .version()
                .toString();
    }

    @Override
    public @NotNull String getBukkitVersion() {
        return getMinecraftVersion() + "-R" + SoakPlugin.plugin()
                .container()
                .metadata()
                .version()
                .toString() + "-" + SoakPlugin.plugin().container().metadata().id();
    }

    @Override
    public @NotNull String getMinecraftVersion() {
        return Sponge.platform().minecraftVersion().name();
    }

    @Override
    public @NotNull Collection<? extends Player> getOnlinePlayers() {
        return this.spongeServer()
                .onlinePlayers()
                .stream()
                .map(spongePlayer -> SoakPlugin.plugin().getMemoryStore().get(spongePlayer))
                .collect(Collectors.toList());
    }

    @Override
    public int getMaxPlayers() {
        return this.spongeServer().maxPlayers();
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        throw NotImplementedException.createByLazy(SoakServer.class, "setMaxPlayers", int.class);
    }

    @Override
    public int getPort() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getPort");
    }

    @Override
    public int getViewDistance() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getViewDistance");
    }

    @Override
    public int getSimulationDistance() {
        throw NotImplementedException.createByLazy(Server.class, "getSimulationDistance");
    }

    @Override
    public @NotNull String getIp() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getIp");
    }

    @Override
    public @NotNull String getWorldType() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getWorldType");
    }

    @Override
    public boolean getGenerateStructures() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getGenerateStructures");
    }

    @Override
    public int getMaxWorldSize() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getMaxWorldSize");
    }

    @Override
    public boolean getAllowEnd() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getAllowEnd");
    }

    @Override
    public boolean getAllowNether() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getAllowNether");
    }

    @Override
    public @NotNull List<String> getInitialEnabledPacks() {
        throw NotImplementedException.createByLazy(Server.class, "getInitialEnabledPacks");
    }

    @Override
    public @NotNull List<String> getInitialDisabledPacks() {
        throw NotImplementedException.createByLazy(Server.class, "getInitialDisabledPacks");
    }

    @Override
    public @NotNull DataPackManager getDataPackManager() {
        throw NotImplementedException.createByLazy(Server.class, "getDataPackManager");
    }

    @Override
    public @NotNull String getResourcePack() {
        throw NotImplementedException.createByLazy(Server.class, "getResourcePack");
    }

    @Override
    public @NotNull String getResourcePackHash() {
        throw NotImplementedException.createByLazy(Server.class, "getResourcePackHash");
    }

    @Override
    public @NotNull String getResourcePackPrompt() {
        throw NotImplementedException.createByLazy(Server.class, "getResourcePackPrompt");
    }

    @Override
    public boolean isResourcePackRequired() {
        throw NotImplementedException.createByLazy(Server.class, "isResourcePackRequired");
    }

    @Override
    public void setWhitelist(boolean value) {
        throw NotImplementedException.createByLazy(SoakServer.class, "setWhitelist", boolean.class);
    }

    @Override
    public boolean isWhitelistEnforced() {
        throw NotImplementedException.createByLazy(Server.class, "isWhitelistEnforced");
    }

    @Override
    public void setWhitelistEnforced(boolean b) {
        throw NotImplementedException.createByLazy(Server.class, "setWhitelistEnforced", boolean.class);
    }

    @Override
    public @NotNull Set<OfflinePlayer> getWhitelistedPlayers() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getWhitelistedPlayers");
    }

    @Override
    public void reloadWhitelist() {
        throw NotImplementedException.createByLazy(SoakServer.class, "reloadWhitelist");
    }

    @Override
    @Deprecated
    public int broadcastMessage(@NotNull String message) {
        return this.broadcast(SoakMessageMap.toComponent(message));
    }

    @Override
    public @NotNull String getUpdateFolder() {
        return getUpdateFolderFile().getAbsolutePath();
    }

    @Override
    public @NotNull File getUpdateFolderFile() {
        return new File("mods/bukkit/update");
    }

    @Override
    public long getConnectionThrottle() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getConnectionThrottle");
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getTicksPerAnimalSpawn");
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getTicksPerMonsterSpawn");
    }

    @Override
    public int getTicksPerWaterSpawns() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getTicksPerWaterSpawns");
    }

    @Override
    public int getTicksPerWaterAmbientSpawns() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getTicksPerWaterAnbientSpawns");
    }

    @Override
    public int getTicksPerWaterUndergroundCreatureSpawns() {
        throw NotImplementedException.createByLazy(Server.class, "getTicksPerWaterUndergroundCreatureSpawns");
    }

    @Override
    public int getTicksPerAmbientSpawns() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getTicksPerAmbientSpawns");
    }

    @Override
    public int getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
        throw NotImplementedException.createByLazy(Server.class, "getTicksPerSpawns", SpawnCategory.class);
    }

    @Override
    public @Nullable Player getPlayerExact(@NotNull String name) {
        return this.getOnlinePlayers().stream().filter(player -> player.getName().equals(name)).findAny().orElse(null);

    }

    @Override
    public @NotNull List<Player> matchPlayer(@NotNull String name) {
        throw NotImplementedException.createByLazy(SoakServer.class, "matchPlayer", String.class);
    }

    @Override
    public @Nullable UUID getPlayerUniqueId(@NotNull String playerName) {
        Player player = getPlayerExact(playerName);
        if (player == null) {
            return null;
        }
        return player.getUniqueId();
    }

    @Override
    public @NotNull SoakPluginManager getPluginManager() {
        return this.pluginManager.get();
    }

    @Override
    public @NotNull BukkitScheduler getScheduler() {
        return this.scheduler.get();
    }

    @Override
    public @NotNull ServicesManager getServicesManager() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getServicesManager");
    }

    @Override
    public @NotNull List<World> getWorlds() {
        return this.spongeServer()
                .worldManager()
                .worlds()
                .stream()
                .map(world -> SoakPlugin.plugin().getMemoryStore().get(world))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTickingWorlds() {
        throw NotImplementedException.createByLazy(Server.class, "isTickingWorlds");
    }

    @Override
    public @Nullable World createWorld(@NotNull WorldCreator creator) {
        throw NotImplementedException.createByLazy(SoakServer.class, "createWorld", WorldCreator.class);
    }

    @Override
    public boolean unloadWorld(@NotNull World world, boolean save) {
        throw NotImplementedException.createByLazy(SoakServer.class, "unloadWorld", World.class, boolean.class);
    }

    @Override
    public @NotNull WorldBorder createWorldBorder() {
        throw NotImplementedException.createByLazy(Server.class, "createWorldBorder");
    }

    @Override
    public @Nullable MapView getMap(int id) {
        throw NotImplementedException.createByLazy(SoakServer.class, "getMap", int.class);
    }

    @Override
    public @NotNull MapView createMap(@NotNull World world) {
        throw NotImplementedException.createByLazy(SoakServer.class, "createMap", World.class);
    }

    @Override
    public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType) {
        throw NotImplementedException.createByLazy(SoakServer.class,
                "createExplorerMap",
                World.class,
                Location.class,
                StructureType.class);
    }

    @Override
    public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
        throw NotImplementedException.createByLazy(SoakServer.class,
                "createExplorerMap",
                World.class,
                Location.class,
                StructureType.class,
                boolean.class);
    }

    @Override
    public void reload() {
        SoakPlugin.plugin()
                .logger()
                .warn("A Bukkit plugin attempted to reload the plugin list. This is not possible in Sponge, reloading the data instead");
        reloadData();
    }

    @Override
    public void reloadData() {
        throw NotImplementedException.createByLazy(SoakServer.class, "reloadData");
    }

    @Override
    public @Nullable PluginCommand getPluginCommand(@NotNull String name) {
        PluginCommand pluginCommand = Sponge
                .pluginManager()
                .plugins()
                .stream()
                .filter(pl -> pl instanceof SoakPluginContainer)
                .sorted(Comparator.comparing(pl -> pl.metadata().id()))
                .map(pl -> ((SoakPluginContainer) pl).instance())
                .flatMap(pl -> pl.commands().stream())
                .filter(cmd -> cmd.getName().equalsIgnoreCase(name))
                .filter(cmd -> cmd instanceof PluginCommand)
                .findFirst()
                .map(cmd -> (PluginCommand) cmd)
                .orElse(null);
        if (pluginCommand == null) {
            SoakPlugin.plugin().logger().warn("A Bukkit plugin attempted to access the command '" + name + "'. It however does not exist");
        }
        return pluginCommand;

    }

    @Override
    public void savePlayers() {
        throw NotImplementedException.createByLazy(SoakServer.class, "savePlayers");
    }

    @Override
    public boolean dispatchCommand(@NotNull CommandSender sender, @NotNull String commandLine) throws CommandException {
        throw NotImplementedException.createByLazy(SoakServer.class,
                "dispatchCommand",
                CommandSender.class,
                String.class);
    }

    @Override
    public boolean addRecipe(@Nullable Recipe recipe) {
        throw NotImplementedException.createByLazy(SoakServer.class, "addRecipe", Recipe.class);
    }

    @Override
    public @Nullable Recipe getCraftingRecipe(@NotNull ItemStack[] itemStacks, @NotNull World world) {
        throw NotImplementedException.createByLazy(Server.class, "getCraftingRecipe", ItemStack.class, World.class);
    }

    @Override
    public @NotNull ItemStack craftItem(@NotNull ItemStack[] itemStacks, @NotNull World world, @NotNull Player player) {
        throw NotImplementedException.createByLazy(Server.class, "craftItem", ItemStack.class, World.class, Player.class);
    }

    @Override
    public @NotNull Iterator<Recipe> recipeIterator() {
        return Sponge
                .server()
                .recipeManager()
                .all()
                .stream()
                .<Recipe>map(recipe -> {
                    try {
                        return SoakRecipeMap.toBukkit(recipe);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .iterator();
    }

    @Override
    public void clearRecipes() {
        throw NotImplementedException.createByLazy(SoakServer.class, "clearRecipes");
    }

    @Override
    public void resetRecipes() {
        throw NotImplementedException.createByLazy(SoakServer.class, "resetRecipes");
    }

    @Override
    public boolean removeRecipe(@NotNull NamespacedKey key) {
        throw NotImplementedException.createByLazy(SoakServer.class, "removeRecipe", NamespacedKey.class);
    }

    @Override
    public @NotNull Map<String, String[]> getCommandAliases() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getCommandAliases", Map.class);
    }

    public ServerWorld defaultWorld() {
        return this.spongeServer()
                .worldManager()
                .world(DefaultWorldKeys.DEFAULT)
                .orElseThrow(() -> new RuntimeException("default world is not loaded"));
    }

    @Override
    public int getSpawnRadius() {
        return SoakPlugin.plugin().getServerProperties().spawnProtection().orElse();
    }

    @Override
    public void setSpawnRadius(int value) {
        SoakPlugin.plugin().getServerProperties().spawnProtection().setMemoryValue(value);
    }

    @Override
    public boolean shouldSendChatPreviews() {
        throw NotImplementedException.createByLazy(Server.class, "shouldSendChatPreviews");
    }

    @Override
    public boolean isEnforcingSecureProfiles() {
        throw NotImplementedException.createByLazy(Server.class, "isEnforcingSecureProfiles");
    }

    @Override
    public boolean getHideOnlinePlayers() {
        throw NotImplementedException.createByLazy(Server.class, "getHideOnlinePlayers");
    }

    @Override
    public boolean getOnlineMode() {
        return SoakPlugin.plugin().getServerProperties().onlineMode().orElse();
    }

    @Override
    public boolean getAllowFlight() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getAllowFlight");
    }

    @Override
    public boolean isHardcore() {
        throw NotImplementedException.createByLazy(SoakServer.class, "isHardcore");
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull String name) {
        var cached = getOfflinePlayerIfCached(name);
        if (cached != null) {
            return cached;
        }
        var future = Sponge.server()
                .gameProfileManager()
                .profile(name)
                .thenCompose(profile -> Sponge.server().userManager().loadOrCreate(profile.uuid()));
        return new SoakOfflinePlayer(future);
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayerIfCached(@NotNull String name) {
        if (Sponge.server().userManager().streamOfMatches(name).findAny().isEmpty()) {
            return null;
        }
        var future = Sponge.server()
                .userManager()
                .load(name)
                .thenApply(op -> op.orElseThrow(() -> new RuntimeException("Failed to load cached player")));
        return new SoakOfflinePlayer(future);
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID id) {
        var future = Sponge.server().userManager().loadOrCreate(id);
        return new SoakOfflinePlayer(future);
    }

    @Override
    public org.bukkit.profile.@NotNull PlayerProfile createPlayerProfile(@Nullable UUID uuid, @Nullable String s) {
        throw NotImplementedException.createByLazy(Server.class, "createPlayerProfile", UUID.class, String.class);
    }

    @Override
    public org.bukkit.profile.@NotNull PlayerProfile createPlayerProfile(@NotNull UUID uuid) {
        throw NotImplementedException.createByLazy(Server.class, "createPlayerProfile", UUID.class);
    }

    @Override
    public org.bukkit.profile.@NotNull PlayerProfile createPlayerProfile(@NotNull String s) {
        throw NotImplementedException.createByLazy(Server.class, "createPlayerProfile", String.class);
    }

    @Override
    public @NotNull Set<String> getIPBans() {
        throw NotImplementedException.createByLazy(SoakServer.class, "getIPBans");
    }

    @Override
    public void banIP(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Server.class, "banIP", String.class);
    }

    @Override
    public void unbanIP(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Server.class, "unbanIP", String.class);
    }

    @Override
    public @NotNull Set<OfflinePlayer> getBannedPlayers() {
        throw NotImplementedException.createByLazy(Server.class, "getBannedPlayers");
    }

    @Override
    public @NotNull BanList getBanList(@NotNull BanList.Type type) {
        throw NotImplementedException.createByLazy(Server.class, "getBanList", BanList.Type.class);
    }

    @Override
    public @NotNull Set<OfflinePlayer> getOperators() {
        throw NotImplementedException.createByLazy(Server.class, "getOperators");
    }

    @Override
    public @NotNull GameMode getDefaultGameMode() {
        throw NotImplementedException.createByLazy(Server.class, "getDefaultGameMode");
    }

    @Override
    public void setDefaultGameMode(@NotNull GameMode arg0) {
        throw NotImplementedException.createByLazy(Server.class, "setDefaultGameMode", GameMode.class);
    }

    @Override
    public @NotNull ConsoleCommandSender getConsoleSender() {
        return new SoakConsoleCommandSender();
    }

    @Override
    public @NotNull CommandSender createCommandSender(@NotNull Consumer<? super Component> consumer) {
        throw NotImplementedException.createByLazy(Server.class, "createCommandSender", Consumer.class);
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        throw NotImplementedException.createByLazy(Server.class, "getOfflinePlayers");
    }

    @Override
    public @NotNull Messenger getMessenger() {
        throw NotImplementedException.createByLazy(Server.class, "getMessenger");
    }

    @Override
    public @NotNull HelpMap getHelpMap() {
        throw NotImplementedException.createByLazy(Server.class, "getHelpMap");
    }

    @Override
    public @NotNull Inventory createInventory(InventoryHolder arg0, @NotNull InventoryType arg1) {
        throw NotImplementedException.createByLazy(Server.class,
                "createInventory",
                InventoryHolder.class,
                InventoryType.class);
    }

    @Override
    public @NotNull Inventory createInventory(InventoryHolder arg0, @NotNull InventoryType arg1, @NotNull Component arg2) {
        throw NotImplementedException.createByLazy(Server.class,
                "createInventory",
                InventoryHolder.class,
                InventoryType.class,
                Component.class);
    }

    @Deprecated
    @Override
    public @NotNull Inventory createInventory(InventoryHolder arg0, @NotNull InventoryType arg1, @NotNull String arg2) {
        return createInventory(arg0, arg1, SoakMessageMap.toComponent(arg2));
    }

    @Override
    public @NotNull Inventory createInventory(InventoryHolder arg0, int arg1) {
        return createChestInventory(arg0, arg1, null);
    }

    @Override
    public @NotNull Inventory createInventory(InventoryHolder arg0, int arg1, @NotNull Component arg2) {
        return createChestInventory(arg0, arg1, arg2);
    }

    private Inventory createChestInventory(InventoryHolder holder, int size, @Nullable Component title) {
        int rows = (size / 9);
        var containerType = InventoryHelper.toChestContainerType(rows);
        var plugin = GenericHelper.fromStackTrace();
        var inventory = ViewableInventory.builder().type(containerType).completeStructure().plugin(plugin).build();
        //TODO holder
        var bukkitInv = new SoakInventory<>(inventory);
        bukkitInv.setRequestedTitle(title);
        return bukkitInv;
    }

    @Deprecated
    @Override
    public @NotNull Inventory createInventory(InventoryHolder arg0, int arg1, @NotNull String arg2) {
        return createInventory(arg0, arg1, SoakMessageMap.toComponent(arg2));
    }

    @Deprecated
    @Override
    public @NotNull Merchant createMerchant(String arg0) {
        throw NotImplementedException.createByLazy(Server.class, "createMerchant", String.class);
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
        throw NotImplementedException.createByLazy(Server.class, "getMaxChainedNeighborUpdates");
    }

    @Override
    public @NotNull Merchant createMerchant(Component arg0) {
        throw NotImplementedException.createByLazy(Server.class, "createMerchant", Component.class);
    }

    @Override
    public int getMonsterSpawnLimit() {
        throw NotImplementedException.createByLazy(Server.class, "getMonsterSpawnLimit");
    }

    @Override
    public int getAnimalSpawnLimit() {
        throw NotImplementedException.createByLazy(Server.class, "getAnimalSpawnLimit");
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        throw NotImplementedException.createByLazy(Server.class, "getWaterAnimalSpawnLimit");
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        throw NotImplementedException.createByLazy(Server.class, "getWaterAmbientSpawnLimit");
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        throw NotImplementedException.createByLazy(Server.class, "getWaterUndergroundCreatureSpawnLimit");
    }

    @Override
    public int getAmbientSpawnLimit() {
        throw NotImplementedException.createByLazy(Server.class, "getAmbientSpawnLimit");
    }

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
        throw NotImplementedException.createByLazy(Server.class, "getSpawnLimit", SpawnCategory.class);
    }

    @Override
    public boolean isPrimaryThread() {
        return this.spongeServer().onMainThread();
    }

    @Override
    public @NotNull Component motd() {
        throw NotImplementedException.createByLazy(Server.class, "motd");
    }

    @Deprecated
    @Override
    public @NotNull String getMotd() {
        return SoakMessageMap.mapToBukkit(this.motd());
    }

    @Override
    public Component shutdownMessage() {
        throw NotImplementedException.createByLazy(Server.class, "shutdownMessage");
    }

    @Deprecated
    @Override
    public String getShutdownMessage() {
        throw NotImplementedException.createByLazy(Server.class, "getShutdownMessage");
    }

    @Override
    public Warning.@NotNull WarningState getWarningState() {
        throw NotImplementedException.createByLazy(Server.class, "getWarningState");
    }

    @Override
    public @NotNull ItemFactory getItemFactory() {
        return this.itemFactory.get();
    }

    @Override
    public @NotNull ScoreboardManager getScoreboardManager() {
        throw NotImplementedException.createByLazy(Server.class, "getScoreboardManager");
    }

    @Override
    public @NotNull Criteria getScoreboardCriteria(@NotNull String s) {
        throw NotImplementedException.createByLazy(Server.class, "getScoreboardCriteria", String.class);
    }

    @Override
    public CachedServerIcon getServerIcon() {
        throw NotImplementedException.createByLazy(Server.class, "getServerIcon");
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull BufferedImage arg0) {
        throw NotImplementedException.createByLazy(Server.class, "loadServerIcon", BufferedImage.class);
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull File arg0) {
        throw NotImplementedException.createByLazy(Server.class, "loadServerIcon", File.class);
    }

    @Override
    public int getIdleTimeout() {
        throw NotImplementedException.createByLazy(Server.class, "getIdleTimeout");
    }

    @Override
    public void setIdleTimeout(int arg0) {
        throw NotImplementedException.createByLazy(Server.class, "setIdleTimeout", int.class);
    }

    @Override
    public ChunkGenerator.@NotNull ChunkData createChunkData(@NotNull World arg0) {
        throw NotImplementedException.createByLazy(Server.class, "createChunkData", World.class);
    }

    @Override
    public ChunkGenerator.@NotNull ChunkData createVanillaChunkData(@NotNull World arg0, int arg1, int arg2) {
        throw NotImplementedException.createByLazy(Server.class,
                "createVanillaChunkData",
                World.class,
                int.class,
                int.class);
    }

    @Override
    public @NotNull KeyedBossBar createBossBar(@NotNull NamespacedKey arg0, String arg1, @NotNull BarColor arg2, @NotNull BarStyle arg3, BarFlag[] arg4) {
        throw NotImplementedException.createByLazy(Server.class,
                "createBossBar",
                NamespacedKey.class,
                String.class,
                BarColor.class,
                BarStyle.class,
                BarFlag[].class);
    }

    @Override
    public @NotNull BossBar createBossBar(String arg0, @NotNull BarColor arg1, @NotNull BarStyle arg2, BarFlag[] arg3) {
        throw NotImplementedException.createByLazy(Server.class,
                "createBossBar",
                String.class,
                BarColor.class,
                BarStyle.class,
                BarFlag[].class);
    }

    @Override
    public @NotNull Iterator<KeyedBossBar> getBossBars() {
        throw NotImplementedException.createByLazy(Server.class, "getBossBars");
    }

    @Override
    public KeyedBossBar getBossBar(@NotNull NamespacedKey arg0) {
        throw NotImplementedException.createByLazy(Server.class, "getBossBar", NamespacedKey.class);
    }

    @Override
    public boolean removeBossBar(@NotNull NamespacedKey arg0) {
        throw NotImplementedException.createByLazy(Server.class, "removeBossBar", NamespacedKey.class);
    }

    @Override
    public Entity getEntity(@NotNull UUID arg0) {
        throw NotImplementedException.createByLazy(Server.class, "getEntity", UUID.class);
    }

    @Override
    public double[] getTPS() {
        throw NotImplementedException.createByLazy(Server.class, "getTPS");
    }

    @Override
    public long[] getTickTimes() {
        throw NotImplementedException.createByLazy(Server.class, "getTickTimes");
    }

    @Override
    public double getAverageTickTime() {
        throw NotImplementedException.createByLazy(Server.class, "getAverageTickTime");
    }

    @Override
    public @NotNull CommandMap getCommandMap() {
        throw NotImplementedException.createByLazy(Server.class, "getCommandMap");
    }

    @Override
    public Advancement getAdvancement(@NotNull NamespacedKey arg0) {
        throw NotImplementedException.createByLazy(Server.class, "getAdvancement", NamespacedKey.class);
    }

    @Override
    public @NotNull Iterator<Advancement> advancementIterator() {
        throw NotImplementedException.createByLazy(Server.class, "advancementIterator");
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Server.class, "createBlockData", String.class);
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull Material arg0) {
        return arg0.createBlockData();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull Material arg0, Consumer arg1) {
        throw NotImplementedException.createByLazy(Server.class, "createBlockData", Material.class, Consumer.class);
    }

    @Override
    public @NotNull BlockData createBlockData(Material arg0, String arg1) {
        throw NotImplementedException.createByLazy(Server.class, "createBlockData", Material.class, String.class);
    }

    @Override
    public @NotNull <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String registry, @NotNull Class<T> clazz) {
        throw NotImplementedException.createByLazy(Server.class, "getTags", String.class, Class.class);
    }

    @Override
    public LootTable getLootTable(@NotNull NamespacedKey arg0) {
        throw NotImplementedException.createByLazy(Server.class, "getLootTable", NamespacedKey.class);
    }

    @Override
    public @NotNull List<Entity> selectEntities(@NotNull CommandSender sender, @NotNull String selector) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(Server.class, "selectEntities", CommandSender.class, String.class);
    }

    @Override
    public @NotNull StructureManager getStructureManager() {
        throw NotImplementedException.createByLazy(Server.class, "getStructureManager");
    }

    @Override
    public @Nullable <T extends Keyed> Registry<T> getRegistry(@NotNull Class<T> aClass) {
        throw NotImplementedException.createByLazy(Server.class, "getRegistry", Class.class);
    }

    @Override
    public void reloadPermissions() {
        throw NotImplementedException.createByLazy(Server.class, "reloadPermissions");
    }

    @Override
    public boolean reloadCommandAliases() {
        throw NotImplementedException.createByLazy(Server.class, "reloadCommandAliases");
    }

    @Override
    public boolean suggestPlayerNamesWhenNullTabCompletions() {
        throw NotImplementedException.createByLazy(Server.class, "suggestPlayerNamesWhenNullTabCompletions");
    }

    @Override
    public @NotNull String getPermissionMessage() {
        throw NotImplementedException.createByLazy(Server.class, "getPermissionMessage");
    }

    @Override
    public @NotNull Component permissionMessage() {
        throw NotImplementedException.createByLazy(Server.class, "permissionMessage");
    }

    @Override
    public @NotNull PlayerProfile createProfile(@NotNull UUID id) {
        var profileManager = Sponge.server().gameProfileManager();
        var opProfile = profileManager.cache().findById(id).map(profile -> new SoakPlayerProfile(profile, true));
        if (opProfile.isPresent()) {
            return opProfile.get();
        }
        try {
            //blocking -> need to fix this
            return profileManager.uncached()
                    .profile(id).thenApply(profile -> new SoakPlayerProfile(profile, false)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        if (uuid == null) {
            if (name == null) {
                throw new IllegalArgumentException("Id or name must be provided");
            }
            return createProfile(name);
        }
        var profileManager = Sponge.server().gameProfileManager();
        var opProfile = profileManager.cache().findById(uuid).map(profile -> name == null ? profile : profile.withName(name)).map(profile -> new SoakPlayerProfile(profile, true));
        if (opProfile.isPresent()) {
            return opProfile.get();
        }
        try {
            //blocking -> need to fix this
            return profileManager.uncached()
                    .profile(uuid).thenApply(profile -> new SoakPlayerProfile(name == null ? profile : profile.withName(name), false)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull PlayerProfile createProfileExact(@Nullable UUID uuid, @Nullable String s) {
        return this.createProfile(uuid, s);
    }

    @Override
    public @NotNull PlayerProfile createProfile(@NotNull String name) {
        var profileManager = Sponge.server().gameProfileManager();
        var opProfile = profileManager.cache().findByName(name).map(profile -> new SoakPlayerProfile(profile, true));
        if (opProfile.isPresent()) {
            return opProfile.get();
        }
        try {
            //blocking -> need to fix this
            return profileManager.uncached()
                    .profile(name).thenApply(profile -> new SoakPlayerProfile(profile, false)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCurrentTick() {
        throw NotImplementedException.createByLazy(Server.class, "getCurrentTick");
    }

    @Override
    public boolean isStopping() {
        throw NotImplementedException.createByLazy(Server.class, "isStopping");
    }

    @Override
    public @NotNull MobGoals getMobGoals() {
        throw NotImplementedException.createByLazy(Server.class, "getMobGoals");
    }

    @Override
    public @NotNull DatapackManager getDatapackManager() {
        throw NotImplementedException.createByLazy(Server.class, "getDatapackManager");
    }

    @Override
    public @NotNull PotionBrewer getPotionBrewer() {
        throw NotImplementedException.createByLazy(Server.class, "getPotionBrewer");
    }

    @Override
    public boolean hasWhitelist() {
        throw NotImplementedException.createByLazy(Server.class, "hasWhitelist");
    }

    @Override
    public void shutdown() {
        this.spongeServer().shutdown();
    }

    @Deprecated
    @Override
    public @NotNull UnsafeValues getUnsafe() {
        return this.unsafeValues.get();
    }

    @Override
    public @NotNull java.util.logging.Logger getLogger() {
        return this.logger;
    }

    @Deprecated
    @Override
    public int broadcast(@NotNull String arg0, @NotNull String arg1) {
        return broadcast(SoakMessageMap.toComponent(arg0), arg1);
    }

    @Override
    public int broadcast(@NotNull Component arg0, @NotNull String arg1) {
        throw NotImplementedException.createByLazy(Server.class, "broadcast", Component.class, String.class);
    }

    @Override
    public int broadcast(@NotNull Component message) {
        Audience audience = this.spongeServer().broadcastAudience();
        Unfinal<Integer> count = new Unfinal<>(0);
        audience.forEachAudience((au) -> count.set(count.get() + 1));
        audience.sendMessage(message);
        return count.get();
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return this.spongeServer().audiences();
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte[] message) {
        throw NotImplementedException.createByLazy(Server.class,
                "sendPluginMessage",
                Plugin.class,
                String.class,
                byte[].class);
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        throw NotImplementedException.createByLazy(Server.class, "getListeningPluginChannels");
    }
}
