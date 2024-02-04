package org.soak.wrapper.plugin;

import io.papermc.paper.plugin.PermissionManager;
import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.impl.event.EventSingleListenerWrapper;
import org.soak.map.event.EventClassMapping;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.plugin.loader.common.SoakPluginContainer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.permission.PermissionService;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Supplier;

public class SoakPluginManager implements SimpPluginManager {

    private final Supplier<PluginManager> spongePluginManager;
    private final Collection<PluginLoader> loaders = new LinkedTransferQueue<>();
    private final Collection<EventSingleListenerWrapper<?>> events = new HashSet<>();

    public SoakPluginManager(Supplier<PluginManager> manager) {
        this.spongePluginManager = manager;
    }

    public PluginManager spongeManager() {
        return this.spongePluginManager.get();
    }


    @Override
    public @Nullable Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        Throwable e = null;
        for (PluginLoader loader : this.loaders) {
            try {
                return loader.loadPlugin(file);
            } catch (Exception ex) {
                e = ex;
            }
        }
        if (e == null) {
            throw new RuntimeException("Unknown error loading " + file.getPath());
        }
        if (e instanceof InvalidPluginException) {
            throw (InvalidPluginException) e;
        }
        if (e instanceof UnknownDependencyException) {
            throw (UnknownDependencyException) e;
        }
        throw (RuntimeException) e;
    }

    @Override
    public boolean isPluginEnabled(@Nullable Plugin plugin) {
        return Sponge
                .pluginManager()
                .plugins()
                .stream()
                .filter(container -> container instanceof SoakPluginContainer)
                .map(container -> (SoakPluginContainer) container)
                .anyMatch(container -> container.plugin().equals(plugin));
    }

    @Override
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(Class.class);
    }

    @Override
    public @NotNull Plugin[] getPlugins() {
        return this
                .spongeManager()
                .plugins()
                .stream()
                .filter(container -> container instanceof SoakPluginContainer)
                .map(container -> ((SoakPluginContainer) container).plugin())
                .toArray(Plugin[]::new);
    }

    @Override
    public void callEvent(@NotNull Event event) throws IllegalStateException {
        for (EventPriority priority : EventPriority.values()) {
            callEvent(event, priority);
        }
    }

    public void callEvent(@NotNull Event event, EventPriority priority) throws IllegalStateException {
        this
                .events
                .stream()
                .filter(wrapper -> wrapper.event().getName().equals(event.getClass().getName()))
                .filter(wrapper -> wrapper.priority().equals(priority))
                .forEach(wrapper -> callEvent(wrapper, event, priority));
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void callEvent(EventSingleListenerWrapper<?> wrapper, T event, EventPriority priority) {
        ((EventSingleListenerWrapper<T>) wrapper).invoke(event, priority);
    }

    @Override
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {
        Collection<EventSingleListenerWrapper<?>> events = EventSingleListenerWrapper.findEventHandlers(plugin,
                listener);
        for (var wrapper : events) {
            registerEvent(wrapper);
        }
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled) {
        EventSingleListenerWrapper<?> wrapper = new EventSingleListenerWrapper<>(listener,
                plugin,
                event,
                priority,
                ignoreCancelled);
        registerEvent(wrapper);
    }

    private void registerEvent(EventSingleListenerWrapper<?> eventWrapper) {
        this.events.add(eventWrapper);
        SoakPluginContainer pluginContainer = SoakPlugin.plugin().getPlugin(eventWrapper.plugin());
        Class<?>[] soakEventClasses;
        try {
            soakEventClasses = EventClassMapping.soakEventClass(eventWrapper.event());
        } catch (RuntimeException e) {
            String className = eventWrapper.event().getName();
            if (!(
                    className.startsWith("org.bukkit") ||
                            className.startsWith("com.destroystokyo.paper") ||
                            className.startsWith("io.papermc") ||
                            className.startsWith("org.spigotmc"))) {
                return;
            }
            SoakPlugin.plugin()
                    .logger()
                    .error("Could not register event for " + eventWrapper.plugin().getName() + ": " + e.getMessage());
            return;
        }
        for (Class<?> soakEventClass : soakEventClasses) {
            try {
                var soakEvent = soakEventClass.getDeclaredConstructor(EventSingleListenerWrapper.class)
                        .newInstance(eventWrapper);
                Sponge.eventManager().registerListeners(pluginContainer, soakEvent);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "disablePlugin", Plugin.class);
    }

    @Override
    public @Nullable Permission getPermission(@NotNull String name) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "getPermission", String.class);
    }

    @Override
    public void addPermission(@NotNull Permission perm) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "addPermission", Permission.class);
    }

    @Override
    public void removePermission(@NotNull Permission perm) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "removePermission", Permission.class);
    }

    @Override
    public void removePermission(@NotNull String name) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "removePermission", String.class);
    }

    public void registerLoader(PluginLoader loader) {
        this.loaders.add(loader);
    }

    @Override
    public @NotNull Set<Permission> getDefaultPermissions(boolean op) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "getDefaultPermissions", boolean.class);
    }

    private Optional<PermissionService> permissionService() {
        return Sponge.serviceProvider()
                .provide(PermissionService.class);
    }

    @Override
    public void recalculatePermissionDefaults(@NotNull Permission perm) {
        var opService = permissionService();
        if (opService.isEmpty()) {
            SoakPlugin.plugin()
                    .logger()
                    .warn("A Bukkit plugin attempted to recalculate permissions without a permissions plugin loaded");
            return;
        }
        var service = opService.get();
        var opDescription = service.description(perm.getName());
        if (opDescription.isEmpty()) {
            return;
        }
        SoakPlugin.plugin()
                .logger()
                .error("A plugin attempted to change the permissions defaults to " + opDescription.get()
                        .id() + " however this is not supported by Sponge. Ignoring request");
    }

    @Override
    public void subscribeToPermission(@NotNull String permission, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class,
                "subscribeFromPermission",
                String.class,
                Permissible.class);
    }

    @Override
    public void unsubscribeFromPermission(@NotNull String permission, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class,
                "unsubscribeFromPermission",
                String.class,
                Permissible.class);
    }

    @Override
    public @NotNull Set<Permissible> getPermissionSubscriptions(@NotNull String permission) {
        //TODO other
        var ret = new HashSet<Permissible>();
        if (Sponge.isServerAvailable()) {
            var players = Bukkit.getServer()
                    .getOnlinePlayers()
                    .stream()
                    .filter(player -> player.hasPermission(permission))
                    .toList();
            ret.addAll(players);
        }
        return ret;
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class,
                "subscribeToDefaultPerms",
                boolean.class,
                Permissible.class);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class,
                "unsubscribeFromDefaultPerms",
                boolean.class,
                Permissible.class);
    }

    @Override
    public @NotNull Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class,
                "getDefaultPermSubscriptions",
                boolean.class);
    }

    @Override
    public @NotNull Set<Permission> getPermissions() {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "getPermissions", Set.class);
    }

    @Override
    public void addPermissions(@NotNull List<Permission> list) {
        throw NotImplementedException.createByLazy(PluginManager.class, "addPermissions");
    }

    @Override
    public void clearPermissions() {
        throw NotImplementedException.createByLazy(PluginManager.class, "clearPermissions");
    }

    @Override
    public boolean useTimings() {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "useTimings");
    }

    @Override
    public boolean isTransitiveDependency(PluginMeta pluginMeta, PluginMeta pluginMeta1) {
        throw NotImplementedException.createByLazy(PluginManager.class, "isTransitiveDependency", PluginMeta.class, PluginMeta.class);
    }

    @Override
    public void overridePermissionManager(@NotNull Plugin plugin, @Nullable PermissionManager permissionManager) {
        throw NotImplementedException.createByLazy(PluginManager.class, "isTransitiveDependency", Plugin.class, PermissionManager.class);
    }
}
