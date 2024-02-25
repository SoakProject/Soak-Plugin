package org.soak.map;

import org.bukkit.block.data.type.Bed;
import org.spongepowered.api.data.type.PortionType;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.registry.DefaultedRegistryReference;

public class SoakHalfMap {

    public static Bed.Part toBukkit(PortionType type) {
        if (type.equals(PortionTypes.BOTTOM.get())) {
            return Bed.Part.FOOT;
        }
        return Bed.Part.HEAD;
    }

    public static DefaultedRegistryReference<PortionType> toSponge(Bed.Part part) {
        return switch (part) {
            case HEAD -> PortionTypes.TOP;
            case FOOT -> PortionTypes.BOTTOM;
        };
    }
}
