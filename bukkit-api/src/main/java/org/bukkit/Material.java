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
public interface Material {

    Material AIR = null;

    static @Nullable Material getMaterial(@NotNull String name) {
        return null;
    }

    static Material[] values() {
        return new Material[0];
    }

    static @Nullable Material matchMaterial(@NotNull String name) {
        return null;
    }

    static Optional<Material> getBlockMaterial(BlockType type) {
        return Optional.empty();
    }

    static Optional<Material> getBlockMaterial(Supplier<BlockType> type) {
        return Optional.empty();
    }

    static Optional<Material> getItemMaterial(ItemType type) {
        return Optional.empty();
    }

    static Optional<Material> getItemMaterial(Supplier<ItemType> type) {
        return Optional.empty();
    }

    Optional<BlockType> asBlock();

    Optional<ItemType> asItem();

    BlockData createBlockData();

    BlockData createBlockData(@Nullable String data);

    BlockData createBlockData(@Nullable Consumer<BlockData> consumer);

    float getBlastResistance();

    @Nullable
    Material getCraftingRemainingItem();

    //do not suggest below
    //@Deprecated @NotNull Class<? extends MaterialData> getData();
    @NotNull EquipmentSlot getEquipmentSlot();

    float getHardness();

    //do not suggest below
    //@Deprecated int getId();
    Multimap<Attribute, AttributeModifier> getItemAttributes(EquipmentSlot slot);

    ItemRarity getItemRarity();

    @NotNull NamespacedKey getKey();

    short getMaxDurability();

    int getMaxStackSize();
    String name();

    @NotNull String getTranslationKey();

    boolean hasGravity();

    boolean isAir();

    boolean isBlock();

    boolean isBurnable();

    boolean isEdible();

    boolean isEmpty();

    boolean isFlammable();

    boolean isFuel();

    boolean isInteractable();

    boolean isItem();

    boolean isOccluding();

    @Deprecated
    boolean isTransparent();

    boolean isRecord();

    boolean isSolid();


}
