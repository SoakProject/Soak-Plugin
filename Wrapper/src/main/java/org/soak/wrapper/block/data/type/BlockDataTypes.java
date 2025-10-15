package org.soak.wrapper.block.data.type;

import org.soak.utils.TagHelper;
import org.soak.wrapper.block.data.CommonBlockData;
import org.soak.wrapper.block.data.SoakBlockData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.tag.BlockTypeTags;
import org.spongepowered.api.util.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public enum BlockDataTypes {

    BED(SoakBed.class, TagHelper.getBlockTypes(BlockTypeTags.BEDS)),
    WALL_SIGN(SoakWallSign.class, TagHelper.getBlockTypes(BlockTypeTags.WALL_SIGNS)),
    BUTTON(SoakButton.class, TagHelper.getBlockTypes(BlockTypeTags.BUTTONS)),
    WATER_CALDRON(SoakCauldron.class, TagHelper.getBlockTypes(BlockTypeTags.CAULDRONS)),

    //GENERICS
    X_Z_ORIENTABLE(SoakXZOrientable.class, BlockTypes.NETHER_PORTAL),

    WATER_LOGGED_4_FACING(SoakFourDirectionalWaterloggedBlockData.class,
                          (state) -> state.supports(Keys.DIRECTION) && state.supports(Keys.IS_WATERLOGGED) && state.with(
                                  Keys.DIRECTION,
                                  Direction.NORTHEAST).isEmpty() && state.with(Keys.DIRECTION, Direction.DOWN)
                                  .isEmpty() ? 80 : 0),

    GENERIC(SoakBlockData.class, state -> 1);

    private final ToIntFunction<BlockState> likely;
    private final Class<? extends CommonBlockData> assigned;

    @SafeVarargs
    BlockDataTypes(Class<? extends CommonBlockData> clazz, Supplier<BlockType>... types) {
        this(clazz, List.of(types));
    }

    BlockDataTypes(Class<? extends CommonBlockData> clazz, Collection<Supplier<BlockType>> types) {
        this(clazz, state -> types.stream().anyMatch(supplier -> supplier.get().equals(state.type())) ? 100 : 0);
    }

    BlockDataTypes(Class<? extends CommonBlockData> clazz, Stream<BlockType> types) {
        this(clazz, types.<Supplier<BlockType>>map(blockType -> () -> blockType).toList());
    }

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

    public CommonBlockData instance(BlockState state)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.assigned.getDeclaredConstructor(BlockState.class).newInstance(state);
    }

}
