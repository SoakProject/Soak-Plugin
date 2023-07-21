package org.soak.wrapper.inventory.meta;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class SoakItemMeta extends AbstractItemMeta {
    public SoakItemMeta(ItemStack stack) {
        super(stack);
    }

    public SoakItemMeta(ItemStackSnapshot stack) {
        super(stack);
    }

    @Override
    public @NotNull ItemMeta clone() {
        return new SoakItemMeta(this.copyToSnapshot());
    }
}
