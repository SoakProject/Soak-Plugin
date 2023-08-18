
package org.bukkit.event.inventory;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ContainerType;
import org.spongepowered.api.item.inventory.Slot;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public enum InventoryType {

    ANVIL,
    BARREL,
    BEACON,
    BLAST_FURNACE,
    BREWING,
    CARTOGRAPHY,
    CHEST,
    CRAFTING,
    CREATIVE,
    DISPENSER,
    DROPPER,
    ENCHANTING,
    ENDER_CHEST,
    FURNACE,
    GRINDSTONE,
    HOPPER,
    LECTERN,
    LOOM,
    MERCHANT,
    PLAYER,
    SHULKER_BOX,
    SMITHING,
    SMOKER,
    STONECUTTER,
    WORKBENCH;


    public static Optional<InventoryType> container(ResourceKey key) {
        throw new RuntimeException("Template file, use VanillaMaterials");
    }

    public static InventoryType container(Container inventory) {
        throw new RuntimeException("Template file, use VanillaMaterials");
    }

    private Collection<ContainerType> sponge() {
        throw new RuntimeException("Template file, use VanillaMaterials");
    }

    @Deprecated
    public String getDefaultTitle() {
        throw new RuntimeException("Template file, use VanillaMaterials");
    }

    public Component defaultTitle() {
        throw new RuntimeException("Template file, use VanillaMaterials");
    }

    public boolean isCraftable() {
        throw new RuntimeException("Template file, use VanillaMaterials");
    }

    public static enum SlotType {
        ARMOR,
        CONTAINER,
        CRAFTING,
        FUEL,
        OUTSIDE,
        QUICKBAR,
        RESULT;

        private final @Nullable Class<? extends Slot> spongeType;

        SlotType() {
            this(null);
        }

        SlotType(@Nullable Class<? extends Slot> slotClass) {
            this.spongeType = slotClass;
        }

        public static SlotType typeFor(Slot slot) {
            return Stream.of(values())
                    .filter(type -> type.isMappingFor(slot))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Unknown type of " + slot.getClass().getName()));
        }

        public boolean isMappingFor(Slot slot) {
            return this.spongeType != null && this.spongeType.isInstance(slot);
        }
    }
}

