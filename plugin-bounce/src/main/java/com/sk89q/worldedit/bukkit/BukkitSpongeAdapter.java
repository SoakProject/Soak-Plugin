package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.sponge.SpongeAdapter;
import com.sk89q.worldedit.sponge.SpongeCommandSender;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.AbstractWorld;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.entity.EntityType;
import com.sk89q.worldedit.world.entity.EntityTypes;
import com.sk89q.worldedit.world.gamemode.GameMode;
import com.sk89q.worldedit.world.item.ItemType;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.soak.map.SoakDirectionMap;
import org.soak.map.SoakLocationMap;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.block.data.AbstractBlockData;
import org.soak.wrapper.command.SoakCommandSender;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.biome.Biomes;

import javax.annotation.Nullable;
import java.util.Optional;

//converts BukkitAdapter calls to sponge calls
public class BukkitSpongeAdapter implements WrapperAdapter {

    @Override
    public BukkitWorld asBukkitWorld(World world) {
        if (world instanceof BukkitWorld bWorld) {
            return bWorld;
        }
        return new BukkitWorld((AbstractWorld) world);
    }

    @Override
    public World adapt(org.bukkit.World world) {
        SoakWorld soakWorld = (SoakWorld) world;
        var spongeWorld = soakWorld.sponge();
        var spongeEditWorld = (AbstractWorld) SpongeAdapter.adapt(spongeWorld);
        return new BukkitWorld(spongeEditWorld);
    }

    @Override
    public Actor adapt(CommandSender sender) {
        if (sender instanceof Player player) {
            return adapt(player);
        }
        SoakCommandSender soakSender = (SoakCommandSender) sender;
        return new SpongeCommandSender(soakSender.getAudience());
    }

    @Override
    public BukkitPlayer adapt(Player player) {
        SoakPlayer soakPlayer = (SoakPlayer) player;
        var spongeEditPlayer = SpongeAdapter.adapt(soakPlayer.spongeEntity());
        return new BukkitPlayer(spongeEditPlayer);
    }

    @Override
    public CommandSender adapt(Actor actor) {
        if (actor instanceof com.sk89q.worldedit.entity.Player editPlayer) {
            return adapt(editPlayer);
        }
        throw new RuntimeException("Unknown worldedit actor of " + actor.getName() + ":" + actor.getClass().getName());
    }

    @Override
    public Player adapt(com.sk89q.worldedit.entity.Player player) {
        var spongePlayer = SpongeAdapter.adapt(player);
        return new SoakPlayer((ServerPlayer) spongePlayer);
    }

    @Override
    public Direction adapt(@Nullable BlockFace face) {
        if (face == null) {
            return null;
        }
        var spongeDirection = SoakDirectionMap.toSponge(face);
        return SpongeAdapter.adapt(spongeDirection);
    }

    @Override
    public org.bukkit.World adapt(World world) {
        var spongeWorld = SpongeAdapter.adapt(world);
        return new SoakWorld(spongeWorld);
    }

    @Override
    public Location adapt(org.bukkit.Location location) {
        var spongeLocation = SoakLocationMap.toSponge(location);
        var rotation = SoakLocationMap.toSpongeRotation(location);
        return SpongeAdapter.adapt(spongeLocation, rotation);
    }

    @Override
    public org.bukkit.Location adapt(Location location) {
        var spongeLocation = SpongeAdapter.adapt(location);
        var bukkitLoc = SoakLocationMap.toBukkit(spongeLocation);
        bukkitLoc.setYaw(location.getYaw());
        bukkitLoc.setPitch(location.getPitch());
        return bukkitLoc;
    }

    @Override
    public org.bukkit.Location adapt(org.bukkit.World world, Vector3 position) {
        return new org.bukkit.Location(world, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public org.bukkit.Location adapt(org.bukkit.World world, BlockVector3 position) {
        return new org.bukkit.Location(world, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public org.bukkit.Location adapt(org.bukkit.World world, Location location) {
        var loc = new org.bukkit.Location(world, location.getX(), location.getY(), location.getZ());
        loc.setPitch(loc.getPitch());
        loc.setYaw(loc.getYaw());
        return loc;
    }

    @Override
    public Vector3 asVector(org.bukkit.Location location) {
        return Vector3.at(location.x(), location.y(), location.z());
    }

    @Override
    public BlockVector3 asBlockVector(org.bukkit.Location location) {
        return BlockVector3.at(location.x(), location.y(), location.z());
    }

    @Override
    public Entity adapt(org.bukkit.entity.Entity entity) {
        if (entity instanceof SoakPlayer player) {
            return adapt(player);
        }
        throw new RuntimeException("Cannot map " + entity.getType().name() + " to worldedit entity");
    }

    @Override
    public Material adapt(ItemType type) {
        var spongeItemType = ItemTypes.registry().value(ResourceKey.resolve(type.getId()));
        return Material.getItemMaterial(spongeItemType);
    }

    @Override
    public Material adapt(BlockType type) {
        var spongeBlockType = BlockTypes.registry().value(ResourceKey.resolve(type.getId()));
        return Material.getBlockMaterial(spongeBlockType);
    }

    @Override
    public GameMode adapt(org.bukkit.GameMode mode) {
        return GameMode.REGISTRY.get(mode.sponge().key(RegistryTypes.GAME_MODE).formatted());
    }

    @Override
    public BiomeType adapt(Biome biome) {
        return BiomeType.REGISTRY.get(biome.getKey().asString());
    }

    @Override
    public Biome adapt(BiomeType type) {
        var id = ResourceKey.resolve(type.getId());
        var spongeBiome = Sponge
                .server()
                .worldManager()
                .worlds()
                .stream()
                .map(Biomes::registry)
                .map(reg -> reg.findValue(id))
                .filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get)
                .orElseThrow(() -> new RuntimeException("Cannot find biometype of " + type.getId()));
        return Biome.fromSponge(spongeBiome);
    }

    @Override
    public EntityType adapt(org.bukkit.entity.EntityType type) {
        return EntityTypes.get(type.key().asString());
    }

    @Override
    public org.bukkit.entity.EntityType adapt(EntityType type) {
        var key = ResourceKey.resolve(type.getId());
        var spongeEntityType = org.spongepowered.api.entity.EntityTypes.registry().value(key);
        return org.bukkit.entity.EntityType.fromSponge(spongeEntityType);
    }

    @Override
    @Nullable
    public BlockType asBlockType(Material material) {
        return com.sk89q.worldedit.world.block.BlockTypes.get(material.key().asString());
    }

    @Override
    @Nullable
    public ItemType asItemType(Material material) {
        return com.sk89q.worldedit.world.item.ItemTypes.get(material.key().asString());
    }

    @Override
    public BlockState adapt(BlockData data) {
        var soakData = (AbstractBlockData) data;
        return SpongeAdapter.adapt(soakData.sponge());
    }

    @Override
    public <B extends BlockStateHolder<B>> BlockData adapt(B block) {
        var spongeState = SpongeAdapter.adapt(block.toImmutableState());
        return SoakPlugin.plugin().getMemoryStore().get(spongeState);
    }

    @Override
    public BlockState asBlockState(ItemStack stack) throws WorldEditException {
        throw new RuntimeException("Cannot convert Bukkit ItemStack to Worldedit BlockState");
    }

    @Override
    public BaseItemStack adapt(ItemStack stack) {
        var spongeStack = SoakItemStackMap.toSponge(stack);
        return SpongeAdapter.adapt(spongeStack);
    }

    @Override
    public ItemStack adapt(BaseItemStack stack) {
        var spongeStack = SpongeAdapter.adapt(stack);
        return SoakItemStackMap.toBukkit(spongeStack);
    }
}
