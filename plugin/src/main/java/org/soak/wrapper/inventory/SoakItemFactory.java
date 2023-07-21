package org.soak.wrapper.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;

import java.util.Objects;
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
        throw NotImplementedException.createByLazy(ItemFactory.class, "equals", ItemMeta.class, ItemMeta.class);
    }

    @Override
    public @NotNull Color getDefaultLeatherColor() {
        throw NotImplementedException.createByLazy(ItemFactory.class, "getDefaultLeatherColor");
    }

    @Override
    public ItemMeta getItemMeta(@NotNull Material arg0) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "getItemMeta", Material.class);
    }

    @Override
    public boolean isApplicable(ItemMeta arg0, ItemStack arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "isApplicable", ItemMeta.class, ItemStack.class);
    }

    @Override
    public boolean isApplicable(ItemMeta arg0, Material arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "isApplicable", ItemMeta.class, Material.class);
    }

    @Override
    public ItemMeta asMetaFor(@NotNull ItemMeta arg0, @NotNull ItemStack arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "asMetaFor", ItemMeta.class, ItemStack.class);
    }

    @Override
    public ItemMeta asMetaFor(@NotNull ItemMeta arg0, @NotNull Material arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "asMetaFor", ItemMeta.class, Material.class);
    }

    @Deprecated
    @Override
    public @NotNull Material updateMaterial(@NotNull ItemMeta arg0, @NotNull Material arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "updateMaterial", ItemMeta.class, Material.class);
    }

    @Override
    public @NotNull HoverEvent<HoverEvent.ShowItem> asHoverEvent(@NotNull ItemStack item, @NotNull UnaryOperator<HoverEvent.ShowItem> op) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "asHoverEvent", ItemStack.class, UnaryOperator.class);
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
        throw NotImplementedException.createByLazy(ItemFactory.class, "hoverContentOf", Entity.class, BaseComponent.class);
    }

    @Deprecated
    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity arg0, BaseComponent[] arg1) {
        throw NotImplementedException.createByLazy(ItemFactory.class, "hoverContentOf", Entity.class, BaseComponent[].class);
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