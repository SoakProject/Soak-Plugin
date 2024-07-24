package org.soak.utils;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Optional;

public class SnapshotHelper {

    public static ItemStackSnapshot copyWithQuantity(ItemStackSnapshot snapshot, int quantity) {
        DataContainer container = snapshot.toContainer();

        for (DataQuery key : container.keys(true)) {
            Optional<Object> opValue = container.get(key);
            if (opValue.isEmpty()) {
                continue;
            }
            System.out.println("'" + key.asString(".") + "': " + opValue.get().toString());
        }


        throw new RuntimeException("Not implemented yet");
    }
}
