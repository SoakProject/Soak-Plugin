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

public interface WrapperAdapter {

    static WrapperAdapter get() {
        try {
            Class.forName("com.sk89q.worldedit.sponge.SpongeAdapter");
            return new BukkitSpongeAdapter();
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
        throw new RuntimeException("No compatible worldedit found", e);

        }
    }

    BukkitWorld asBukkitWorld(World world);

    World adapt(org.bukkit.World world);

    Actor adapt(CommandSender sender);

    BukkitPlayer adapt(Player player);

    CommandSender adapt(Actor actor);

    Player adapt(com.sk89q.worldedit.entity.Player player);

    Direction adapt(@Nullable BlockFace face);

    org.bukkit.World adapt(World world);

    Location adapt(org.bukkit.Location location);

    org.bukkit.Location adapt(Location location);

    org.bukkit.Location adapt(org.bukkit.World world, Vector3 position);

    org.bukkit.Location adapt(org.bukkit.World world, BlockVector3 position);

    org.bukkit.Location adapt(org.bukkit.World world, Location location);

    Vector3 asVector(org.bukkit.Location location);

    BlockVector3 asBlockVector(org.bukkit.Location location);

    Entity adapt(org.bukkit.entity.Entity entity);

    Material adapt(ItemType type);

    Material adapt(BlockType type);

    GameMode adapt(org.bukkit.GameMode mode);

    BiomeType adapt(Biome biome);

    Biome adapt(BiomeType type);

    EntityType adapt(org.bukkit.entity.EntityType type);

    org.bukkit.entity.EntityType adapt(EntityType type);

    @Nullable
    BlockType asBlockType(Material material);

    @Nullable
    ItemType asItemType(Material material);

    BlockState adapt(BlockData data);

    <B extends BlockStateHolder<B>> BlockData adapt(B block);

    BlockState asBlockState(ItemStack stack) throws WorldEditException;

    BaseItemStack adapt(ItemStack stack);

    ItemStack adapt(BaseItemStack stack);
}

    

