package org.soak.wrapper.world;

import com.destroystokyo.paper.HeightmapType;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakResourceKeyMap;
import org.soak.map.SoakWorldTypeMap;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.utils.single.SoakSingleInstance;
import org.soak.wrapper.block.SoakBlock;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class SoakWorld implements World, CraftWorld, SoakSingleInstance<org.spongepowered.api.world.server.ServerWorld> {

    private final ResourceKey key;
    private org.spongepowered.api.world.server.ServerWorld world;

    public SoakWorld(org.spongepowered.api.world.server.ServerWorld world) {
        this.world = world;
        this.key = world.key();
    }

    public org.spongepowered.api.world.server.ServerWorld sponge() {
        return this.world;
    }

    @Override
    public int getEntityCount() {
        return this.world.entities().size();
    }

    @Override
    public int getTileEntityCount() {
        return this.world.blockEntities().size();
    }

    @Override
    public int getTickableTileEntityCount() {
        return getTileEntityCount(); //need to filter
    }

    @Override
    public int getChunkCount() {
        return (int) StreamSupport.stream(this.world.loadedChunks().spliterator(), false).count();
    }

    @Override
    public int getPlayerCount() {
        return this.world.players().size();
    }

    @Override
    public long getTime() {
        return this.world.properties().dayTime().asTicks().ticks();
    }

    @Override
    public void setTime(long arg0) {
        this.world.properties().setDayTime(MinecraftDayTime.of(Sponge.server(), Ticks.of(arg0)));
    }

    @Override
    public @NotNull Block getBlockAt(@NotNull Location arg0) {
        return this.getBlockAt(arg0.getBlockX(), arg0.getBlockY(), arg0.getBlockZ());
    }

    @Override
    public @NotNull Block getBlockAt(int arg0, int arg1, int arg2) {
        return new SoakBlock(this.sponge().location(arg0, arg1, arg2));
    }

    @Override
    public int getHighestBlockYAt(int arg0, int arg1) {
        return this.sponge().highestYAt(arg0, arg1);
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location arg0) {
        return this.sponge().highestPositionAt(new Vector3i(arg0.getBlockX(), arg0.getBlockY(), arg0.getBlockZ())).y();
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location arg0, @NotNull HeightMap arg1) {
        throw NotImplementedException.createByLazy(World.class, "getHighestBlockYAt", Location.class, HeightMap.class);
    }

    @Override
    public int getHighestBlockYAt(int arg0, int arg1, @NotNull HeightMap arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "getHighestBlockYAt",
                int.class,
                int.class,
                HeightMap.class);
    }

    @Deprecated
    @Override
    public int getHighestBlockYAt(int arg0, int arg1, HeightmapType arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "getHighestBlockYAt",
                int.class,
                int.class,
                HeightmapType.class);
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(World.class, "getHighestBlockAt", Location.class);
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location arg0, @NotNull HeightMap arg1) {
        throw NotImplementedException.createByLazy(World.class, "getHighestBlockAt", Location.class, HeightMap.class);
    }

    @Override
    public @NotNull Block getHighestBlockAt(int arg0, int arg1, @NotNull HeightMap arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "getHighestBlockAt",
                int.class,
                int.class,
                HeightMap.class);
    }

    @Override
    public @NotNull Block getHighestBlockAt(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "getHighestBlockAt", int.class, int.class);
    }

    @Override
    public @NotNull Chunk getChunkAt(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "getChunkAt", int.class, int.class);
    }

    @Override
    public @NotNull Chunk getChunkAt(int i, int i1, boolean b) {
        throw NotImplementedException.createByLazy(World.class, "getChunkAt", int.class, int.class, boolean.class);
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(World.class, "getChunkAt", Location.class);
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Block arg0) {
        throw NotImplementedException.createByLazy(World.class, "getChunkAt", Block.class);
    }

    @Override
    public boolean isChunkGenerated(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "isChunkGenerated", int.class, int.class);
    }

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen, boolean urgent) {
        throw NotImplementedException.createByLazy(World.class,
                "getChunkAtAsync",
                int.class,
                int.class,
                boolean.class,
                boolean.class);
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<Entity> filter) {
        throw NotImplementedException.createByLazy(World.class,
                "getNearbyEntities",
                Location.class,
                double.class,
                double.class,
                double.class,
                Predicate.class);
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
        throw NotImplementedException.createByLazy(World.class,
                "getNearbyEntities",
                Location.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
        throw NotImplementedException.createByLazy(World.class, "getNearbyEntities", BoundingBox.class);
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<Entity> filter) {
        throw NotImplementedException.createByLazy(World.class,
                "getNearbyEntities",
                BoundingBox.class,
                Predicate.class);
    }

    @Override
    public @NotNull List<Player> getPlayers() {
        throw NotImplementedException.createByLazy(World.class, "getPlayers");
    }

    @Override
    public boolean createExplosion(Entity arg0, @NotNull Location arg1, float arg2, boolean arg3, boolean arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                Entity.class,
                Location.class,
                float.class,
                boolean.class,
                boolean.class);
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                double.class,
                double.class,
                double.class,
                float.class);
    }

    @Override
    public boolean createExplosion(@NotNull Location arg0, float arg1, boolean arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                Location.class,
                float.class,
                boolean.class);
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4, boolean arg5) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                double.class,
                double.class,
                double.class,
                float.class,
                boolean.class,
                boolean.class);
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                double.class,
                double.class,
                double.class,
                float.class,
                boolean.class);
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4, boolean arg5, Entity arg6) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                double.class,
                double.class,
                double.class,
                float.class,
                boolean.class,
                boolean.class,
                Entity.class);
    }

    @Override
    public boolean createExplosion(@NotNull Location arg0, float arg1) {
        throw NotImplementedException.createByLazy(World.class, "createExplosion", Location.class, float.class);
    }

    @Override
    public boolean createExplosion(@NotNull Location arg0, float arg1, boolean arg2, boolean arg3, Entity arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                Location.class,
                float.class,
                boolean.class,
                boolean.class,
                Entity.class);
    }

    @Override
    public boolean createExplosion(@NotNull Location arg0, float arg1, boolean arg2, boolean arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "createExplosion",
                Location.class,
                float.class,
                boolean.class,
                boolean.class);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {
        return spawn(location, clazz, (e) -> {
        }, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function, @NotNull CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        var bukkitType = Arrays.stream(EntityType.values())
                .filter(type -> type.getEntityClass().getName().equals(clazz.getName()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("EntityType could not be found for class " + clazz.getName()));
        var spongeEntity = this.sponge()
                .createEntity(bukkitType.asSponge(), new Vector3d(location.getX(), location.getY(), location.getZ()));
        var bukkitEntity = (T) AbstractEntity.wrap(spongeEntity);
        function.accept(bukkitEntity);
        this.sponge().spawnEntity(spongeEntity);
        return bukkitEntity;
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass, boolean b, @Nullable Consumer<T> consumer) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(World.class, "spawn", Location.class, Class.class, boolean.class, Consumer.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, List arg1, Player arg2, double arg3, double arg4, double arg5, int arg6, double arg7, double arg8, double arg9, double arg10, Object arg11, boolean arg12) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                List.class,
                Player.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class,
                Object.class,
                boolean.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, Object arg7, boolean arg8) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class,
                Object.class,
                boolean.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5, Object arg6) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, Object arg5) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, Object arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, Object arg7) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, Object arg8) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8, Object arg9, boolean arg10) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class,
                Object.class,
                boolean.class);
    }

    @Override
    public @NotNull MoonPhase getMoonPhase() {
        throw NotImplementedException.createByLazy(World.class, "getMoonPhase");
    }

    @Override
    public boolean lineOfSightExists(@NotNull Location arg0, @NotNull Location arg1) {
        throw NotImplementedException.createByLazy(World.class, "lineOfSightExists", Location.class, Location.class);
    }

    @Override
    public boolean hasCollisionsIn(@NotNull BoundingBox boundingBox) {
        return false;
    }

    @Override
    public boolean isChunkLoaded(@NotNull Chunk arg0) {
        throw NotImplementedException.createByLazy(World.class, "isChunkLoaded", Chunk.class);
    }

    @Override
    public boolean isChunkLoaded(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "isChunkLoaded", int.class, int.class);
    }

    @Override
    public Chunk[] getLoadedChunks() {
        throw NotImplementedException.createByLazy(World.class, "getLoadedChunks");
    }

    @Override
    public void loadChunk(@NotNull Chunk arg0) {
        throw NotImplementedException.createByLazy(World.class, "loadChunk", Chunk.class);
    }

    @Override
    public void loadChunk(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "loadChunk", int.class, int.class);
    }

    @Override
    public boolean loadChunk(int arg0, int arg1, boolean arg2) {
        throw NotImplementedException.createByLazy(World.class, "loadChunk", int.class, int.class, boolean.class);
    }

    @Deprecated
    @Override
    public boolean isChunkInUse(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "isChunkInUse", int.class, int.class);
    }

    @Override
    public boolean unloadChunk(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "unloadChunk", int.class, int.class);
    }

    @Override
    public boolean unloadChunk(int arg0, int arg1, boolean arg2) {
        throw NotImplementedException.createByLazy(World.class, "unloadChunk", int.class, int.class, boolean.class);
    }

    @Override
    public boolean unloadChunk(@NotNull Chunk arg0) {
        throw NotImplementedException.createByLazy(World.class, "unloadChunk", Chunk.class);
    }

    @Override
    public boolean unloadChunkRequest(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "unloadChunkRequest", int.class, int.class);
    }

    @Deprecated
    @Override
    public boolean regenerateChunk(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "regenerateChunk", int.class, int.class);
    }

    @Deprecated
    @Override
    public boolean refreshChunk(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "refreshChunk", int.class, int.class);
    }

    @Override
    public boolean isChunkForceLoaded(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "isChunkForceLoaded", int.class, int.class);
    }

    @Override
    public void setChunkForceLoaded(int arg0, int arg1, boolean arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "setChunkForceLoaded",
                int.class,
                int.class,
                boolean.class);
    }

    @Override
    public @NotNull Collection<Chunk> getForceLoadedChunks() {
        throw NotImplementedException.createByLazy(World.class, "getForceLoadedChunks");
    }

    @Override
    public boolean addPluginChunkTicket(int arg0, int arg1, @NotNull Plugin arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "addPluginChunkTicket",
                int.class,
                int.class,
                Plugin.class);
    }

    @Override
    public boolean removePluginChunkTicket(int arg0, int arg1, @NotNull Plugin arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "removePluginChunkTicket",
                int.class,
                int.class,
                Plugin.class);
    }

    @Override
    public void removePluginChunkTickets(@NotNull Plugin arg0) {
        throw NotImplementedException.createByLazy(World.class, "removePluginChunkTickets", Plugin.class);
    }

    @Override
    public @NotNull Collection<Plugin> getPluginChunkTickets(int x, int z) {
        throw NotImplementedException.createByLazy(World.class, "getPluginChunkTickets", int.class, int.class);
    }

    @Override
    public @NotNull Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        throw NotImplementedException.createByLazy(World.class, "getPluginChunkTickets");
    }

    @Override
    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
        throw NotImplementedException.createByLazy(World.class, "dropItem", Location.class, ItemStack.class);
    }

    @Override
    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<Item> function) {
        throw NotImplementedException.createByLazy(World.class,
                "dropItem",
                Location.class,
                ItemStack.class,
                Consumer.class);
    }

    @Override
    public @NotNull Item dropItemNaturally(@NotNull Location arg0, @NotNull ItemStack arg1) {
        throw NotImplementedException.createByLazy(World.class, "dropItemNaturally", Location.class, ItemStack.class);
    }

    @Override
    public @NotNull Item dropItemNaturally(@NotNull Location arg0, @NotNull ItemStack arg1, Consumer arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "dropItemNaturally",
                Location.class,
                ItemStack.class,
                Consumer.class);
    }

    @Override
    public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread, @NotNull Class<T> clazz) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnArrow",
                Location.class,
                Vector.class,
                float.class,
                float.class,
                Class.class);
    }

    @Override
    public @NotNull Arrow spawnArrow(@NotNull Location arg0, @NotNull Vector arg1, float arg2, float arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnArrow",
                Location.class,
                Vector.class,
                float.class,
                float.class);
    }

    @Override
    public boolean generateTree(@NotNull Location arg0, @NotNull TreeType arg1, @NotNull BlockChangeDelegate arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "generateTree",
                Location.class,
                TreeType.class,
                BlockChangeDelegate.class);
    }

    @Override
    public boolean generateTree(@NotNull Location arg0, @NotNull TreeType arg1) {
        throw NotImplementedException.createByLazy(World.class, "generateTree", Location.class, TreeType.class);
    }

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location arg0, @NotNull EntityType arg1) {
        var type = arg1.asSponge();
        var entity = this.world.createEntity(type, new Vector3d(arg0.getX(), arg0.getY(), arg0.getZ()));
        this.world.spawnEntity(entity);
        return AbstractEntity.wrap(entity);
    }

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType, boolean b) {
        throw NotImplementedException.createByLazy(World.class, "spawnEntity", Location.class, EntityType.class, boolean.class);
    }

    @Override
    public @NotNull LightningStrike strikeLightning(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(World.class, "strikeLightning", Location.class);
    }

    @Override
    public @NotNull LightningStrike strikeLightningEffect(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(World.class, "strikeLightningEffect", Location.class);
    }

    @Override
    public @Nullable Location findLightningRod(@NotNull Location location) {
        throw NotImplementedException.createByLazy(World.class, "findLightningRod", Location.class);
    }

    @Override
    public @Nullable Location findLightningTarget(@NotNull Location location) {
        throw NotImplementedException.createByLazy(World.class, "findLightningTarget", Location.class);
    }

    @Override
    public @NotNull List<Entity> getEntities() {
        throw NotImplementedException.createByLazy(World.class, "getEntities");
    }

    @Override
    public @NotNull List<LivingEntity> getLivingEntities() {
        throw NotImplementedException.createByLazy(World.class, "getLivingEntities");
    }

    @Override
    public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {
        throw NotImplementedException.createByLazy(World.class, "getEntitiesByClass", Class.class);
    }

    @SafeVarargs
    @Override
    public final @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
        throw NotImplementedException.createByLazy(World.class, "getEntitiesByClass", Class[].class);
    }

    @Override
    public @NotNull Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
        throw NotImplementedException.createByLazy(World.class, "getEntitiesByClasses", Class[].class);
    }

    @Override
    public Entity getEntity(@NotNull UUID arg0) {
        throw NotImplementedException.createByLazy(World.class, "getEntity", UUID.class);
    }

    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location arg0, @NotNull Vector arg1, double arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTraceEntities",
                Location.class,
                Vector.class,
                double.class);
    }

    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location arg0, @NotNull Vector arg1, double arg2, double arg3, Predicate arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTraceEntities",
                Location.class,
                Vector.class,
                double.class,
                double.class,
                Predicate.class);
    }

    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location arg0, @NotNull Vector arg1, double arg2, Predicate arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTraceEntities",
                Location.class,
                Vector.class,
                double.class,
                Predicate.class);
    }

    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location arg0, @NotNull Vector arg1, double arg2, double arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTraceEntities",
                Location.class,
                Vector.class,
                double.class,
                double.class);
    }

    @Override
    public RayTraceResult rayTraceBlocks(@NotNull Location arg0, @NotNull Vector arg1, double arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTraceBlocks",
                Location.class,
                Vector.class,
                double.class);
    }

    @Override
    public RayTraceResult rayTraceBlocks(@NotNull Location arg0, @NotNull Vector arg1, double arg2, @NotNull FluidCollisionMode arg3, boolean arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTraceBlocks",
                Location.class,
                Vector.class,
                double.class,
                FluidCollisionMode.class,
                boolean.class);
    }

    @Override
    public RayTraceResult rayTraceBlocks(@NotNull Location arg0, @NotNull Vector arg1, double arg2, @NotNull FluidCollisionMode arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTraceBlocks",
                Location.class,
                Vector.class,
                double.class,
                FluidCollisionMode.class);
    }

    @Override
    public RayTraceResult rayTrace(@NotNull Location arg0, @NotNull Vector arg1, double arg2, @NotNull FluidCollisionMode arg3, boolean arg4, double arg5, Predicate arg6) {
        throw NotImplementedException.createByLazy(World.class,
                "rayTrace",
                Location.class,
                Vector.class,
                double.class,
                FluidCollisionMode.class,
                boolean.class,
                double.class,
                Predicate.class);
    }

    @Override
    public @NotNull UUID getUID() {
        return this.world.uniqueId();
    }

    @Override
    public @NotNull Location getSpawnLocation() {
        var spawnLocation = this.sponge().properties().spawnPosition();
        return new Location(this, spawnLocation.x(), spawnLocation.y(), spawnLocation.z());
    }

    @Override
    public boolean setSpawnLocation(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(World.class, "setSpawnLocation", Location.class);
    }

    @Override
    public boolean setSpawnLocation(int arg0, int arg1, int arg2) {
        throw NotImplementedException.createByLazy(World.class, "setSpawnLocation", int.class, int.class, int.class);
    }

    @Override
    public boolean setSpawnLocation(int arg0, int arg1, int arg2, float arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "setSpawnLocation",
                int.class,
                int.class,
                int.class,
                float.class);
    }

    @Override
    public long getFullTime() {
        throw NotImplementedException.createByLazy(World.class, "getFullTime");
    }

    @Override
    public void setFullTime(long arg0) {
        throw NotImplementedException.createByLazy(World.class, "setFullTime", long.class);
    }

    @Override
    public boolean isDayTime() {
        throw NotImplementedException.createByLazy(World.class, "isDayTime");
    }

    @Override
    public long getGameTime() {
        throw NotImplementedException.createByLazy(World.class, "getGameTime");
    }

    @Override
    public boolean hasStorm() {
        throw NotImplementedException.createByLazy(World.class, "hasStorm");
    }

    @Override
    public void setStorm(boolean arg0) {
        throw NotImplementedException.createByLazy(World.class, "setStorm", boolean.class);
    }

    @Override
    public int getWeatherDuration() {
        throw NotImplementedException.createByLazy(World.class, "getWeatherDuration");
    }

    @Override
    public void setWeatherDuration(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setWeatherDuration", int.class);
    }

    @Override
    public boolean isThundering() {
        throw NotImplementedException.createByLazy(World.class, "isThundering");
    }

    @Override
    public void setThundering(boolean arg0) {
        throw NotImplementedException.createByLazy(World.class, "setThundering", boolean.class);
    }

    @Override
    public int getThunderDuration() {
        throw NotImplementedException.createByLazy(World.class, "getThunderDuration");
    }

    @Override
    public void setThunderDuration(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setThunderDuration", int.class);
    }

    @Override
    public boolean isClearWeather() {
        throw NotImplementedException.createByLazy(World.class, "isClearWeather");
    }

    @Override
    public int getClearWeatherDuration() {
        throw NotImplementedException.createByLazy(World.class, "getClearWeatherDuration");
    }

    @Override
    public void setClearWeatherDuration(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setClearWeatherDuration", int.class);
    }

    @Override
    public @NotNull Environment getEnvironment() {
        return SoakWorldTypeMap.toBukkit(this.sponge().worldType());
    }

    @Override
    public long getSeed() {
        return this.world.seed();
    }

    @Override
    public boolean getPVP() {
        return this.world.properties().pvp();
    }

    @Override
    public void setPVP(boolean arg0) {
        this.world.properties().setPvp(arg0);
    }

    @Override
    public ChunkGenerator getGenerator() {
        throw NotImplementedException.createByLazy(World.class, "getGenerator");
    }

    @Override
    public @Nullable BiomeProvider getBiomeProvider() {
        throw NotImplementedException.createByLazy(World.class, "getBiomeProvider");
    }

    @Override
    public @NotNull List<BlockPopulator> getPopulators() {
        throw NotImplementedException.createByLazy(World.class, "getPopulators");
    }

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location arg0, @NotNull BlockData arg1) {
        throw NotImplementedException.createByLazy(World.class, "spawnFallingBlock", Location.class, BlockData.class);
    }

    @Override
    @Deprecated
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location arg0, @NotNull MaterialData arg1) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnFallingBlock",
                Location.class,
                MaterialData.class);
    }

    @Deprecated
    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location arg0, @NotNull Material arg1, byte arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "spawnFallingBlock",
                Location.class,
                Material.class,
                byte.class);
    }

    @Override
    public void playEffect(@NotNull Location arg0, @NotNull Effect arg1, int arg2) {
        throw NotImplementedException.createByLazy(World.class, "playEffect", Location.class, Effect.class, int.class);
    }

    @Override
    public void playEffect(@NotNull Location arg0, @NotNull Effect arg1, Object arg2, int arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "playEffect",
                Location.class,
                Effect.class,
                Object.class,
                int.class);
    }

    @Override
    public void playEffect(@NotNull Location arg0, @NotNull Effect arg1, Object arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "playEffect",
                Location.class,
                Effect.class,
                Object.class);
    }

    @Override
    public void playEffect(@NotNull Location arg0, @NotNull Effect arg1, int arg2, int arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "playEffect",
                Location.class,
                Effect.class,
                int.class,
                int.class);
    }

    @Override
    public @NotNull ChunkSnapshot getEmptyChunkSnapshot(int arg0, int arg1, boolean arg2, boolean arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "getEmptyChunkSnapshot",
                int.class,
                int.class,
                boolean.class,
                boolean.class);
    }

    @Override
    public void setSpawnFlags(boolean arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(World.class, "setSpawnFlags", boolean.class, boolean.class);
    }

    @Override
    public boolean getAllowAnimals() {
        throw NotImplementedException.createByLazy(World.class, "getAllowAnimals");
    }

    @Override
    public boolean getAllowMonsters() {
        throw NotImplementedException.createByLazy(World.class, "getAllowMonsters");
    }

    @Override
    public @NotNull Biome getBiome(@NotNull Location location) {
        throw NotImplementedException.createByLazy(World.class, "getBiome", Location.class);
    }

    @Override
    public @NotNull Biome getBiome(int arg0, int arg1, int arg2) {
        throw NotImplementedException.createByLazy(World.class, "getBiome", int.class, int.class, int.class);
    }

    @Override
    public @NotNull Biome getComputedBiome(int i, int i1, int i2) {
        throw NotImplementedException.createByLazy(World.class, "getBiome", int.class, int.class, int.class);
    }

    @Override
    public void setBiome(@NotNull Location location, @NotNull Biome biome) {
        throw NotImplementedException.createByLazy(World.class, "setBiome", Location.class, Biome.class);
    }

    @Deprecated
    @Override
    public @NotNull Biome getBiome(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "getBiome", int.class, int.class);
    }

    @Override
    public void setBiome(int arg0, int arg1, int arg2, @NotNull Biome arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "setBiome",
                int.class,
                int.class,
                int.class,
                Biome.class);
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull Location location) {
        return getBlockState(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public @NotNull BlockState getBlockState(int i, int i1, int i2) {
        throw NotImplementedException.createByLazy(World.class, "getBlockState", int.class, int.class, int.class);
    }

    @Override
    public @NotNull BlockData getBlockData(@NotNull Location location) {
        return getBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public @NotNull BlockData getBlockData(int i, int i1, int i2) {
        throw NotImplementedException.createByLazy(World.class, "getBlockData", int.class, int.class, int.class);
    }

    @Override
    public @NotNull Material getType(@NotNull Location location) {
        return getType(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public @NotNull Material getType(int i, int i1, int i2) {
        throw NotImplementedException.createByLazy(World.class, "getType", int.class, int.class, int.class);

    }

    @Override
    public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
        setBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockData);
    }

    @Override
    public void setBlockData(int i, int i1, int i2, @NotNull BlockData blockData) {
        throw NotImplementedException.createByLazy(World.class, "setBlockData", int.class, int.class, int.class, BlockData.class);
    }

    @Override
    public void setType(@NotNull Location location, @NotNull Material material) {
        setType(location.getBlockX(), location.getBlockY(), location.getBlockZ(), material);
    }

    @Override
    public void setType(int i, int i1, int i2, @NotNull Material material) {
        throw NotImplementedException.createByLazy(World.class, "setType", int.class, int.class, int.class, Material.class);

    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType) {
        throw NotImplementedException.createByLazy(World.class, "generateTree", Location.class, Random.class, TreeType.class);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Consumer<BlockState> consumer) {
        throw NotImplementedException.createByLazy(World.class, "generateTree", Location.class, Random.class, TreeType.class, Consumer.class);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Predicate<BlockState> predicate) {
        throw NotImplementedException.createByLazy(World.class, "generateTree", Location.class, Random.class, TreeType.class, Predicate.class);
    }

    @Deprecated
    @Override
    public void setBiome(int arg0, int arg1, @NotNull Biome arg2) {
        throw NotImplementedException.createByLazy(World.class, "setBiome", int.class, int.class, Biome.class);
    }

    @Override
    public double getTemperature(int arg0, int arg1, int arg2) {
        throw NotImplementedException.createByLazy(World.class, "getTemperature", int.class, int.class, int.class);
    }

    @Deprecated
    @Override
    public double getTemperature(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "getTemperature", int.class, int.class);
    }

    @Deprecated
    @Override
    public double getHumidity(int arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "getHumidity", int.class, int.class);
    }

    @Override
    public double getHumidity(int arg0, int arg1, int arg2) {
        throw NotImplementedException.createByLazy(World.class, "getHumidity", int.class, int.class, int.class);
    }

    @Override
    public int getLogicalHeight() {
        return this.world.height();
    }

    @Override
    public int getMinHeight() {
        throw NotImplementedException.createByLazy(World.class, "getMinHeight");
    }

    @Override
    public int getMaxHeight() {
        throw NotImplementedException.createByLazy(World.class, "getMaxHeight");
    }

    @Override
    public @NotNull BiomeProvider vanillaBiomeProvider() {
        throw NotImplementedException.createByLazy(World.class, "vanillaBiomeProvider");
    }

    @Override
    public int getSeaLevel() {
        throw NotImplementedException.createByLazy(World.class, "getSeaLevel");
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        throw NotImplementedException.createByLazy(World.class, "getKeepSpawnInMemory");
    }

    @Override
    public void setKeepSpawnInMemory(boolean arg0) {
        throw NotImplementedException.createByLazy(World.class, "setKeepSpawnInMemory", boolean.class);
    }

    @Override
    public boolean isAutoSave() {
        throw NotImplementedException.createByLazy(World.class, "isAutoSave");
    }

    @Override
    public void setAutoSave(boolean arg0) {
        throw NotImplementedException.createByLazy(World.class, "setAutoSave", boolean.class);
    }

    @Override
    public @NotNull Difficulty getDifficulty() {
        throw NotImplementedException.createByLazy(World.class, "getDifficulty");
    }

    @Override
    public void setDifficulty(@NotNull Difficulty arg0) {
        throw NotImplementedException.createByLazy(World.class, "setDifficulty", Difficulty.class);
    }

    @Override
    public @NotNull File getWorldFolder() {
        throw NotImplementedException.createByLazy(World.class, "getWorldFolder");
    }

    @Deprecated
    @Override
    public WorldType getWorldType() {
        throw NotImplementedException.createByLazy(World.class, "getWorldType");
    }

    @Override
    public boolean canGenerateStructures() {
        throw NotImplementedException.createByLazy(World.class, "canGenerateStructures");
    }

    @Override
    public boolean isHardcore() {
        throw NotImplementedException.createByLazy(World.class, "isHardcore");
    }

    @Override
    public void setHardcore(boolean arg0) {
        throw NotImplementedException.createByLazy(World.class, "setHardcore", boolean.class);
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        throw NotImplementedException.createByLazy(World.class, "getTicksPerAnimalSpawns");
    }

    @Override
    public void setTicksPerAnimalSpawns(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setTicksPerAnimalSpawns", int.class);
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        throw NotImplementedException.createByLazy(World.class, "getTicksPerMonsterSpawns");
    }

    @Override
    public void setTicksPerMonsterSpawns(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setTicksPerMonsterSpawns", int.class);
    }

    @Override
    public long getTicksPerWaterSpawns() {
        throw NotImplementedException.createByLazy(World.class, "getTicksPerWaterSpawns");
    }

    @Override
    public void setTicksPerWaterSpawns(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setTicksPerWaterSpawns", int.class);
    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
        throw NotImplementedException.createByLazy(World.class, "getTicksPerWaterAmbientSpawns");
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setTicksPerWaterAmbientSpawns", int.class);
    }

    @Override
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        throw NotImplementedException.createByLazy(World.class, "getTickPerWaterUndergroundCreatureSpawns");
    }

    @Override
    public void setTicksPerWaterUndergroundCreatureSpawns(int i) {
        throw NotImplementedException.createByLazy(World.class, "setTickPerWaterUndergroundCreatureSpawns", int.class);

    }

    @Override
    public long getTicksPerAmbientSpawns() {
        throw NotImplementedException.createByLazy(World.class, "getTicksPerAmbientSpawns");
    }

    @Override
    public void setTicksPerAmbientSpawns(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setTicksPerAmbientSpawns", int.class);
    }

    @Override
    public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
        throw NotImplementedException.createByLazy(World.class, "getTicksPerSpawns", SpawnCategory.class);
    }

    @Override
    public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int i) {
        throw NotImplementedException.createByLazy(World.class, "setTicksPerSpawns", SpawnCategory.class, int.class);
    }

    @Override
    public int getMonsterSpawnLimit() {
        throw NotImplementedException.createByLazy(World.class, "getMonsterSpawnLimit");
    }

    @Override
    public void setMonsterSpawnLimit(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setMonsterSpawnLimit", int.class);
    }

    @Override
    public int getAnimalSpawnLimit() {
        throw NotImplementedException.createByLazy(World.class, "getAnimalSpawnLimit");
    }

    @Override
    public void setAnimalSpawnLimit(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setAnimalSpawnLimit", int.class);
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        throw NotImplementedException.createByLazy(World.class, "getWaterAnimalSpawnLimit");
    }

    @Override
    public void setWaterAnimalSpawnLimit(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setWaterAnimalSpawnLimit", int.class);
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterUndergroundCreatureSpawnLimit(int i) {

    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        throw NotImplementedException.createByLazy(World.class, "getWaterAmbientSpawnLimit");
    }

    @Override
    public void setWaterAmbientSpawnLimit(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setWaterAmbientSpawnLimit", int.class);
    }

    @Override
    public int getAmbientSpawnLimit() {
        throw NotImplementedException.createByLazy(World.class, "getAmbientSpawnLimit");
    }

    @Override
    public void setAmbientSpawnLimit(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setAmbientSpawnLimit", int.class);
    }

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
        throw NotImplementedException.createByLazy(World.class, "getSpawnLimit", SpawnCategory.class);
    }

    @Override
    public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int i) {
        throw NotImplementedException.createByLazy(World.class, "setSpawnLimit", SpawnCategory.class, int.class);
    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull String arg1, float arg2, float arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "playSound",
                Location.class,
                String.class,
                float.class,
                float.class);
    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull Sound arg1, @NotNull SoundCategory arg2, float arg3, float arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "playSound",
                Location.class,
                Sound.class,
                SoundCategory.class,
                float.class,
                float.class);
    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull Sound arg1, float arg2, float arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "playSound",
                Location.class,
                Sound.class,
                float.class,
                float.class);
    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull String arg1, @NotNull SoundCategory arg2, float arg3, float arg4) {
        throw NotImplementedException.createByLazy(World.class,
                "playSound",
                Location.class,
                String.class,
                SoundCategory.class,
                float.class,
                float.class);
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {
        throw NotImplementedException.createByLazy(World.class, "playSound", Entity.class, Sound.class, float.class, float.class);
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull String s, float v, float v1) {
        throw NotImplementedException.createByLazy(World.class, "playSound", Entity.class, String.class, float.class, float.class);

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {
        throw NotImplementedException.createByLazy(World.class, "playSound", Entity.class, Sound.class, SoundCategory.class, float.class, float.class);

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v, float v1) {
        throw NotImplementedException.createByLazy(World.class, "playSound", Entity.class, String.class, SoundCategory.class, float.class, float.class);

    }

    @Override
    public String[] getGameRules() {
        throw NotImplementedException.createByLazy(World.class, "getGameRules");
    }

    @Override
    public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> rule) {
        throw NotImplementedException.createByLazy(World.class, "getGameRuleValue", GameRule.class);
    }

    @Deprecated
    @Override
    public String getGameRuleValue(String arg0) {
        throw NotImplementedException.createByLazy(World.class, "getGameRuleValue", String.class);
    }

    @Deprecated
    @Override
    public boolean setGameRuleValue(@NotNull String arg0, @NotNull String arg1) {
        throw NotImplementedException.createByLazy(World.class, "setGameRuleValue", String.class, String.class);
    }

    @Override
    public boolean isGameRule(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(World.class, "isGameRule", String.class);
    }

    @Override
    public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> rule) {
        throw NotImplementedException.createByLazy(World.class, "getGameRuleDefault", GameRule.class);
    }

    @Override
    public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue) {
        throw NotImplementedException.createByLazy(World.class, "setGameRule", GameRule.class, Object.class);
    }

    @Override
    public @NotNull WorldBorder getWorldBorder() {
        throw NotImplementedException.createByLazy(World.class, "getWorldBorder");
    }

    @Override
    public Location locateNearestStructure(@NotNull Location arg0, @NotNull StructureType arg1, int arg2, boolean arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "locateNearestStructure",
                Location.class,
                StructureType.class,
                int.class,
                boolean.class);
    }

    @Override
    public @Nullable StructureSearchResult locateNearestStructure(@NotNull Location location, org.bukkit.generator.structure.@NotNull StructureType structureType, int i, boolean b) {
        throw NotImplementedException.createByLazy(World.class, "locateNearestStructure", Location.class, org.bukkit.generator.structure.StructureType.class, int.class, boolean.class);
    }

    @Override
    public @Nullable StructureSearchResult locateNearestStructure(@NotNull Location location, @NotNull Structure structure, int i, boolean b) {
        throw NotImplementedException.createByLazy(World.class, "locateNearestStructure", Location.class, Structure.class, int.class, boolean.class);
    }

    @Override
    public Location locateNearestBiome(@NotNull Location arg0, @NotNull Biome arg1, int arg2) {
        throw NotImplementedException.createByLazy(World.class,
                "locateNearestBiome",
                Location.class,
                Biome.class,
                int.class);
    }

    @Override
    public Location locateNearestBiome(@NotNull Location arg0, @NotNull Biome arg1, int arg2, int arg3) {
        throw NotImplementedException.createByLazy(World.class,
                "locateNearestBiome",
                Location.class,
                Biome.class,
                int.class,
                int.class);
    }

    @Override
    public boolean isUltrawarm() {
        throw NotImplementedException.createByLazy(World.class, "isUltrawarm");
    }

    @Override
    public boolean isNatural() {
        throw NotImplementedException.createByLazy(World.class, "isNatural");
    }

    @Override
    public boolean isBedWorks() {
        return this.doesBedWork();
    }

    @Override
    public boolean hasSkyLight() {
        return this.hasSkylight();
    }

    @Override
    public boolean hasCeiling() {
        return this.hasBedrockCeiling();
    }

    @Override
    public boolean hasSkylight() {
        throw NotImplementedException.createByLazy(World.class, "hasSkylight");
    }

    @Override
    public boolean hasBedrockCeiling() {
        throw NotImplementedException.createByLazy(World.class, "hasBedrockCeiling");
    }

    @Override
    public boolean isPiglinSafe() {
        throw NotImplementedException.createByLazy(World.class, "isPiglinSafe");
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        return false;
    }

    @Override
    public boolean doesBedWork() {
        throw NotImplementedException.createByLazy(World.class, "doesBedWork");
    }

    @Override
    public boolean hasRaids() {
        throw NotImplementedException.createByLazy(World.class, "hasRaids");
    }

    @Override
    public boolean isUltraWarm() {
        return this.isUltrawarm();
    }

    @Override
    public boolean isFixedTime() {
        throw NotImplementedException.createByLazy(World.class, "isFixedTime");
    }

    @Override
    public @NotNull Collection<Material> getInfiniburn() {
        throw NotImplementedException.createByLazy(World.class, "getInfiniburn");
    }

    @Override
    public void sendGameEvent(@Nullable Entity entity, @NotNull GameEvent gameEvent, @NotNull Vector vector) {

    }

    @Override
    public int getViewDistance() {
        return this.world.properties().viewDistance();
    }

    @Override
    public void setViewDistance(int arg0) {
        this.world.properties().setViewDistance(arg0);
    }

    @Override
    public int getSimulationDistance() {
        throw NotImplementedException.createByLazy(World.class, "getSimulationDistance");
    }

    @Override
    public void setSimulationDistance(int i) {
        throw NotImplementedException.createByLazy(World.class, "setSimulationDistance", int.class);
    }

    @Override
    public int getNoTickViewDistance() {
        throw NotImplementedException.createByLazy(World.class, "getNoTickViewDistance");
    }

    @Override
    public void setNoTickViewDistance(int arg0) {
        throw NotImplementedException.createByLazy(World.class, "setNoTickViewDistance", int.class);
    }

    @Override
    public int getSendViewDistance() {
        throw NotImplementedException.createByLazy(World.class, "getSendViewDistance");
    }

    @Override
    public void setSendViewDistance(int i) {
        throw NotImplementedException.createByLazy(World.class, "setSendViewDistance", int.class);
    }

    @Override
    public @NotNull Spigot spigot() {
        throw NotImplementedException.createByLazy(World.class, "spigot");
    }

    @Override
    public Raid locateNearestRaid(@NotNull Location arg0, int arg1) {
        throw NotImplementedException.createByLazy(World.class, "locateNearestRaid", Location.class, int.class);
    }

    @Override
    public @NotNull List<Raid> getRaids() {
        throw NotImplementedException.createByLazy(World.class, "getRaids");
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        throw NotImplementedException.createByLazy(World.class, "getEnderDragonBattle");
    }

    @Override
    public @NotNull Set<FeatureFlag> getFeatureFlags() {
        return Collections.emptySet();
    }

    @Override
    public boolean doesRespawnAnchorWork() {
        throw NotImplementedException.createByLazy(World.class, "doesRespawnAnchorWork");
    }

    @Override
    public double getCoordinateScale() {
        throw NotImplementedException.createByLazy(World.class, "getCoordinateScale");
    }

    @Override
    public @NotNull String getName() {
        return this.world.properties()
                .displayName()
                .map(com -> PlainTextComponentSerializer.plainText().serialize(com))
                .orElse(this.world.key().value());
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return SoakResourceKeyMap.mapToBukkit(this.world.key());
    }

    @Override
    public void save() {
        throw NotImplementedException.createByLazy(World.class, "save");
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        throw NotImplementedException.createByLazy(SoakWorld.class, "setMetadata", String.class, MetadataValue.class);
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        throw NotImplementedException.createByLazy(SoakWorld.class, "getMetadata", String.class);
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        throw NotImplementedException.createByLazy(SoakWorld.class, "hasMetadata", String.class);
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        throw NotImplementedException.createByLazy(SoakWorld.class, "removeMetadata", String.class, Plugin.class);
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte[] message) {
        throw NotImplementedException.createByLazy(SoakWorld.class,
                "sendPluginMessage",
                Plugin.class,
                String.class,
                byte[].class);
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        throw NotImplementedException.createByLazy(SoakWorld.class, "getListeningPluginChannels");
    }

    @Override
    public WorldServer getHandle() {
        return (WorldServer) this.sponge();
    }

    @Override
    public int hashCode() {
        return this.sponge().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SoakWorld)) {
            return false;
        }
        var world = (SoakWorld) obj;
        return this.sponge().equals(world.sponge());
    }

    @Override
    public void setSponge(ServerWorld sponge) {
        this.world = sponge;
    }

    @Override
    public boolean isSame(ServerWorld sponge) {
        return this.key.equals(sponge.key());
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        throw NotImplementedException.createByLazy(World.class, "getPersistentDataContainer");
    }
}
