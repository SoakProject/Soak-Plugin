package org.soak.wrapper.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.exception.NotImplementedException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.query.QueryTypes;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SoakInventory<Inv extends org.spongepowered.api.item.inventory.Inventory> implements Inventory {

    private final Inv spongeInventory;
    private Component requestedTitle;

    public SoakInventory(Inv spongeInventory) {
        this.spongeInventory = spongeInventory;
    }

    public static <I extends org.spongepowered.api.item.inventory.Inventory> SoakInventory<I> wrap(I inventory) {
        return new SoakInventory<>(inventory);
    }

    public Optional<Component> requestedTitle() {
        return Optional.ofNullable(this.requestedTitle);
    }

    public void setRequestedTitle(Component component) {
        this.requestedTitle = component;
    }

    public Inv sponge() {
        return this.spongeInventory;
    }

    @Override
    public @NotNull ItemStack[] getContents() {
        return items().toArray(ItemStack[]::new);
    }

    @Override
    public void setContents(ItemStack[] arg0) {
        throw NotImplementedException.createByLazy(Inventory.class, "setContents", ItemStack[].class);
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(Inventory.class, "all", Material.class);
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item) {
        throw NotImplementedException.createByLazy(Inventory.class, "all", ItemStack.class);
    }

    private org.spongepowered.api.item.inventory.Inventory asJustStack(ItemStack stack) {
        var item = SoakItemStackMap.toSponge(stack);
        return this.spongeInventory.query(QueryTypes.ITEM_STACK_EXACT.get().of(item));
    }

    private org.spongepowered.api.item.inventory.Inventory asJustType(Material material) {
        var itemType = material.asItem()
                .orElseThrow(() -> new RuntimeException("Material " + material.name() + " has no itemtype"));
        return this.spongeInventory.query(QueryTypes.ITEM_TYPE.get().of(itemType));
    }

    @Override
    public ItemStack getItem(int arg0) {
        var spongeItem = this.spongeInventory.peekAt(arg0)
                .orElse(org.spongepowered.api.item.inventory.ItemStack.empty());
        return SoakItemStackMap.toBukkit(spongeItem);
    }

    @Override
    public void remove(@NotNull ItemStack arg0) {
        asJustStack(arg0).clear();
    }

    @Override
    public void remove(@NotNull Material arg0) {
        asJustType(arg0).clear();
    }

    @Override
    public void clear(int arg0) {
        throw NotImplementedException.createByLazy(Inventory.class, "clear", int.class);
    }

    @Override
    public void clear() {
        this.spongeInventory.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.spongeInventory.capacity() == this.spongeInventory.freeCapacity();
    }

    private Stream<ItemStack> items() {
        return this.spongeInventory.slots()
                .stream()
                .map(org.spongepowered.api.item.inventory.Inventory::peek)
                .filter(item -> item.equals(org.spongepowered.api.item.inventory.ItemStack.empty()))
                .map(SoakItemStackMap::toBukkit);
    }

    @Override
    public @NotNull ListIterator<ItemStack> iterator() {
        return items().collect(Collectors.toList()).listIterator();
    }

    @Override
    public @NotNull ListIterator<ItemStack> iterator(int index) {
        return items().collect(Collectors.toList()).listIterator(index);
    }

    @Override
    public boolean contains(ItemStack arg0, int arg1) {
        throw NotImplementedException.createByLazy(Inventory.class, "contains", ItemStack.class, int.class);
    }

    @Override
    public boolean contains(@NotNull Material arg0, int arg1) {
        throw NotImplementedException.createByLazy(Inventory.class, "contains", Material.class, int.class);
    }

    @Override
    public boolean contains(ItemStack arg0) {
        return asJustStack(arg0).capacity() != 0;
    }

    @Override
    public boolean contains(@NotNull Material arg0) {
        return asJustType(arg0).capacity() != 0;
    }

    @Override
    public int first(@NotNull ItemStack arg0) {
        var slots = asJustStack(arg0).slots();
        if (slots.isEmpty()) {
            return -1;
        }
        return slots.get(0).get(Keys.SLOT_INDEX).orElse(-1);
    }

    @Override
    public int first(@NotNull Material arg0) {
        var slots = asJustType(arg0).slots();
        if (slots.isEmpty()) {
            return -1;
        }
        return slots.get(0).get(Keys.SLOT_INDEX).orElse(-1);
    }

    @Override
    public Location getLocation() {
        throw NotImplementedException.createByLazy(Inventory.class, "getLocation");
    }

    @Override
    public int close() {
        throw NotImplementedException.createByLazy(Inventory.class, "close");
    }

    @Override
    public @NotNull InventoryType getType() {
        if (!(spongeInventory instanceof Container)) {
            throw new RuntimeException("Sponge inventory is not a Container, unknown inventory type");
        }
        return InventoryType.container((Container) this.spongeInventory);
    }

    @Override
    public int getSize() {
        return this.spongeInventory.capacity();
    }

    @Override
    public int getMaxStackSize() {
        throw NotImplementedException.createByLazy(Inventory.class, "getMaxStackSize");
    }

    @Override
    public void setMaxStackSize(int arg0) {
        throw NotImplementedException.createByLazy(Inventory.class, "setMaxStackSize", int.class);
    }

    @Override
    public void setItem(int arg0, ItemStack arg1) {
        var item = SoakItemStackMap.toSponge(arg1);
        this.spongeInventory.set(arg0, item);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        HashMap<Integer, ItemStack> map = new HashMap<>();
        for (var bukkitItem : items) {
            boolean wasPlaced = false;
            var item = SoakItemStackMap.toSponge(bukkitItem);
            for (var slot : this.spongeInventory.slots()) {
                if (!slot.canFit(item)) {
                    continue;
                }
                var slotIndex = slot.get(Keys.SLOT_INDEX)
                        .orElseThrow(() -> new RuntimeException("No slot index for " + this.spongeInventory.getClass()
                                .getName()));
                map.put(slotIndex, bukkitItem);
                wasPlaced = true;
                break;
            }
            if (!wasPlaced) {
                throw new IllegalArgumentException("Cannot place item " + item.type()
                        .key(RegistryTypes.ITEM_TYPE)
                        .formatted());
            }
        }
        if (map.size() != items.length) {
            //shouldn't be hit, but just in case
            throw new IllegalArgumentException("Cannot place all items");
        }
        map.forEach((index, bukkitItem) -> {
            var item = SoakItemStackMap.toSponge(bukkitItem);
            this.spongeInventory.offer(index, item);
        });
        return map;
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(Inventory.class, "removeItem", ItemStack[].class);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> removeItemAnySlot(@NotNull ItemStack... items) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(Inventory.class, "removeItemAnySlot", ItemStack[].class);
    }

    @Override
    public ItemStack[] getStorageContents() {
        throw NotImplementedException.createByLazy(Inventory.class, "getStorageContents");
    }

    @Override
    public void setStorageContents(ItemStack[] arg0) {
        throw NotImplementedException.createByLazy(Inventory.class, "setStorageContents", ItemStack[].class);
    }

    @Override
    public boolean containsAtLeast(ItemStack arg0, int arg1) {
        throw NotImplementedException.createByLazy(Inventory.class, "containsAtLeast", ItemStack.class, int.class);
    }

    @Override
    public int firstEmpty() {
        throw NotImplementedException.createByLazy(Inventory.class, "firstEmpty");
    }

    @Override
    public @NotNull List<HumanEntity> getViewers() {
        throw NotImplementedException.createByLazy(Inventory.class, "getViewers");
    }

    @Override
    public InventoryHolder getHolder() {
        throw NotImplementedException.createByLazy(Inventory.class, "getHolder");
    }

    @Override
    public InventoryHolder getHolder(boolean arg0) {
        throw NotImplementedException.createByLazy(Inventory.class, "getHolder", boolean.class);
    }

}