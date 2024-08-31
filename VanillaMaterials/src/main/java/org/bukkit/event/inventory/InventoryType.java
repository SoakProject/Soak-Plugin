package org.bukkit.event.inventory;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.exception.NotImplementedException;
import org.soak.plugin.SoakManager;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.entity.carrier.*;
import org.spongepowered.api.block.entity.carrier.furnace.BlastFurnace;
import org.spongepowered.api.block.entity.carrier.furnace.Furnace;
import org.spongepowered.api.block.entity.carrier.furnace.Smoker;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.inventory.crafting.CraftingOutput;
import org.spongepowered.api.item.inventory.slot.EquipmentSlot;
import org.spongepowered.api.item.inventory.slot.FuelSlot;
import org.spongepowered.api.item.inventory.slot.OutputSlot;
import org.spongepowered.api.item.inventory.type.BlockEntityInventory;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.api.item.merchant.Merchant;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum InventoryType {

    ANVIL("", ContainerTypes.ANVIL),
    BARREL("", container -> isCarrier(container, Barrel.class), ContainerTypes.GENERIC_3X3),
    BEACON("", container -> isBlockEntity(container, BlockTypes.BEACON), ContainerTypes.BEACON),
    BLAST_FURNACE("", container -> isCarrier(container, BlastFurnace.class), ContainerTypes.BLAST_FURNACE),
    BREWING("", container -> isCarrier(container, BrewingStand.class), ContainerTypes.BREWING_STAND),
    CARTOGRAPHY("",
            container -> isBlockEntity(container, BlockTypes.CARTOGRAPHY_TABLE),
            ContainerTypes.CARTOGRAPHY_TABLE),
    CHEST("ChestMenu", container -> isBlockEntity(container, BlockTypes.CHEST),
            ContainerTypes.GENERIC_9X6,
            ContainerTypes.GENERIC_9X5,
            ContainerTypes.GENERIC_9X4,
            ContainerTypes.GENERIC_9X3,
            ContainerTypes.GENERIC_9X2,
            ContainerTypes.GENERIC_9X1),
    CRAFTING("", ContainerTypes.CRAFTING), //this is players 2x2 crafting
    CREATIVE(""), //there is no ContainerType of CREATIVE. In Sponge attempting to get the container type from a creative inventory returns null
    DISPENSER("", container -> isCarrier(container, Dispenser.class), ContainerTypes.HOPPER),
    DROPPER("", container -> isCarrier(container, Dropper.class), ContainerTypes.HOPPER),
    ENCHANTING("", container -> isBlockEntity(container, BlockTypes.ENCHANTING_TABLE), ContainerTypes.ENCHANTMENT),
    ENDER_CHEST("", container -> isBlockEntity(container, BlockTypes.ENDER_CHEST), ContainerTypes.GENERIC_9X3),
    FURNACE("", container -> isCarrier(container, Furnace.class), ContainerTypes.FURNACE),
    GRINDSTONE("", container -> isBlockEntity(container, BlockTypes.GRINDSTONE), ContainerTypes.GRINDSTONE),
    HOPPER("", container -> isCarrier(container, Hopper.class), ContainerTypes.HOPPER),
    LECTERN("", container -> isBlockEntity(container, BlockTypes.LECTERN), ContainerTypes.LECTERN),
    LOOM("", container -> isBlockEntity(container, BlockTypes.LOOM), ContainerTypes.LOOM),
    MERCHANT("", container -> isCarrier(container, Merchant.class), ContainerTypes.MERCHANT),
    PLAYER(""), //failsafe is creative .... maybe should have a failsafe for when the player is not in creative
    SHULKER_BOX("", container -> isCarrier(container, ShulkerBox.class), ContainerTypes.SHULKER_BOX),
    SMITHING("", container -> isBlockEntity(container, BlockTypes.ANVIL), ContainerTypes.ANVIL),
    SMOKER("", container -> isCarrier(container, Smoker.class), ContainerTypes.SMOKER),
    STONECUTTER("", container -> isBlockEntity(container, BlockTypes.STONECUTTER), ContainerTypes.STONECUTTER),
    WORKBENCH("", container -> isBlockEntity(container, BlockTypes.CRAFTING_TABLE), ContainerTypes.CRAFTING);

    private final Supplier<ContainerType>[] spongeType;
    private final @Nullable Predicate<Container> isInventory;
    private final String nmsClassNames; //really don't like using NMS as it prevents the plugin from working on unoffical platforms, but also doesn't sit right that a platform simulator (which can't emulate that platforms NMS) uses NMS itself. So ideally find another way

    private InventoryType(String nmsClassName, Supplier<ContainerType>... type) {
        this(nmsClassName, null, type);
    }

    private InventoryType(String nmsClassName, @Nullable Predicate<Container> predicate, Supplier<ContainerType>... type) {
        this.isInventory = predicate;
        this.spongeType = type;
        this.nmsClassNames = nmsClassName;
    }

    private static boolean isBlockEntity(Inventory inventory, Supplier<BlockType>... anyBlockTypes) {
        if (!(inventory instanceof BlockEntityInventory<?>)) {
            return false;
        }
        return ((BlockEntityInventory<?>) inventory).blockEntity()
                .map(entity -> Arrays.stream(anyBlockTypes)
                        .map(Supplier::get)
                        .anyMatch(blockType -> entity.block().type().equals(blockType)))
                .orElse(false);
    }

    private static boolean isCarrier(Inventory inventory, Class<? extends Carrier> blockEntityType) {
        if (!(inventory instanceof BlockEntityInventory<?>)) {
            return false;
        }
        return ((BlockEntityInventory<?>) inventory).blockEntity()
                .map(blockEntityType::isInstance)
                .orElse(false);
    }

    public static Optional<InventoryType> container(ResourceKey key) {
        return Arrays.stream(values())
                .filter(type -> type.sponge().stream().anyMatch(ct -> ct.key(RegistryTypes.CONTAINER_TYPE).equals(key)))
                .findAny();
    }

    public static InventoryType container(Container container) {
        var opType = Arrays.stream(values())
                .filter(type -> type.isInventory != null)
                .filter(type -> type.isInventory.test(container))
                .findAny();
        if (opType.isPresent()) {
            return opType.get();
        }
        //failsafe -> find a none NMS way to do this please
        var className = container.getClass().getName();
        return Arrays.stream(values())
                .filter(type -> className.endsWith(type.nmsClassNames))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find container for " + container.getClass().getName()));
    }

    private Collection<ContainerType> sponge() {
        if (this.spongeType == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.spongeType).map(Supplier::get).collect(Collectors.toList());
    }

    public String getDefaultTitle() {
        return this.sponge().stream()
                .map(ct -> ct.key(RegistryTypes.CONTAINER_TYPE).value())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not get container type for " + this.name()));
    }

    public Component defaultTitle() {
        return Component.text(this.getDefaultTitle());
    }

    public boolean isCraftable() {
        throw NotImplementedException.createByLazy(InventoryType.class, "isCraftable");
    }

    public int getDefaultSize() {
        //this is horrible .... but works
        return this
                .sponge()
                .stream()
                .mapToInt(ct -> ViewableInventory
                        .builder()
                        .type(ct)
                        .completeStructure()
                        .plugin(SoakManager.getManager().getOwnContainer())
                        .build()
                        .capacity())
                .max()
                .orElseThrow(() -> new IllegalStateException("Shouldnt be possible: type: " + name()));
    }

    public static enum SlotType {
        ARMOR(EquipmentSlot.class),
        CONTAINER(slot -> slot.root() instanceof Container),
        CRAFTING(CraftingOutput.class),
        FUEL(FuelSlot.class),
        OUTSIDE(slot -> false),
        QUICKBAR(slot -> false), //no idea
        RESULT(OutputSlot.class);

        private final @NotNull Predicate<Slot> isSlot;

        SlotType(Class<? extends Slot> slotClass) {
            this(slotClass::isInstance);
        }

        SlotType(@NotNull Predicate<Slot> predicate) {
            this.isSlot = predicate;
        }

        public static SlotType typeFor(Slot slot) {
            return Stream.of(values())
                    .filter(type -> type.isMappingFor(slot))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Unknown type of " + slot.getClass().getName()));
        }

        public boolean isMappingFor(Slot slot) {
            return this.isSlot.test(slot);
        }
    }
}
