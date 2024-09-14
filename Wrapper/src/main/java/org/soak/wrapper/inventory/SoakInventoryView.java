package org.soak.wrapper.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.soak.WrapperManager;
import org.soak.exception.NotImplementedException;
import org.soak.map.SoakMessageMap;
import org.soak.plugin.SoakManager;
import org.soak.utils.ReflectionHelper;
import org.spongepowered.api.item.inventory.Container;

public class SoakInventoryView extends InventoryView {

    private final Container spongeContainer;

    public SoakInventoryView(Container container) {
        this.spongeContainer = container;
    }

    public @NotNull Container sponge() {
        return this.spongeContainer;
    }

    @Override
    public @NotNull Inventory getTopInventory() {
        var inventory = SoakInventory.wrap(this.spongeContainer);
        inventory.setRequestedTitle(this.title());
        return inventory;
    }

    @Override
    public @NotNull Inventory getBottomInventory() {
        return getPlayer().getInventory();
    }

    @Override
    public @NotNull HumanEntity getPlayer() {
        return SoakManager.<WrapperManager>getManager().getMemoryStore().get(this.spongeContainer.viewer());
    }

    @Override
    public @NotNull InventoryType getType() {
        return InventoryType.container(this.spongeContainer);
    }

    @Override
    public @NotNull Component title() {
        var currentMenu = this.spongeContainer.currentMenu();
        if (currentMenu.isEmpty()) {
            //if remapped inventory -> it may return null here -> this forces it out
            var player = this.spongeContainer.viewer();
            try {
                var menuProvider = ReflectionHelper.getField(player, "inventory$menuProvider");
                var spongeWrappedComponent = ReflectionHelper.getField(menuProvider, "title");
                return ReflectionHelper.getField(spongeWrappedComponent, "wrapped");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        var opTitle = currentMenu.get().title();
        return opTitle
                .orElseGet(() -> this.getType().defaultTitle());

    }

    @Override
    @Deprecated
    public @NotNull String getTitle() {
        return SoakMessageMap.mapToBukkit(title());
    }

    @Override
    public void setTitle(@NotNull String s) {
        this.spongeContainer.currentMenu().ifPresent(menu -> menu.setTitle(LegacyComponentSerializer.legacySection().deserialize(s)));
    }

    @Override
    public @NotNull String getOriginalTitle() {
        throw NotImplementedException.createByLazy(InventoryView.class, "getOriginalTitle");
    }
}
