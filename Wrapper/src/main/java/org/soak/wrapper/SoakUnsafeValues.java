package org.soak.wrapper;

import com.google.common.collect.Multimap;
import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.exception.NotImplementedException;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class SoakUnsafeValues implements UnsafeValues {
    @Override
    public GsonComponentSerializer colorDownsamplingGsonComponentSerializer() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "colorDownsamplingGsonComponentSerializer");
    }

    @Override
    public ComponentFlattener componentFlattener() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "componentFlattener");
    }

    @Override
    public GsonComponentSerializer gsonComponentSerializer() {
        return GsonComponentSerializer.gson();
    }

    @Override
    public LegacyComponentSerializer legacyComponentSerializer() {
        return LegacyComponentSerializer.legacySection();
    }

    @Override
    public Component resolveWithContext(Component component, CommandSender commandSender, Entity entity, boolean b) throws IOException {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "resolveWithContext", Component.class, CommandSender.class, Entity.class, boolean.class);
    }

    @Override
    public void reportTimings() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "reportTimings");
    }

    @Override
    public Material toLegacy(Material arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "toLegacy", Material.class);
    }

    @Override
    public Material fromLegacy(MaterialData arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "fromLegacy", MaterialData.class, boolean.class);
    }

    @Override
    public Material fromLegacy(MaterialData arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "fromLegacy", MaterialData.class);
    }

    @Override
    public BlockData fromLegacy(Material arg0, byte arg1) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "fromLegacy", Material.class, byte.class);
    }

    @Override
    public Material fromLegacy(Material arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "fromLegacy", Material.class);
    }

    @Override
    public Material getMaterial(String arg0, int arg1) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getMaterial", String.class, int.class);
    }

    @Override
    public int getDataVersion() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getDataVersion");
    }

    @Override
    public ItemStack modifyItemStack(ItemStack arg0, String arg1) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "modifyItemStack", ItemStack.class, String.class);
    }

    @Override
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {
        String apiVersion = pdf.getAPIVersion();
        if (apiVersion == null) {
            throw new InvalidPluginException("Legacy plugins are not supported on Soak");
        }
    }

    @Override
    public byte[] processClass(PluginDescriptionFile arg0, String arg1, byte[] arg2) {
        //TODO no idea what this does
        return arg2;
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey arg0, String arg1) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "loadAdvancement", NamespacedKey.class, String.class);
    }

    @Override
    public boolean removeAdvancement(NamespacedKey arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "removeAdvancement", NamespacedKey.class);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material material, EquipmentSlot equipmentSlot) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getDefaultAttributeModifiers", Material.class, EquipmentSlot.class);
    }

    @Override
    public CreativeCategory getCreativeCategory(Material material) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getCreativeCategory", Material.class);
    }

    @Override
    public String getBlockTranslationKey(Material material) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getBlockTranslationKey", Material.class);
    }

    @Override
    public String getItemTranslationKey(Material material) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getItemTranslationKey", Material.class);
    }

    @Override
    public String getTimingsServerName() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getTimingsServerName");
    }

    @Override
    public byte[] serializeItem(ItemStack arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "serializeItem", ItemStack.class);
    }

    @Override
    public ItemStack deserializeItem(byte[] arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "deserializeItem", byte[].class);
    }

    @Override
    public byte[] serializeEntity(Entity entity) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "serializeEntity", Entity.class);
    }

    @Override
    public Entity deserializeEntity(byte[] bytes, World world, boolean b) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "deserializeEntity", byte.class, World.class, boolean.class);
    }

    @Override
    public String getTranslationKey(ItemStack arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getTranslationKey", ItemStack.class);
    }

    @Override
    public @Nullable FeatureFlag getFeatureFlag(@NotNull NamespacedKey namespacedKey) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getFeatureFlag", NamespacedKey.class);
    }

    @Override
    public String getTranslationKey(EntityType arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getTranslationKey", EntityType.class);
    }

    @Override
    public int nextEntityId() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "nextEntityId");
    }

    @Override
    public @NotNull String getMainLevelName() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getMainLevelName");
    }

    @Override
    public ItemRarity getItemRarity(Material arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getItemRarity", Material.class);
    }

    @Override
    public ItemRarity getItemStackRarity(ItemStack arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getItemStackRarity", ItemStack.class);
    }

    @Override
    public boolean isValidRepairItemStack(@NotNull ItemStack arg0, @NotNull ItemStack arg1) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "isValidRepairItemStack", ItemStack.class, ItemStack.class);
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getItemAttributes(@NotNull Material material, @NotNull EquipmentSlot equipmentSlot) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getItemAttributes", Material.class, EquipmentSlot.class);
    }

    @Override
    public int getProtocolVersion() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getProtocolVersion");
    }

    @Override
    public boolean hasDefaultEntityAttributes(@NotNull NamespacedKey namespacedKey) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "hasDefaultEntityAttributes", NamespacedKey.class);
    }

    @Override
    public @NotNull Attributable getDefaultEntityAttributes(@NotNull NamespacedKey namespacedKey) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getDefaultEntityAttributes", NamespacedKey.class);
    }

    @Override
    public boolean isCollidable(@NotNull Material material) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "isCollidable", Material.class);
    }

    @Override
    public @NotNull NamespacedKey getBiomeKey(RegionAccessor regionAccessor, int i, int i1, int i2) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "setBiomeKey", RegionAccessor.class, int.class, int.class, int.class);
    }

    @Override
    public void setBiomeKey(RegionAccessor regionAccessor, int i, int i1, int i2, NamespacedKey namespacedKey) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "setBiomeKey", RegionAccessor.class, int.class, int.class, int.class, NamespacedKey.class);
    }

    @Override
    public boolean isSupportedApiVersion(String arg0) {
        //example: 1.19
        DefaultArtifactVersion thisVersion = new DefaultArtifactVersion(arg0);
        DefaultArtifactVersion minVersion = new DefaultArtifactVersion("1.13");
        return thisVersion.compareTo(minVersion) >= 0;
    }

    @Override
    public PlainComponentSerializer plainComponentSerializer() {
        return PlainComponentSerializer.plain();
    }

    @Override
    public PlainTextComponentSerializer plainTextSerializer() {
        return PlainTextComponentSerializer.plainText();
    }

}