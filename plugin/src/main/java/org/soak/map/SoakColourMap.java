package org.soak.map;

import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.util.Color;

import java.util.Optional;

public class SoakColourMap {

    public static Color toSponge(org.bukkit.Color colour) {
        return Color.ofRgb(colour.asRGB());
    }

    public static org.bukkit.Color toBukkit(Color colour) {
        return org.bukkit.Color.fromRGB(colour.rgb());
    }

    public static Optional<DyeColor> toSpongeDye(Color color) {
        return DyeColors.registry().stream().filter(dye -> dye.color().equals(color)).findAny();
    }

    public static org.bukkit.DyeColor toBukkitDye(DyeColor colour) {
        return org.bukkit.DyeColor.getByColor(toBukkit(colour.color()));
    }
}
