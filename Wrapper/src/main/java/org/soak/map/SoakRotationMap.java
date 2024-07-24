package org.soak.map;

import org.bukkit.block.structure.StructureRotation;
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;

public class SoakRotationMap {

    public static DefaultedRegistryReference<Rotation> toSponge(StructureRotation rotation) {
        return switch (rotation) {
            case NONE -> Rotations.NONE;
            case CLOCKWISE_90 -> Rotations.CLOCKWISE_90;
            case COUNTERCLOCKWISE_90 -> Rotations.COUNTERCLOCKWISE_90;
            case CLOCKWISE_180 -> Rotations.CLOCKWISE_180;
            default -> throw new RuntimeException("Unknown rotation: " + rotation.name());
        };
    }
}
