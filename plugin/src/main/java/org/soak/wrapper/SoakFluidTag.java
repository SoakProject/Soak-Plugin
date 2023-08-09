package org.soak.wrapper;

import org.bukkit.Fluid;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakFluidTypeMap;
import org.soak.map.SoakResourceKeyMap;
import org.spongepowered.api.fluid.FluidType;

import java.util.Set;
import java.util.stream.Collectors;

public class SoakFluidTag implements Tag<Fluid> {

    private org.spongepowered.api.tag.Tag<FluidType> tag;

    public SoakFluidTag(org.spongepowered.api.tag.Tag<FluidType> tag) {
        this.tag = tag;
    }

    @Override
    public boolean isTagged(@NotNull Fluid item) {
        return this.tag.contains(SoakFluidTypeMap.toSponge(item));
    }

    @Override
    public @NotNull Set<Fluid> getValues() {
        return this.tag.values().stream().map(SoakFluidTypeMap::toBukkit).collect(Collectors.toSet());
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return SoakResourceKeyMap.mapToBukkit(this.tag.key());
    }
}
