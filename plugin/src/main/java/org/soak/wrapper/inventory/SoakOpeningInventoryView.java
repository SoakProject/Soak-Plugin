package org.soak.wrapper.inventory;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.soak.exception.NotImplementedException;
import org.soak.wrapper.entity.living.human.SoakPlayer;

//used if a inventory has been requested to open, but another action is being done
public class SoakOpeningInventoryView extends InventoryView {

    private Inventory top;
    private SoakPlayer player;
    private String title;

    public SoakOpeningInventoryView(Inventory top, SoakPlayer player, String title) {
        this.top = top;
        this.player = player;
        this.title = title;
    }

    @Override
    public @NotNull Inventory getTopInventory() {
        return top;
    }

    @Override
    public @NotNull Inventory getBottomInventory() {
        return getPlayer().getInventory();
    }

    @Override
    public @NotNull SoakPlayer getPlayer() {
        return this.player;
    }

    @Override
    public @NotNull InventoryType getType() {
        return this.top.getType();
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(@NotNull String s) {
        this.title = s;
    }

    @Override
    public @NotNull String getOriginalTitle() {
        throw NotImplementedException.createByLazy(InventoryView.class, "getOriginalTitle");
    }
}
