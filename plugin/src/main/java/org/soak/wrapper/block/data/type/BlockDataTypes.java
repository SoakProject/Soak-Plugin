package org.soak.wrapper.block.data.type;

import org.soak.utils.TagHelper;
import org.soak.wrapper.block.data.CommonBlockData;
import org.soak.wrapper.block.data.SoakBlockData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.tag.BlockTypeTags;
import org.spongepowered.api.util.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public enum BlockDataTypes {

    BED(SoakBed.class, (state) -> TagHelper.getBlockTypes(BlockTypeTags.BEDS).anyMatch(blockType -> blockType.equals(state.type())) ? 100 : 0),
    WALL_SIGN(SoakWallSign.class, (state) -> TagHelper.getBlockTypes(BlockTypeTags.WALL_SIGNS).anyMatch(blockType -> blockType.equals(state.type())) ? 100 : 0),
    BUTTON(SoakButton.class, (state) -> TagHelper.getBlockTypes(BlockTypeTags.BUTTONS).anyMatch(blockType -> blockType.equals(state.type())) ? 100 : 0),

    //GENERICS
    X_Z_ORIENTABLE(SoakXZOrientable.class, (state) -> Stream.of(BlockTypes.NETHER_PORTAL).anyMatch(blockType -> blockType.get().equals(state.type())) ? 100 : 0),

    WATER_LOGGED_4_FACING(SoakFourDirectionalWaterloggedBlockData.class,
            (state) -> state.supports(Keys.DIRECTION) && state.supports(Keys.IS_WATERLOGGED) && state.with(Keys.DIRECTION,
                    Direction.NORTHEAST).isEmpty() && state.with(Keys.DIRECTION, Direction.DOWN).isEmpty() ? 80 : 0),

    GENERIC(SoakBlockData.class, state -> 1);

    private final ToIntFunction<BlockState> likely;
    private final Class<? extends CommonBlockData> assigned;

    BlockDataTypes(Class<? extends CommonBlockData> clazz, ToIntFunction<BlockState> function) {
        this.likely = function;
        this.assigned = clazz;
    }

    public static BlockDataTypes valueFor(BlockState state) {
        return Stream.of(values()).max(Comparator.comparing(type -> type.getLikely(state))).orElse(GENERIC);
    }

    public int getLikely(BlockState state) {
        return likely.applyAsInt(state);
    }

    public CommonBlockData instance(BlockState state) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.assigned.getDeclaredConstructor(BlockState.class).newInstance(state);
    }

}
