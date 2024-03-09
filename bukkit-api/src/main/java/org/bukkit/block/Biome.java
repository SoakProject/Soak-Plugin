package org.bukkit.block;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

//template class
public enum Biome {

    ;

    public @NotNull org.spongepowered.api.world.biome.Biome asSponge(){
        throw new RuntimeException();
    }

    public @NotNull NamespacedKey getKey(){
        throw new RuntimeException();
    }

    public @NotNull String translationKey(){
        throw new RuntimeException();
    }

    public static Biome fromSponge(org.spongepowered.api.world.biome.Biome biome){
        throw new RuntimeException();
    }



}
