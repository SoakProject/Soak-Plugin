package org.soak.impl.event.entity.player.data;

import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakPlugin;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.entity.living.Humanoid;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;

public class SoakExpChangeEvent extends AbstractDataEvent<Integer> {

    private final EventSingleListenerWrapper<FoodLevelChangeEvent> singleListenerWrapper;

    public SoakExpChangeEvent(EventSingleListenerWrapper<FoodLevelChangeEvent> singleListenerWrapper) {
        this.singleListenerWrapper = singleListenerWrapper;
    }

    @Override
    public Key<? extends Value<Integer>> keyValue() {
        return Keys.EXPERIENCE;
    }

    @Override
    protected boolean isCorrectHolder(DataHolder.Mutable holder) {
        return holder instanceof Humanoid;
    }

    @Override
    protected void fireEvent(ChangeDataHolderEvent.ValueChange spongeEvent, EventPriority priority, DataHolder.Mutable player, Integer changedTo, Integer changedFrom) {
        var human = new SoakPlayer((ServerPlayer) player);
        //TODO get entity that caused exp
        var event = new PlayerExpChangeEvent(human, changedTo);

        SoakPlugin.server().getPluginManager().callEvent(this.singleListenerWrapper, event, priority);
    }
}
