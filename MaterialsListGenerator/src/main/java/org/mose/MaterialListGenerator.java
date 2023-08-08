package org.mose;

import org.bukkit.Material;

public class MaterialListGenerator {

    public static void main(String[] args) {
        for (Material material : Material.values()) {
            if(material.isLegacy()){
                continue;
            }
            String blockType = "null";
            if (material.isBlock()) {
                blockType = "BlockTypes." + material.name();
            }

            String itemType = "null";
            if (material.isItem()) {
                itemType = "ItemTypes." + material.name();
            }
            System.out.println(material.name() + "(" + blockType + ", " + itemType + "),");
        }
    }
}
