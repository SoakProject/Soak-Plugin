package org.soak.map.event.entity.player.data;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.soak.WrapperManager;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.SoakManager;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.entity.living.Humanoid;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;

public class SoakFoodLevelChangeEvent extends AbstractDataEvent<Integer> {

    private final EventSingleListenerWrapper<FoodLevelChangeEvent> singleListenerWrapper;

    public SoakFoodLevelChangeEvent(EventSingleListenerWrapper<FoodLevelChangeEvent> singleListenerWrapper) {
        this.singleListenerWrapper = singleListenerWrapper;
    }

    @Override
    public Key<? extends Value<Integer>> keyValue() {
        return Keys.FOOD_LEVEL;
    }

    @Override
    protected boolean isCorrectHolder(DataHolder.Mutable holder) {
        return holder instanceof Humanoid;
    }

    @Override
    protected void fireEvent(ChangeDataHolderEvent.ValueChange spongeEvent, EventPriority priority, DataHolder.Mutable player, Integer changedTo, Integer changedFrom) {
        var human = (HumanEntity) AbstractEntity.wrap((Humanoid) player);
        var stack = spongeEvent.context().get(EventContextKeys.USED_ITEM).map(SoakItemStackMap::toBukkit);
        var event = new FoodLevelChangeEvent(human, changedTo, stack.orElse(null));

        SoakManager.<WrapperManager>getManager().getServer().getPluginManager().callEvent(this.singleListenerWrapper, event, priority);

        if (event.isCancelled()) {
            spongeEvent.setCancelled(event.isCancelled());
        }
    }
}
