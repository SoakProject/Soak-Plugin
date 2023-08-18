package org.soak.utils;

import org.spongepowered.api.item.inventory.ContainerType;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.registry.DefaultedRegistryReference;

public class InventoryHelper {

    public static DefaultedRegistryReference<ContainerType> toChestContainerType(int row) {
        switch (row) {
            case 1 -> {
                return ContainerTypes.GENERIC_9X1;
            }
            case 2 -> {
                return ContainerTypes.GENERIC_9X2;
            }
            case 3 -> {
                return ContainerTypes.GENERIC_9X3;
            }
            case 4 -> {
                return ContainerTypes.GENERIC_9X4;
            }
            case 5 -> {
                return ContainerTypes.GENERIC_9X5;
            }
            case 6 -> {
                return ContainerTypes.GENERIC_9X6;
            }
            default -> throw new IndexOutOfBoundsException("Unknown chest size of " + row);
        }
    }
}
