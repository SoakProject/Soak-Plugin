package org.soak.wrapper;

import com.google.common.collect.Multimap;
import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;

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
        throw NotImplementedException.createByLazy(UnsafeValues.class, "gsonComponentSerializer");
    }

    @Override
    public LegacyComponentSerializer legacyComponentSerializer() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "legacyComponentSerializer");
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
    public String getTranslationKey(Material arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getTranslationKey", Material.class);
    }

    @Override
    public String getTranslationKey(ItemStack arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getTranslationKey", ItemStack.class);
    }

    @Override
    public String getTranslationKey(EntityType arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getTranslationKey", EntityType.class);
    }

    @Override
    public String getTranslationKey(Block arg0) {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "getTranslationKey", Block.class);
    }

    @Override
    public int nextEntityId() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "nextEntityId");
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
    public boolean isSupportedApiVersion(String arg0) {
        //example: 1.19
        DefaultArtifactVersion thisVersion = new DefaultArtifactVersion(arg0);
        DefaultArtifactVersion minVersion = new DefaultArtifactVersion("1.13");
        return thisVersion.compareTo(minVersion) >= 0;
    }

    @Override
    public PlainComponentSerializer plainComponentSerializer() {
        throw NotImplementedException.createByLazy(UnsafeValues.class, "plainComponentSerializer");
    }

}