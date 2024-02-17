package org.soak.map;

import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.util.mirror.Mirror;
import org.spongepowered.api.util.mirror.Mirrors;

public class SoakMirrorMap {

    public static DefaultedRegistryReference<Mirror> toSponge(org.bukkit.block.structure.Mirror mirror) {
        return switch (mirror) {
            case NONE -> Mirrors.NONE;
            case LEFT_RIGHT -> Mirrors.LEFT_RIGHT;
            case FRONT_BACK -> Mirrors.FRONT_BACK;
            default -> throw new RuntimeException("Unknown mirror of " + mirror.name());
        };
    }
}
