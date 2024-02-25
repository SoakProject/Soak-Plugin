package org.soak.wrapper.block.data.type;

import org.soak.utils.TagHelper;
import org.soak.wrapper.block.data.CommonBlockData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.tag.BlockTypeTags;
import org.spongepowered.api.util.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public enum BlockDataTypes {

    BED(SoakBed.class, (state) -> TagHelper.getBlockTypes(BlockTypeTags.BEDS).anyMatch(blockType -> blockType.equals(state.type())) ? 100 : 0),
    WALL_SIGN(SoakWallSign.class, (state) -> TagHelper.getBlockTypes(BlockTypeTags.WALL_SIGNS).anyMatch(blockType -> blockType.equals(state.type())) ? 100 : 0),

    //GENERICS
    X_Z_ORIENTABLE(SoakXZOrientable.class, (state) -> Stream.of(BlockTypes.NETHER_PORTAL).anyMatch(blockType -> blockType.get().equals(state.type())) ? 100 : 0),

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
        var result = Stream.of(values()).max(Comparator.comparing(type -> type.getLikely(state)));
        if (state.type().equals(BlockTypes.NETHER_PORTAL.get())) {
            System.out.println(result.map(Enum::name).orElse(""));
        }
        return result;
    }

    public int getLikely(BlockState state) {
        return likely.applyAsInt(state);
    }

    public CommonBlockData instance(BlockState state) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.assigned.getDeclaredConstructor(BlockState.class).newInstance(state);
    }

}
