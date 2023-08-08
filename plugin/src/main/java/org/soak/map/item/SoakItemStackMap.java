package org.soak.map.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.soak.utils.SnapshotHelper;
import org.soak.wrapper.inventory.meta.*;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.function.Function;

public class SoakItemStackMap {

    public static org.bukkit.inventory.ItemStack toBukkit(@NotNull ItemStack stack) {
        return toBukkit(stack.type(), stack.quantity(), stack);
    }

    public static org.bukkit.inventory.ItemStack toBukkit(@NotNull ItemStackSnapshot stack) {
        return toBukkit(stack.type(), stack.quantity(), stack);
    }

    public static ItemStack toSponge(@NotNull ItemMeta meta) {
        return toSponge(meta, AbstractItemMeta::asStack);
    }

    public static ItemStackSnapshot toSpongeSnapshot(@NotNull ItemMeta meta) {
        return toSponge(meta, AbstractItemMeta::asSnapshot);
    }

    public static ItemStack toSponge(@NotNull org.bukkit.inventory.ItemStack meta) {
        if (!meta.hasItemMeta()) {
            return ItemStack.of(meta.getType()
                    .asItem()
                    .orElseThrow(() -> new RuntimeException("Material of " + meta.getType()
                            .name() + " is not an item")), meta.getAmount());
        }
        return toSponge(meta, AbstractItemMeta::asStack);
    }

    public static ItemStackSnapshot toSpongeSnapshot(@NotNull org.bukkit.inventory.ItemStack meta) {
        return toSponge(meta, AbstractItemMeta::asSnapshot);
    }

    private static <VC extends ValueContainer> VC toSponge(@NotNull ItemMeta meta, @NotNull Function<AbstractItemMeta, VC> spongeType) {
        if (!(meta instanceof AbstractItemMeta im)) {
            throw new RuntimeException(meta.getClass().getName() + " does not extend AbstractItemMeta");
        }
        return spongeType.apply(im);
    }

    private static <VC extends ValueContainer> VC toSponge(@NotNull org.bukkit.inventory.ItemStack stack, @NotNull Function<AbstractItemMeta, VC> spongeType) {
        if (!stack.hasItemMeta()) {
            throw new RuntimeException(
                    "ItemStack provided does not have any meta. This should be checked for in the previous method");
        }
        VC valueContainer = toSponge(stack.getItemMeta(), spongeType);

        //needs to update the quantity from Bukkit's ItemStack
        if (valueContainer instanceof ItemStack spongeStack) {
            spongeStack.setQuantity(stack.getAmount());
            return (VC) spongeStack;
        }
        ItemStackSnapshot snapshot = (ItemStackSnapshot) valueContainer;
        return (VC) SnapshotHelper.copyWithQuantity(snapshot, stack.getAmount());
    }

    private static org.bukkit.inventory.ItemStack toBukkit(@NotNull ItemType type, int amount, @NotNull ValueContainer container) {
        org.bukkit.inventory.ItemStack stack = new org.bukkit.inventory.ItemStack(Material.getItemMaterial(type),
                amount);
        stack.setItemMeta(toBukkitMeta(container));
        return stack;
    }

    @SuppressWarnings("deprecation")
    public static AbstractItemMeta toBukkitMeta(@NotNull ValueContainer container) {
        ItemType type = container instanceof ItemStack ? ((ItemStack) container).type() : ((ItemStackSnapshot) container).type();
        if (container.supports(Keys.POTION_EFFECTS)) {
            return new SoakPotionItemMeta(container);
        }
        if (container.supports(Keys.FIREWORK_EFFECTS)) {
            return new SoakFireworkEffectMeta(container);
        }
        if (type.equals(ItemTypes.PLAYER_HEAD.get()) || container.supports(Keys.SKIN_PROFILE_PROPERTY)) {
            return new SoakSkullMeta(container);
        }
        return new SoakItemMeta(container);
    }
}
