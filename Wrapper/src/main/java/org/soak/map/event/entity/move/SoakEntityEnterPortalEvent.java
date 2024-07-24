package org.soak.map.event.entity.move;

import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.soak.WrapperManager;
import org.soak.map.SoakLocationMap;
import org.soak.map.event.EventSingleListenerWrapper;
import org.soak.plugin.SoakManager;
import org.soak.utils.TagHelper;
import org.soak.wrapper.entity.AbstractEntity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.tag.BlockTypeTags;

public class SoakEntityEnterPortalEvent {

    private final EventSingleListenerWrapper<EntityPortalEnterEvent> singleEventListener;

    public SoakEntityEnterPortalEvent(EventSingleListenerWrapper<EntityPortalEnterEvent> wrapper) {
        this.singleEventListener = wrapper;
    }

    @Listener(order = Order.FIRST)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void firstEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.HIGHEST);
    }

    @Listener(order = Order.EARLY)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void earlyEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.HIGH);
    }

    @Listener(order = Order.DEFAULT)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void normalEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.NORMAL);
    }

    @Listener(order = Order.LATE)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void lateEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.LOW);
    }

    @Listener(order = Order.LAST)
    @Exclude(RespawnPlayerEvent.Recreate.class)
    public void lastEvent(MoveEntityEvent event) {
        fireEvent(event, EventPriority.LOWEST);
    }

    private void fireEvent(MoveEntityEvent event, EventPriority priority) {
        var spongeLocation = event.entity().world().location(event.destinationPosition());
        var type = spongeLocation.blockType();
        if (TagHelper.getBlockTypes(BlockTypeTags.PORTALS).noneMatch(blockType -> blockType.equals(type))) {
            return;
        }

        var originalSpongeLocation = event.entity().world().location(event.originalPosition());
        var originalType = originalSpongeLocation.blockType();
        if (TagHelper.getBlockTypes(BlockTypeTags.PORTALS).anyMatch(blockType -> blockType.equals(originalType))) {
            return;
        }

        var bukkitEntity = AbstractEntity.wrap(event.entity());
        var bukkitLocation = SoakLocationMap.toBukkit(spongeLocation);

        var bukkitEvent = new EntityPortalEnterEvent(bukkitEntity, bukkitLocation);
        SoakManager.<WrapperManager>getManager().getServer().getPluginManager().callEvent(this.singleEventListener, bukkitEvent, priority);
    }
}
