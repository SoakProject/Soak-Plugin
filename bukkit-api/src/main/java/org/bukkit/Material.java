package org.bukkit;

import com.google.common.collect.Multimap;
import io.papermc.paper.inventory.ItemRarity;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemType;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

//dummy class -> Not meant for production use
//look at vanillaMaterials or extended materials for actual implementation
public abstract class Material implements Keyed {

    public static final Material AIR = null;

    static @Nullable Material getMaterial(@NotNull String name) {
        return null;
    }

    public static Material[] values() {
        return new Material[0];
    }

    public static @Nullable Material matchMaterial(@NotNull String name) {
        return null;
    }

    public static @NotNull Material getBlockMaterial(BlockType type) {
        throw new RuntimeException("This is a dummy class, something didn't go right in gradle");
    }

    public static @NotNull Material getBlockMaterial(Supplier<BlockType> type) {
        throw new RuntimeException("This is a dummy class, something didn't go right in gradle");
    }

    public static @NotNull Material getItemMaterial(ItemType type) {
        throw new RuntimeException("This is a dummy class, something didn't go right in gradle");
    }

    public static @NotNull Material getItemMaterial(Supplier<ItemType> type) {
        throw new RuntimeException("This is a dummy class, something didn't go right in gradle");
    }

    public abstract Optional<BlockType> asBlock();

    public abstract Optional<ItemType> asItem();

    public abstract BlockData createBlockData();

    public abstract BlockData createBlockData(@Nullable String data);

    public abstract BlockData createBlockData(@Nullable Consumer<BlockData> consumer);

    public abstract float getBlastResistance();

    @Nullable
    public abstract Material getCraftingRemainingItem();

    //do not suggest below
    //@Deprecated @NotNull Class<? extends MaterialData> getData();
    public abstract @NotNull EquipmentSlot getEquipmentSlot();

    public abstract float getHardness();

    //do not suggest below
    //@Deprecated int getId();
    public abstract Multimap<Attribute, AttributeModifier> getItemAttributes(EquipmentSlot slot);

    public abstract ItemRarity getItemRarity();

    public abstract @NotNull NamespacedKey getKey();

    public abstract short getMaxDurability();

    public abstract int getMaxStackSize();

    public abstract String name();

    public abstract @NotNull String getTranslationKey();

    public abstract boolean hasGravity();

    public abstract boolean isAir();

    public abstract boolean isBlock();

    public abstract boolean isBurnable();

    public abstract boolean isEdible();

    public abstract boolean isEmpty();

    public abstract boolean isFlammable();

    public abstract boolean isFuel();

    public abstract boolean isInteractable();

    public abstract boolean isItem();

    public abstract boolean isOccluding();

    @Deprecated
    public abstract boolean isTransparent();

    public abstract boolean isRecord();

    public abstract boolean isSolid();


}
