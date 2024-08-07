package org.soak.wrapper.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.soak.exception.NotImplementedException;
import org.soak.map.item.SoakItemStackMap;
import org.soak.wrapper.inventory.meta.AbstractItemMeta;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;

public class SoakItemFactory implements ItemFactory {
    @Override
    public @NotNull Component displayName(@NotNull ItemStack arg0) {
        ItemMeta meta = arg0.getItemMeta();
        if (meta == null) {
            throw new IllegalStateException("Itemstack does not have meta applied");
        }
        return Objects.requireNonNullElse(meta.displayName(), Component.empty());
    }

    @Override
    public @NotNull ItemStack ensureServerConversions(@NotNull ItemStack item) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "ensureServerConversions", ItemStack.class);
    }

    @Override
    public boolean equals(ItemMeta arg0, ItemMeta arg1) {
        if (arg0 == null && arg1 == null) {
            return true;
        }
        if (arg0 == null) {
            return false;
        }
        if (arg1 == null) {
            return false;
        }

        return ItemStackComparators.IGNORE_SIZE.get()
                .compare(SoakItemStackMap.toSponge(arg0), SoakItemStackMap.toSponge(arg1)) == 0;
    }

    @Override
    public @NotNull Color getDefaultLeatherColor() {
        throw NotImplementedException.createByLazy(ItemFactory.class, "getDefaultLeatherColor");
    }

    @Override
    public @NotNull ItemStack createItemStack(@NotNull String s) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(ItemFactory.class, "createItemStack", String.class);
    }

    @Override
    public ItemMeta getItemMeta(@NotNull Material arg0) {
        ItemType type = arg0.asItem()
                .orElseThrow(() -> new IllegalStateException("Material of " + arg0.name() + " is not a item"));
        return SoakItemStackMap.toBukkitMeta(org.spongepowered.api.item.inventory.ItemStack.of(type));
    }

    @Override
    public boolean isApplicable(ItemMeta arg0, ItemStack arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "isApplicable", ItemMeta.class, ItemStack.class);
    }

    private boolean isMetadataTypeApplicable(ItemMeta arg0, ItemMeta compare) {
        Class<?> targetType = compare.getClass();
        return arg0.getClass().isAssignableFrom(targetType);
    }

    @Override
    public boolean isApplicable(ItemMeta arg0, Material arg1) {
        return isMetadataTypeApplicable(arg0, this.getItemMeta(arg1));
    }

    @Override
    public ItemMeta asMetaFor(@NotNull ItemMeta arg0, @NotNull ItemStack arg1) {
        ItemMeta stacksMeta = arg1.hasItemMeta() ? arg1.getItemMeta() : SoakItemStackMap.toBukkitMeta(org.spongepowered.api.item.inventory.ItemStack.of(
                arg1.getType().asItem().orElseThrow(() -> new RuntimeException("Material is not item in itemstack")),
                arg1.getAmount()));
        if (!(stacksMeta instanceof AbstractItemMeta)) {
            throw new RuntimeException("An item meta was not of abstract type: From: " + stacksMeta.getClass()
                    .getSimpleName());
        }
        var stacksSoakMeta = (AbstractItemMeta) stacksMeta;
        stacksSoakMeta.copyInto(arg0);
        return stacksSoakMeta;
    }

    @Override
    public ItemMeta asMetaFor(@NotNull ItemMeta arg0, @NotNull Material arg1) {
        return asMetaFor(arg0, new ItemStack(arg1));
    }

    @Override
    public @NotNull Material updateMaterial(@NotNull ItemMeta arg0, @NotNull Material arg1) {
        if (!(arg0 instanceof AbstractItemMeta)) {
            throw new RuntimeException("ItemMeta is not extending AbstractItemMeta (" + arg0.getClass()
                    .getName() + ")");
        }
        var abstractMeta = (AbstractItemMeta) arg0;
        abstractMeta.manipulate(container -> {
            ItemType type = arg1.asItem().orElseThrow(() -> new RuntimeException("Material is not a item"));
            if (container instanceof org.spongepowered.api.item.inventory.ItemStack) {
                var stack = (org.spongepowered.api.item.inventory.ItemStack) container;
                return org.spongepowered.api.item.inventory.ItemStack.builder()
                        .fromItemStack(stack)
                        .itemType(type)
                        .build();
            }
            return org.spongepowered.api.item.inventory.ItemStack.builder()
                    .fromSnapshot((ItemStackSnapshot) container)
                    .itemType(type)
                    .build()
                    .createSnapshot();
        });
        return arg1;
    }

    @Override
    public @NotNull ItemStack enchantWithLevels(@NotNull ItemStack itemStack, @Range(from = 1L, to = 30L) int i, boolean b, @NotNull Random random) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "enchantWithLevels", ItemStack.class, int.class, boolean.class, Random.class);
    }

    @Override
    public @NotNull HoverEvent<HoverEvent.ShowItem> asHoverEvent(@NotNull ItemStack item, @NotNull UnaryOperator<HoverEvent.ShowItem> op) {
        throw NotImplementedException.createByLazy(ItemFactory.class,
                "asHoverEvent",
                ItemStack.class,
                UnaryOperator.class);
    }

    @Override
    public String getI18NDisplayName(ItemStack arg0) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "getI18NDisplayName", ItemStack.class);
    }

    @Deprecated
    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity arg0, String arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "hoverContentOf", Entity.class, String.class);
    }

    @Deprecated
    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity arg0, BaseComponent arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class,
                "hoverContentOf",
                Entity.class,
                BaseComponent.class);
    }

    @Deprecated
    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity arg0, BaseComponent[] arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class,
                "hoverContentOf",
                Entity.class,
                BaseComponent[].class);
    }

    @Override
    public @Nullable ItemStack getSpawnEgg(@Nullable EntityType entityType) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "getSpawnEgg", EntityType.class);
    }

    @Deprecated
    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity arg0) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "hoverContentOf", Entity.class);
    }

    @Override
    public @NotNull Content hoverContentOf(@NotNull ItemStack arg0) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "hoverContentOf", ItemStack.class);
    }

}