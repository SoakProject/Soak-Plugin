package org.soak.map;

import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.profile.property.ProfileProperty;

public class SoakProfilePropertyMap {

    public static ProfileProperty toSponge(@NotNull com.destroystokyo.paper.profile.ProfileProperty property) {
        return ProfileProperty.of(property.getName(), property.getValue(), property.getSignature());
    }

    public static com.destroystokyo.paper.profile.ProfileProperty toBukkit(@NotNull ProfileProperty property) {
        return new com.destroystokyo.paper.profile.ProfileProperty(property.name(), property.value(), property.signature().orElse(null));
    }
}
