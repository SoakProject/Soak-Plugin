package org.soak.wrapper.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakMessageMap;
import org.soak.plugin.SoakPlugin;
import org.soak.exception.NotImplementedException;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;

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
        return SoakInventory.wrap(this.spongeContainer);
    }

    @Override
    public @NotNull Inventory getBottomInventory() {
        return getPlayer().getInventory();
    }

    @Override
    public @NotNull HumanEntity getPlayer() {
        return SoakPlugin.plugin().getMemoryStore().get(this.spongeContainer.viewer());
    }

    @Override
    public @NotNull InventoryType getType() {
        return InventoryType.container(this.spongeContainer);
    }

    @Override
    public @NotNull Component title() {
        return this.spongeContainer.currentMenu()
                .flatMap(InventoryMenu::title)
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
