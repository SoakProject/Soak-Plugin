package org.soak.map;

import org.bukkit.Material;
import org.soak.generate.bukkit.MaterialList;
import org.spongepowered.api.block.BlockType;

public class SoakBlockMap {

    //come on ... remove Material .... please
    public static Material toBukkit(BlockType type) {
        return MaterialList.value(type);
    }

}
