package org.soak.map.item.inventory;

import org.bukkit.inventory.EquipmentSlot;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.registry.DefaultedRegistryReference;

public class SoakEquipmentMap {

    public static DefaultedRegistryReference<EquipmentType> toSponge(EquipmentSlot slot) {
        switch (slot) {
            case HAND:
                return EquipmentTypes.MAINHAND;
            case OFF_HAND:
                return EquipmentTypes.OFFHAND;
            case FEET:
                return EquipmentTypes.FEET;
            case LEGS:
                return EquipmentTypes.LEGS;
            case CHEST:
                return EquipmentTypes.CHEST;
            case HEAD:
                return EquipmentTypes.HEAD;
            default:
                throw new RuntimeException("No mapping for " + slot.name());
        }
    }
}
