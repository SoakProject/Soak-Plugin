package org.soak.map.event;


import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.soak.impl.event.block.*;
import org.soak.impl.event.block.piston.SoakPistonExtendEvent;
import org.soak.impl.event.command.SoakPreCommandEvent;
import org.soak.impl.event.entity.SoakEntityDeathEvent;
import org.soak.impl.event.entity.SoakEntityExplosionEvent;
import org.soak.impl.event.entity.move.SoakVehicleMoveEvent;
import org.soak.impl.event.entity.move.SoakVehicleRotateEvent;
import org.soak.impl.event.entity.player.combat.SoakPlayerDeathEvent;
import org.soak.impl.event.entity.player.combat.SoakPlayerRespawnEvent;
import org.soak.impl.event.entity.player.connection.SoakPlayerJoinEvent;
import org.soak.impl.event.entity.player.connection.SoakPlayerKickEvent;
import org.soak.impl.event.entity.player.connection.SoakPlayerQuitEvent;
import org.soak.impl.event.entity.player.data.*;
import org.soak.impl.event.entity.player.interact.SoakPlayerInteractAirEvent;
import org.soak.impl.event.entity.player.interact.SoakPlayerInteractBlockEvent;
import org.soak.impl.event.entity.player.interact.move.SoakPlayerMoveEvent;
import org.soak.impl.event.entity.player.interact.move.SoakPlayerRotateEvent;
import org.soak.impl.event.entity.player.interact.move.SoakPlayerTeleportEvent;
import org.soak.impl.event.inventory.action.SoakEnchantEvent;
import org.soak.impl.event.inventory.action.SoakInventoryCloseEvent;
import org.soak.impl.event.inventory.action.click.SoakInventoryClickEvent;
import org.soak.impl.event.inventory.action.furnance.SoakBurnItemEvent;
import org.soak.impl.event.inventory.action.furnance.SoakSmeltItemEvent;
import org.soak.impl.event.inventory.hand.action.SoakItemUseEvent;
import org.soak.impl.event.world.SoakWorldLoadEvent;
import org.soak.impl.event.world.SoakWorldUnloadEvent;

public class EventClassMapping {

    public static Class<?>[] soakEventClass(Class<? extends Event> bukkitClass) {
        String name = bukkitClass.getName();
        if (name.equals(PluginEnableEvent.class.getName()) || name.equals(PluginDisableEvent.class.getName())) {
            //these are fired within Soak itself
            return array();
        }
        if (name.equals(SignChangeEvent.class.getName())) {
            return array(SoakSignChangeEvent.class);
        }
        if (name.equals(VehicleMoveEvent.class.getName())) {
            return array(SoakVehicleMoveEvent.class, SoakVehicleRotateEvent.class);
        }
        if (name.equals(PlayerMoveEvent.class.getName())) {
            return array(SoakPlayerMoveEvent.class, SoakPlayerRotateEvent.class);
        }
        if (name.equals(PlayerTeleportEvent.class.getName())) {
            return array(SoakPlayerTeleportEvent.class);
        }
        if (name.equals(InventoryCloseEvent.class.getName())) {
            return array(SoakInventoryCloseEvent.class);
        }
        if (name.equals(PlayerInteractEvent.class.getName())) {
            return array(SoakPlayerInteractBlockEvent.class, SoakPlayerInteractAirEvent.class);
        }
        if (name.equals(WorldUnloadEvent.class.getName())) {
            return array(SoakWorldUnloadEvent.class);
        }
        if (name.equals(WorldLoadEvent.class.getName())) {
            return array(SoakWorldLoadEvent.class);
        }
        if (name.equals(PlayerKickEvent.class.getName())) {
            return array(SoakPlayerKickEvent.class);
        }
        if (name.equals(PlayerJoinEvent.class.getName())) {
            return array(SoakPlayerJoinEvent.class);
        }
        if (name.equals(PlayerQuitEvent.class.getName())) {
            return array(SoakPlayerQuitEvent.class);
        }
        if (name.equals(EntityDeathEvent.class.getName())) {
            return array(SoakEntityDeathEvent.class);
        }
        if (name.equals(PlayerDeathEvent.class.getName())) {
            return array(SoakPlayerDeathEvent.class);
        }
        if (name.equals(PlayerCommandPreprocessEvent.class.getName())) {
            return array(SoakPreCommandEvent.class);
        }
        if (name.equals(PlayerToggleSprintEvent.class.getName())) {
            return array(SoakToggleSprintEvent.class);
        }
        if (name.equals(PlayerToggleSneakEvent.class.getName())) {
            return array(SoakToggleSneakEvent.class);
        }
        if (name.equals(EntityToggleGlideEvent.class.getName())) {
            return array(SoakToggleGlideEvent.class);
        }
        if (name.equals(FoodLevelChangeEvent.class.getName())) {
            return array(SoakFoodLevelChangeEvent.class);
        }
        if (name.equals(PlayerExpChangeEvent.class.getName())) {
            return array(SoakExpChangeEvent.class);
        }
        if (name.equals(PlayerRespawnEvent.class.getName())) {
            return array(SoakPlayerRespawnEvent.class);
        }
        if (name.equals(EnchantItemEvent.class.getName())) {
            return array(SoakEnchantEvent.class);
        }
        if (name.equals(FurnaceBurnEvent.class.getName())) {
            return array(SoakBurnItemEvent.class);
        }
        if (name.equals(FurnaceSmeltEvent.class.getName())) {
            return array(SoakSmeltItemEvent.class);
        }
        if (name.equals(PlayerItemConsumeEvent.class.getName())) {
            return array(SoakItemUseEvent.class);
        }
        if (name.equals(InventoryClickEvent.class.getName())) {
            return array(SoakInventoryClickEvent.class);
        }
        if (name.equals(BlockBreakEvent.class.getName())) {
            return array(SoakBlockBreakEvent.class);
        }
        if (name.equals(BlockPlaceEvent.class.getName())) {
            return array(SoakBlockPlaceEvent.class);
        }
        if (name.equals(BlockPhysicsEvent.class.getName())) {
            return array(SoakBlockPhysicsEvent.class);
        }
        if (name.equals(BlockFromToEvent.class.getName())) {
            return array(SoakBlockFlowExpandEvent.class);
        }
        if (name.equals(EntityExplodeEvent.class.getName())) {
            return array(SoakEntityExplosionEvent.class);
        }
        if (name.equals(BlockPistonExtendEvent.class.getName())) {
            return array(SoakPistonExtendEvent.class);
        }
        throw new RuntimeException("No mapping found for Bukkit Event: " + bukkitClass.getName());
    }

    private static Class<?>[] array(Class<?>... classes) {
        return classes;
    }
}
