package org.soak.wrapper.inventory.meta;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class SoakItemMeta extends AbstractItemMeta {
    public SoakItemMeta(ItemStack stack) {
        super(stack);
    }

    public SoakItemMeta(ItemStackSnapshot stack) {
        super(stack);
    }

    //shouldn't use if the type is known
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public SoakItemMeta(ValueContainer container) {
        super(container);
    }

    @Override
    public @NotNull SoakItemMeta clone() {
        return new SoakItemMeta(this.copyToSnapshot());
    }
}
