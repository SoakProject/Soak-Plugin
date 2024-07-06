package org.soak.utils;

import org.mose.collection.stream.builder.CollectionStreamBuilder;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class ListMappingUtils {

    public static <E, T> CollectionStreamBuilder.SortedRules<E, T> fromStream(CollectionStreamBuilder.Rules<E, T> builder, Supplier<Stream<E>> collectionGetter, BiPredicate<E, T> compare, Comparator<E> sort) {
        ToIntFunction<T> indexOf = value -> {
            AtomicInteger count = new AtomicInteger(0);
            collectionGetter.get().sorted(sort).filter(spongeValue -> {
                boolean is = compare.test(spongeValue, value);
                if (is) {
                    return true;
                }
                count.addAndGet(1);
                return false;
            }).findAny();
            return count.get();
        };

        return builder.withFirstIndexOf(indexOf);
    }
}
