package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.entity.EntityType;
import com.sk89q.worldedit.world.gamemode.GameMode;
import com.sk89q.worldedit.world.item.ItemType;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

//converts BukkitAdapter calls to sponge/forge calls
public class BukkitAdapter {

    public static boolean equals(BlockType type, Material material) {
        return type.getId().equals(material.key().toString());
    }

    private static <T> T run(Function<WrapperAdapter, T> run) {
        return run.apply(WrapperAdapter.get());
    }

    public static BukkitWorld asBukkitWorld(World world) {
        return run(adapter -> adapter.asBukkitWorld(world));
    }

    public static World adapt(org.bukkit.World world) {
        return run(adapter -> adapter.adapt(world));
    }

    public static Actor adapt(CommandSender sender) {
        return run(adapter -> adapter.adapt(sender));

    }

    public static BukkitPlayer adapt(Player player) {
        return run(adapter -> adapter.adapt(player));
    }

    public static CommandSender adapt(Actor actor) {
        return run(adapter -> adapter.adapt(actor));
    }

    public static Player adapt(com.sk89q.worldedit.entity.Player player) {
        return run(adapter -> adapter.adapt(player));
    }

    public static Direction adapt(@Nullable BlockFace face) {
        return run(adapter -> adapter.adapt(face));
    }

    public static org.bukkit.World adapt(World world) {
        return run(adapter -> adapter.adapt(world));
    }

    public static Location adapt(org.bukkit.Location location) {
        return run(adapter -> adapter.adapt(location));
    }

    public static org.bukkit.Location adapt(Location location) {
        return run(adapter -> adapter.adapt(location));
    }

    public static org.bukkit.Location adapt(org.bukkit.World world, Vector3 position) {
        return run(adapter -> adapter.adapt(world, position));
    }

    public static org.bukkit.Location adapt(org.bukkit.World world, BlockVector3 position) {
        return run(adapter -> adapter.adapt(world, position));
    }

    public static org.bukkit.Location adapt(org.bukkit.World world, Location location) {
        return run(adapter -> adapter.adapt(world, location));
    }

    public static Vector3 asVector(org.bukkit.Location location) {
        return run(adapter -> adapter.asVector(location));
    }

    public static BlockVector3 asBlockVector(org.bukkit.Location location) {
        return run(adapter -> adapter.asBlockVector(location));
    }

    public static Entity adapt(org.bukkit.entity.Entity entity) {
        return run(adapter -> adapter.adapt(entity));
    }

    public static Material adapt(ItemType type) {
        return run(adapter -> adapter.adapt(type));
    }

    public static Material adapt(BlockType type) {
        return run(adapter -> adapter.adapt(type));
    }

    public static GameMode adapt(org.bukkit.GameMode mode) {
        return run(adapter -> adapter.adapt(mode));
    }

    public static BiomeType adapt(Biome biome) {
        return run(adapter -> adapter.adapt(biome));
    }

    public static Biome adapt(BiomeType type) {
        return run(adapter -> adapter.adapt(type));
    }

    public static EntityType adapt(org.bukkit.entity.EntityType type) {
        return run(adapter -> adapter.adapt(type));
    }

    public static org.bukkit.entity.EntityType adapt(EntityType type) {
        return run(adapter -> adapter.adapt(type));
    }

    @Nullable
    public static BlockType asBlockType(Material material) {
        return run(adapter -> adapter.asBlockType(material));
    }

    @Nullable
    public static ItemType asItemType(Material material) {
        return run(adapter -> adapter.asItemType(material));
    }

    public static BlockState adapt(BlockData data) {
        return run(adapter -> adapter.adapt(data));
    }

    public static <B extends BlockStateHolder<B>> BlockData adapt(B block) {
        return run(adapter -> adapter.adapt(block));
    }

    public static BlockState asBlockState(ItemStack stack) throws WorldEditException {
        return WrapperAdapter.get().asBlockState(stack);
    }

    public static BaseItemStack adapt(ItemStack stack) {
        return run(adapter -> adapter.adapt(stack));

    }

    public static ItemStack adapt(BaseItemStack stack) {
        return run(adapter -> adapter.adapt(stack));
    }
}
