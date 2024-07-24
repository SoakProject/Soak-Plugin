package org.soak.map.event.entity.player.data;

import org.bukkit.event.EventPriority;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;

public abstract class AbstractDataEvent<T> {

    public abstract Key<? extends Value<T>> keyValue();

    @Listener(order = Order.FIRST)
    public void firstEvent(ChangeDataHolderEvent.ValueChange spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    public void earlyEvent(ChangeDataHolderEvent.ValueChange spongeEvent) {
        fireEvent(spongeEvent, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    public void normalEvent(ChangeDataHolderEvent.ValueChange spongeEvent) {
        fireEvent(spongeEvent, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    public void lateEvent(ChangeDataHolderEvent.ValueChange spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    public void lastEvent(ChangeDataHolderEvent.ValueChange spongeEvent) {
        fireEvent(spongeEvent, EventPriority.LOWEST);
    }

    protected abstract boolean isCorrectHolder(DataHolder.Mutable holder);

    private void fireEvent(ChangeDataHolderEvent.ValueChange spongeEvent, EventPriority priority) {
        var holder = spongeEvent.targetHolder();
        if (!isCorrectHolder(holder)) {
            return;
        }
        var result = spongeEvent.endResult();
        var opData = result.successfulValue(keyValue());
        if (opData.isEmpty()) {
            return;
        }
        var changedTo = opData.get().get();
        var original = spongeEvent.originalChanges().successfulValue(keyValue()).map(Value::get).orElse(null);

        fireEvent(spongeEvent, priority, holder, changedTo, original);
    }

    protected abstract void fireEvent(ChangeDataHolderEvent.ValueChange spongeEvent, EventPriority priority, DataHolder.Mutable player, T changedTo, T changedFrom);
}
