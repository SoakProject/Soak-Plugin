package org.soak.utils;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.BlockStateKeys;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;

import java.util.stream.Stream;

@ApiStatus.Internal
public class DebugHelper {

    public static Stream<Key<? extends Value<?>>> supported(DataHolder holder) {
        return FakeRegistryHelper.<Key<? extends Value<?>>>getFields(Keys.class, Key.class).stream().filter(holder::supports);
    }

    public static Stream<Key<? extends Value<?>>> supported(BlockState state) {
        Stream<Key<? extends Value<?>>> blockStateKeys = FakeRegistryHelper.<Key<? extends Value<?>>>getFields(BlockStateKeys.class, Key.class).stream().filter(state::supports);
        var typicalKeys = supported((DataHolder) state);
        return Stream.concat(typicalKeys, blockStateKeys);
    }
}
