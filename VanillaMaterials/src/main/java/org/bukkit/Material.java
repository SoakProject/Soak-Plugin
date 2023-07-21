package org.bukkit;

import com.google.common.collect.Multimap;
import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.Constants;
import org.soak.map.SoakResourceKeyMap;
import org.soak.plugin.exception.NotImplementedException;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.MatterTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.tag.ItemTypeTags;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum Material {
    ;

    private final @Nullable Supplier<ItemType> itemType;
    private final @Nullable Supplier<BlockType> blockType;

    Material(@Nullable Supplier<BlockType> blockType, @Nullable Supplier<ItemType> type) {
        this.blockType = blockType;
        this.itemType = type;
    }

    public static @Nullable Material matchMaterial(@NotNull String name) {
        return Stream.of(values()).filter(mat -> mat.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static @Nullable Material getMaterial(@NotNull String name) {
        return matchMaterial(name);
    }

    private BlockType asBlockType() {
        if (this.blockType == null) {
            throw new IllegalStateException("Material of " + this.name() + " is not a block");
        }
        return this.blockType.get();
    }

    private ItemType asItemType() {
        if (this.itemType == null) {
            throw new IllegalStateException("Material of " + this.name() + " is not a item");
        }
        return this.itemType.get();
    }

    public Optional<BlockType> asBlock() {
        if (this.blockType == null) {
            return Optional.empty();
        }
        return Optional.of(this.blockType.get());
    }

    public Optional<ItemType> asItem() {
        if (this.itemType == null) {
            return Optional.empty();
        }
        return Optional.of(this.itemType.get());
    }

    public BlockData createBlockData() {
        throw NotImplementedException.createByLazy(Material.class, "createBlockData");
    }

    public BlockData createBlockData(@Nullable String data) {
        throw NotImplementedException.createByLazy(Material.class, "createBlockData", String.class);
    }

    public BlockData createBlockData(@Nullable Consumer<BlockData> consumer) {
        throw NotImplementedException.createByLazy(Material.class, "createBlockData", Consumer.class);
    }

    public float getBlastResistance() {
        throw NotImplementedException.createByLazy(Material.class, "getBlastResistance");
    }

    public @Nullable Material getCraftingRemainingItem() {
        throw NotImplementedException.createByLazy(Material.class, "getCraftingRemainingItem");
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        throw NotImplementedException.createByLazy(Material.class, "getEquipmentSlot");
    }

    public @NotNull EquipmentSlot getHardness() {
        throw NotImplementedException.createByLazy(Material.class, "getHardness");
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getItemAttributes(EquipmentSlot slot) {
        throw NotImplementedException.createByLazy(Material.class, "getHardness");
    }

    public @NotNull ItemRarity getItemRarity() {
        //this.itemType.get().rarity();
        throw NotImplementedException.createByLazy(Material.class, "getHardness");
    }

    public @NotNull NamespacedKey getKey() {
        if (this.itemType != null) {
            ResourceKey key = this.itemType.get().key(RegistryTypes.ITEM_TYPE);
            return SoakResourceKeyMap.mapToBukkit(key);
        }
        ResourceKey key = this.asBlockType().key(RegistryTypes.BLOCK_TYPE);
        return SoakResourceKeyMap.mapToBukkit(key);
    }

    public short getMaxDurability() {
        return (short) this.asItemType().getInt(Keys.MAX_DURABILITY).orElse(Constants.NOT_APPLICABLE_INT);
    }

    public int getMaxStackSize() {
        return this.asItemType().maxStackQuantity();
    }

    private @Nullable String getTranslationKey(ComponentLike item) {
        Component component = item.asComponent();
        if (component instanceof TranslatableComponent translate) {
            return translate.key();
        }
        return null;
    }

    public @NotNull String getTranslationKey() {
        if (this.itemType != null) {
            String key = getTranslationKey(this.itemType.get());
            if (key != null) {
                return key;
            }
        }
        if (this.blockType != null) {
            String key = getTranslationKey(this.blockType.get());
            if (key != null) {
                return key;
            }
        }
        throw new IllegalStateException("The material of " + this.name() + " does not have a translation key. Bukkit does not expect this.");
    }

    public boolean hasGravity() {
        return this.asBlockType().get(Keys.IS_GRAVITY_AFFECTED).orElse(false);
    }

    public boolean isAir() {
        return this.asBlockType().get(Keys.MATTER_TYPE).map(matter -> matter.equals(MatterTypes.GAS.get())).orElse(false);
    }

    public boolean isBlock() {
        return this.blockType != null;
    }

    //isnt this the same as isFlammable?
    public boolean isBurnable() {
        return this.asBlockType().get(Keys.IS_FLAMMABLE).orElse(false);
    }

    public boolean isEdible() {
        throw NotImplementedException.createByLazy(Material.class, "isEdible");
    }

    public boolean isEmpty() {
        throw NotImplementedException.createByLazy(Material.class, "isEmpty");
    }

    public boolean isFlammable() {
        return this.asBlockType().get(Keys.IS_FLAMMABLE).orElse(false);
    }

    public boolean isFuel() {
        throw NotImplementedException.createByLazy(Material.class, "isFuel");
    }

    public boolean isInteractable() {
        throw NotImplementedException.createByLazy(Material.class, "isInteractable");
    }

    public boolean isItem() {
        return this.itemType != null;
    }

    public boolean isOccluding() {
        throw NotImplementedException.createByLazy(Material.class, "isOccluding");
    }

    public boolean isRecord() {
        return ItemTypeTags.MUSIC_DISCS.get().contains(this.asItemType());
    }

    public boolean isSolid() {
        return this.asItemType().get(Keys.MATTER_TYPE).map(matter -> matter.equals(MatterTypes.SOLID.get())).orElse(false);
    }

}
