package org.soak.wrapper.enchantment;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.soak.map.SoakResourceKeyMap;
import org.soak.map.item.SoakEnchantmentTypeMap;
import org.soak.map.item.SoakItemStackMap;
import org.soak.plugin.exception.NotImplementedException;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Set;

public class SoakEnchantment extends Enchantment {

    private final EnchantmentType type;


    public SoakEnchantment(@NotNull EnchantmentType type) {
        super(SoakResourceKeyMap.mapToBukkit(type.key(RegistryTypes.ENCHANTMENT_TYPE)));
        this.type = type;
    }

    @Override
    public @NotNull String getName() {
        return PlainTextComponentSerializer.plainText().serialize(this.type.asComponent());
    }

    @Override
    public int getMaxLevel() {
        return this.type.maximumLevel();
    }

    @Override
    public int getStartLevel() {
        return this.type.minimumLevel();
    }

    @Override
    public boolean isTreasure() {
        return this.type.isTreasure();
    }

    @Override
    public boolean isCursed() {
        return this.type.isCurse();
    }

    @Override
    public @NotNull Component displayName(int level) {
        return this.type.asComponent(); //level makes it different?
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        throw NotImplementedException.createByLazy(Enchantment.class, "getItemTarget");
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment arg0) {
        return !this.type.isCompatibleWith(SoakEnchantmentTypeMap.toSponge(arg0));
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack arg0) {
        var spongeStack = SoakItemStackMap.toSponge(arg0);
        return this.type.canBeAppliedToStack(spongeStack);
    }

    @Override
    public boolean isTradeable() {
        throw NotImplementedException.createByLazy(Enchantment.class, "isTradeable");
    }

    @Override
    public boolean isDiscoverable() {
        throw NotImplementedException.createByLazy(Enchantment.class, "isDiscoverable");
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        throw NotImplementedException.createByLazy(Enchantment.class, "getRarity");
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityCategory arg1) {
        throw NotImplementedException.createByLazy(Enchantment.class, "getDamageIncrease", int.class, EntityCategory.class);
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        throw NotImplementedException.createByLazy(Enchantment.class, "getActiveSlots");
    }
}
