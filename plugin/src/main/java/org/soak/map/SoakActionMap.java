package org.soak.map;

import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;

public class SoakActionMap {

    public static Action toBukkit(HandType type, boolean isAir) {
        if (type.equals(HandTypes.MAIN_HAND.get())) {
            return isAir ? Action.RIGHT_CLICK_AIR : Action.RIGHT_CLICK_BLOCK;
        }
        return isAir ? Action.LEFT_CLICK_AIR : Action.LEFT_CLICK_BLOCK;
    }

    public static EquipmentSlot toBukkit(HandType type){
        if(type.equals(HandTypes.MAIN_HAND.get())){
            return EquipmentSlot.HAND;
        }
        return EquipmentSlot.OFF_HAND;
    }
}
