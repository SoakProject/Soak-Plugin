package org.soak.plugin.utils;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.Value;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DataHelper {

    public static <T extends DataHolder> T copyInto(T copyTo, DataHolder copyFrom) {
        Set<Key<?>> keys = copyFrom.getKeys().stream().filter(copyTo::supports).collect(Collectors.toSet());
        for (Key<?> key : keys) {
            if (copyTo instanceof DataHolder.Mutable) {
                copyTo = (T) copyToMutable((DataHolder.Mutable) copyTo, copyFrom, key);
            } else {
                copyTo = (T) copyToImmutable((DataHolder.Immutable<?>) copyTo, copyFrom, key);
            }
        }
        return copyTo;
    }

    private static <DH extends DataHolder.Mutable, V> DH copyToMutable(DH copyTo, DataHolder copyFrom, Key<?> keyUnmapped) {
        Key<? extends Value<V>> key = (Key<? extends Value<V>>) keyUnmapped;
        Optional<V> opValue = copyFrom.get(key);
        opValue.ifPresent(v -> copyTo.offer(key, v));
        return copyTo;
    }

    private static <T extends DataHolder.Immutable<?>, V> T copyToImmutable(T copyTo, DataHolder copyFrom, Key<?> keyUnmapped) {
        Key<? extends Value<V>> key = (Key<? extends Value<V>>) keyUnmapped;
        Optional<V> opValue = copyFrom.get(key);
        if (opValue.isEmpty()) {
            return copyTo;
        }
        return copyTo.with(key, opValue.get()).map(t -> (T) t).orElseThrow(() -> new RuntimeException("Key was not checked if supported"));
    }
}
