package org.soak.plugin.data.type;

import org.soak.plugin.utils.Singleton;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class BukkitDataTypes {

    public static final BukkitStringDataType STRING = new BukkitStringDataType();

    public static Singleton<Collection<BukkitDataType<?>>> TYPES = new Singleton<>(() -> Arrays.stream(BukkitDataTypes.class.getDeclaredFields())
            .filter(field -> Modifier.isPublic(field.getModifiers()))
            .filter(field -> Modifier.isStatic(field.getModifiers()))
            .filter(field -> Modifier.isFinal(field.getModifiers()))
            .filter(field -> BukkitDataType.class.isAssignableFrom(field.getType()))
            .map(field -> {
                try {
                    return (BukkitDataType<?>) field.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
}
