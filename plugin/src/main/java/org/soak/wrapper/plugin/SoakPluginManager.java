package org.soak.wrapper.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.plugin.loader.sponge.SoakPluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Supplier;

public class SoakPluginManager implements SimpPluginManager {

    private final Supplier<PluginManager> spongePluginManager;
    private final Collection<PluginLoader> loaders = new LinkedTransferQueue<>();

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
        if (e instanceof InvalidPluginException ipe) {
            throw ipe;
        }
        if (e instanceof UnknownDependencyException ude) {
            throw ude;
        }
        throw (RuntimeException) e;
    }

    @Override
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        throw NotImplementedException.createByLazy(Class.class);
    }

    @Override
    public @NotNull Plugin[] getPlugins() {
        return this.spongeManager().plugins().stream().filter(container -> container instanceof SoakPluginContainer).map(container -> ((SoakPluginContainer) container).instance()).toArray(Plugin[]::new);
    }

    @Override
    public void callEvent(@NotNull Event event) throws IllegalStateException {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "callEvent", Event.class);
    }

    @Override
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "registerEvents", Listener.class, Plugin.class);
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "registerEvent", Class.class, Listener.class, EventPriority.class, EventExecutor.class, Plugin.class, boolean.class);
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

    @Override
    public void recalculatePermissionDefaults(@NotNull Permission perm) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "recalculatePermissionDefaults", Permission.class);
    }

    @Override
    public void subscribeToPermission(@NotNull String permission, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "subscribeFromPermission", String.class, Permissible.class);
    }

    @Override
    public void unsubscribeFromPermission(@NotNull String permission, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "unsubscribeFromPermission", String.class, Permissible.class);
    }

    @Override
    public @NotNull Set<Permissible> getPermissionSubscriptions(@NotNull String permission) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "getPermissionSubscriptions", String.class);
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "subscribeToDefaultPerms", boolean.class, Permissible.class);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, @NotNull Permissible permissible) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "unsubscribeFromDefaultPerms", boolean.class, Permissible.class);
    }

    @Override
    public @NotNull Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "getDefaultPermSubscriptions", boolean.class);
    }

    @Override
    public @NotNull Set<Permission> getPermissions() {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "getPermissions", Set.class);
    }

    @Override
    public boolean useTimings() {
        throw NotImplementedException.createByLazy(SoakPluginManager.class, "useTimings");
    }
}
