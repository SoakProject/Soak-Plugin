package org.soak.map;

import org.spongepowered.api.util.Color;

public class SoakColourMap {

    public static Color toSponge(org.bukkit.Color colour) {
        return Color.ofRgb(colour.asRGB());
    }

    public static org.bukkit.Color toBukkit(Color colour) {
        return org.bukkit.Color.fromRGB(colour.rgb());
    }
}
