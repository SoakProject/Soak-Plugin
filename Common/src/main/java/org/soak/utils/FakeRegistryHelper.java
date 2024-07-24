package org.soak.utils;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeRegistryHelper {

    private static final Map<String, Collection<?>> CACHE = new ConcurrentHashMap<>();

    public static <T> Collection<T> getFields(Class<?> from, Class<?> type) {
        return getFields(from, type, Modifier::isFinal, Modifier::isPublic, Modifier::isStatic);
    }

    public static <T> Collection<T> getFields(Class<?> from, Class<?> type, IntPredicate... modifiers) {
        String keyName = from.getName();
        Collection<?> cached = CACHE.get(keyName);
        if (cached != null) {
            return cached.stream().map(v -> (T) v).collect(Collectors.toList());
        }
        Collection<T> values = Arrays.stream(from.getDeclaredFields()).filter(field -> field.getType().isAssignableFrom(type)).filter(field -> Stream.of(modifiers).allMatch(check -> check.test(field.getModifiers()))).map(field -> {
            try {
                return (T) field.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        CACHE.put(keyName, values);
        return values;

    }
}
