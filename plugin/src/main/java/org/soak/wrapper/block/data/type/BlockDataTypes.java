package org.soak.wrapper.block.data.type;

import org.soak.wrapper.block.data.CommonBlockData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.tag.BlockTypeTags;
import org.spongepowered.api.util.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public enum BlockDataTypes {

    WALL_SIGN(SoakWallSign.class, (state) -> BlockTypeTags.WALL_SIGNS.get().contains(state.type()) ? 100 : 0),
    WATER_LOGGED_4_FACING(SoakFourDirectionalWaterloggedBlockData.class,
            (state) -> state.supports(Keys.DIRECTION) && state.supports(Keys.IS_WATERLOGGED) && state.with(Keys.DIRECTION,
                    Direction.NORTHEAST).isEmpty() && state.with(Keys.DIRECTION, Direction.DOWN).isEmpty() ? 80 : 0);

    private final ToIntFunction<BlockState> likely;
    private final Class<? extends CommonBlockData> assigned;

    BlockDataTypes(Class<? extends CommonBlockData> clazz, ToIntFunction<BlockState> function) {
        this.likely = function;
        this.assigned = clazz;
    }

    public static Optional<BlockDataTypes> valueFor(BlockState state) {
        return Stream.of(values()).max(Comparator.comparing(type -> type.getLikely(state)));
    }

    public int getLikely(BlockState state) {
        return likely.applyAsInt(state);
    }

    public CommonBlockData instance(BlockState state) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.assigned.getDeclaredConstructor(BlockState.class).newInstance(state);
    }

}
