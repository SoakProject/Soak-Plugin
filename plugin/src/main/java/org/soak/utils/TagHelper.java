package org.soak.utils;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.fluid.FluidType;
import org.spongepowered.api.fluid.FluidTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.tag.Tag;

import java.util.stream.Stream;

public class TagHelper {

    public static Stream<BlockType> getBlockTypes(Tag<BlockType> tag) {
        return BlockTypes.registry().stream().filter(type -> type.is(tag));
    }

    public static Stream<ItemType> getItemTypes(Tag<ItemType> tag) {
        return ItemTypes.registry().stream().filter(type -> type.is(tag));
    }

    public static Stream<EntityType<?>> getEntityTypes(Tag<EntityType<?>> tag) {
        return EntityTypes.registry().stream().filter(type -> type.is(tag));
    }

    public static Stream<FluidType> getFluidTypes(Tag<FluidType> tag) {
        return FluidTypes.registry().stream().filter(type -> type.is(tag));
    }
}
