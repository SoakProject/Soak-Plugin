package org.soak.wrapper.block.state.capture;

import org.bukkit.block.BlockState;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CapturedBlockStateData<D, BS extends BlockState> {

    private final BiConsumer<BS, D> setter;
    private final Function<BS, D> getter;
    private @Nullable D value;

    public CapturedBlockStateData(BiConsumer<BS, D> consumer, Function<BS, D> getter) {
        this(consumer, getter, null);
    }

    public CapturedBlockStateData(BiConsumer<BS, D> consumer, Function<BS, D> getter, @Nullable D value) {
        this.getter = getter;
        this.setter = consumer;
        this.value = value;
    }

    public BiConsumer<BS, D> setter() {
        return this.setter;
    }

    public Function<BS, D> getter() {
        return this.getter;
    }

    public void setValue(D value){
        this.value = value;
    }

    public void setValue(BS blockState){
        this.setter.accept(blockState, value);
    }

    public void setValue(BS blockState, D value){
        this.value = value;
        setValue(blockState);
    }

    public @Nullable D value() {
        return this.value;
    }

    public D value(BS blockState) {
        D value = this.value;
        if (value != null) {
            return value;
        }
        return this.getter.apply(blockState);
    }
}
