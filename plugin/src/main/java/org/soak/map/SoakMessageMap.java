package org.soak.map;

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class SoakMessageMap {

    public static String mapToBukkit(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static Component toComponent(String message) {
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }

    public static Component toComponent(BaseComponent... components) {
        var jsonText = ComponentSerializer.toString(components);
        return GsonComponentSerializer.gson().deserialize(jsonText);
    }

    public static MessageType toComponent(ChatMessageType type) {
        switch (type) {
            case CHAT -> {
                return MessageType.CHAT;
            }
            case SYSTEM -> {
                return MessageType.SYSTEM;
            }
            case ACTION_BAR -> {
                return MessageType.SYSTEM; //TODO
            }
            default -> throw new RuntimeException("No mapping for " + type.name());
        }
    }
}
