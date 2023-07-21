package org.soak.wrapper.inventory.meta;

import com.destroystokyo.paper.Namespaced;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakMessageMap;
import org.soak.plugin.exception.NotImplementedException;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.ListValue;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractItemMeta implements ItemMeta {

    protected ValueContainer container;

    public AbstractItemMeta(ItemStack stack) {
        this.container = stack;
    }

    public AbstractItemMeta(ItemStackSnapshot stack) {
        this.container = stack;
    }

    protected ItemStackSnapshot copyToSnapshot() {
        if (this.container instanceof ItemStack stack) {
            return stack.createSnapshot();
        }
        return ((ItemStackSnapshot) this.container).copy();
    }

    private <T> void set(@NotNull Key<Value<T>> key, @Nullable T value) {
        if (value == null) {
            remove(key);
            return;
        }
        if (this.container instanceof ItemStack stack) {
            stack.offer(key, value);
            return;
        }
        this.container = ((ItemStackSnapshot) this.container).with(key, value).orElseThrow(() -> new RuntimeException("Key of " + key.key().formatted() + " is not supported with ItemStackSnapshot"));
    }

    private <T> void setList(@NotNull Key<ListValue<T>> key, @Nullable List<T> value) {
        if (value == null) {
            remove(key);
            return;
        }
        if (this.container instanceof ItemStack stack) {
            stack.offer(key, value);
            return;
        }
        this.container = ((ItemStackSnapshot) this.container).with(key, value).orElseThrow(() -> new RuntimeException("Key of " + key.key().formatted() + " is not supported with ItemStackSnapshot"));
    }

    private ItemType type() {
        if (this.container instanceof ItemStack stack) {
            return stack.type();
        }
        return ((ItemStackSnapshot) this.container).type();
    }

    private void remove(@NotNull Key<?> key) {
        if (this.container instanceof ItemStack stack) {
            stack.remove(key);
            return;
        }
        this.container = ((ItemStackSnapshot) this.container).without(key).orElseThrow(() -> new RuntimeException("Key of " + key.key().formatted() + " is not supported with ItemStackSnapshot"));
    }

    @Override
    public boolean hasDisplayName() {
        return this.container.get(Keys.DISPLAY_NAME).isPresent();
    }

    @Override
    public @Nullable Component displayName() {
        return this.container.get(Keys.DISPLAY_NAME).orElse(null);
    }

    @Override
    public void displayName(@Nullable Component displayName) {
        this.set(Keys.DISPLAY_NAME, displayName);
    }

    @Override
    public @NotNull String getDisplayName() {
        Component displayName = displayName();
        if (displayName == null) {
            displayName = this.type().asComponent();
        }
        return SoakMessageMap.mapToBukkit(displayName);
    }

    @Override
    public void setDisplayName(@Nullable String name) {
        if (name == null) {
            remove(Keys.DISPLAY_NAME);
            return;
        }
        Component displayName = SoakMessageMap.mapToComponent(name);
        displayName(displayName);
    }

    @Override
    public @NotNull BaseComponent[] getDisplayNameComponent() {
        throw NotImplementedException.createByLazy(AbstractItemMeta.class, "getDisplayNameComponent");
    }

    @Override
    public void setDisplayNameComponent(@Nullable BaseComponent[] component) {
        throw NotImplementedException.createByLazy(AbstractItemMeta.class, "setDisplayNameComponent", BaseComponent.class);
    }

    @Override
    public boolean hasLocalizedName() {
        throw NotImplementedException.createByLazy(AbstractItemMeta.class, "hasLocalizedName");
    }

    @Override
    public @NotNull String getLocalizedName() {
        throw NotImplementedException.createByLazy(AbstractItemMeta.class, "getLocalizedName");
    }

    @Override
    public void setLocalizedName(@Nullable String name) {
        throw NotImplementedException.createByLazy(AbstractItemMeta.class, "setLocalizedName", String.class);
    }

    @Override
    public boolean hasLore() {
        return this.container.get(Keys.LORE).isPresent();
    }

    @Override
    public @Nullable List<Component> lore() {
        return this.container.get(Keys.LORE).orElse(null);
    }

    @Override
    public void lore(@Nullable List<Component> lore) {
        setList(Keys.LORE, lore);
    }

    @Override
    public @Nullable List<String> getLore() {
        List<Component> list = this.lore();
        if (list == null) {
            return null;
        }
        return list.stream().map(SoakMessageMap::mapToBukkit).collect(Collectors.toList());
    }

    @Override
    public void setLore(@Nullable List<String> lore) {
        if (lore == null) {
            remove(Keys.LORE);
            return;
        }
        List<Component> list = lore.stream().map(SoakMessageMap::mapToComponent).collect(Collectors.toList());
        lore(list);
    }

    @Override
    public @Nullable List<BaseComponent[]> getLoreComponents() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getLoreComponents");
    }

    @Override
    public void setLoreComponents(@Nullable List<BaseComponent[]> lore) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setLoreComponents", List.class);

    }

    @Override
    public boolean hasCustomModelData() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "hasCustomModelData");
    }

    @Override
    public int getCustomModelData() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getCustomModelData");
    }

    @Override
    public void setCustomModelData(@Nullable Integer data) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setCustomModelData", Integer.class);
    }

    @Override
    public boolean hasEnchants() {
        return this.container.get(Keys.APPLIED_ENCHANTMENTS).isPresent();
    }

    @Override
    public boolean hasEnchant(@NotNull Enchantment ench) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "hasEnchant", Enchantment.class);
    }

    @Override
    public int getEnchantLevel(@NotNull Enchantment ench) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getEnchantLevel", Enchantment.class);
    }

    @Override
    public @NotNull Map<Enchantment, Integer> getEnchants() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getEnchants");
    }

    @Override
    public boolean addEnchant(@NotNull Enchantment ench, int level, boolean ignoreLevelRestriction) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "addEnchant", Enchantment.class, int.class, boolean.class);
    }

    @Override
    public boolean removeEnchant(@NotNull Enchantment ench) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "removeEnchant", Enchantment.class);
    }

    @Override
    public boolean hasConflictingEnchant(@NotNull Enchantment ench) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "hasConflictingEnchant", Enchantment.class);
    }

    @Override
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "addItemFlags", ItemFlag.class);
    }

    @Override
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "removeItemFlags", ItemFlag.class);
    }

    @Override
    public @NotNull Set<ItemFlag> getItemFlags() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getItemFlags");
    }

    @Override
    public boolean hasItemFlag(@NotNull ItemFlag flag) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "hasItemFlags", ItemFlag.class);
    }

    @Override
    public boolean isUnbreakable() {
        return this.container.get(Keys.IS_UNBREAKABLE).orElse(false);
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        set(Keys.IS_UNBREAKABLE, unbreakable);
    }

    @Override
    public boolean hasAttributeModifiers() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "hasAttributeModifiers");
    }

    @Override
    public @Nullable Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getAttributeModifiers");
    }

    @Override
    public void setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setAttributeModifiers", Multimaps.class);

    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getAttributeModifiers", EquipmentSlot.class);
    }

    @Override
    public @Nullable Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getAttributeModifiers", Attribute.class);
    }

    @Override
    public boolean addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "addAttributeModifiers", Attribute.class, AttributeModifier.class);
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "removeAttributeModifier", Attribute.class);
    }

    @Override
    public boolean removeAttributeModifier(@NotNull EquipmentSlot slot) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "removeAttributeModifier", EquipmentSlot.class);
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "removeAttributeModifier", Attribute.class, AttributeModifier.class);
    }

    @Override
    @Deprecated
    public @NotNull CustomItemTagContainer getCustomTagContainer() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getCustomTagContainer");
    }

    @Override
    public void setVersion(int version) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setVersion", int.class);
    }

    @Override
    public Set<Material> getCanDestroy() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getCanDestroy");
    }

    @Override
    public void setCanDestroy(Set<Material> canDestroy) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setCanDestroy", Set.class);
    }

    @Override
    public Set<Material> getCanPlaceOn() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getCanPlaceOn");
    }

    @Override
    public void setCanPlaceOn(Set<Material> canPlaceOn) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setCanPlaceOn", Set.class);
    }

    @Override
    public @NotNull Set<Namespaced> getDestroyableKeys() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getDestroyableKeys");
    }

    @Override
    public void setDestroyableKeys(@NotNull Collection<Namespaced> canDestroy) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setDestroyableKeys", Collection.class);
    }

    @Override
    public @NotNull Set<Namespaced> getPlaceableKeys() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getPlaceableKeys");
    }

    @NotNull
    @Override
    public void setPlaceableKeys(@NotNull Collection<Namespaced> canPlaceOn) {
        throw NotImplementedException.createByLazy(ItemMeta.class, "setPlaceableKeys");
    }

    @Override
    public boolean hasPlaceableKeys() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "hasPlaceableKeys");
    }

    @Override
    public boolean hasDestroyableKeys() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "hasDestroyableKeys");
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "serialize");
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        throw NotImplementedException.createByLazy(ItemMeta.class, "getPersistentDataContainer");
    }
}
