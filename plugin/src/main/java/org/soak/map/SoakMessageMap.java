package org.soak.map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class SoakMessageMap {

    public static String mapToBukkit(Component component){
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static Component mapToComponent(String message){
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }
}
