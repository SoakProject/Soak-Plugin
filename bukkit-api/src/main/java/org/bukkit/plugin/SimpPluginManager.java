package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.wrapper.exception.bukkit.NeverImplementException;

import java.io.File;
import java.util.Arrays;

public interface SimpPluginManager extends PluginManager {

    @Override
    default @Nullable Plugin getPlugin(@NotNull String name) {
        return Arrays.stream(this.getPlugins()).filter(pl -> pl.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    default void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin) {
        registerEvent(event, listener, priority, executor, plugin, false);
    }

    @Override
    @Deprecated
    default boolean isPluginEnabled(@NotNull String name) {
        Plugin plugin = this.getPlugin(name);
        return isPluginEnabled(plugin);
    }

    @Override
    @Deprecated
    default @NotNull Plugin[] loadPlugins(@NotNull File directory) {
        throw new NeverImplementException("loadPlugins");
    }

    @Override
    @Deprecated
    default void disablePlugins() {
        throw new NeverImplementException("disablePlugins");
    }

    @Override
    @Deprecated
    default void clearPlugins() {
        throw new NeverImplementException("clearPlugins");
    }

    @Override
    @Deprecated
    default void enablePlugin(@NotNull Plugin plugin) {
        throw new NeverImplementException("enablePlugin");
    }

    @Override
    @Deprecated
    default void disablePlugin(@NotNull Plugin plugin, boolean closeClassloader) {
        if (!closeClassloader) {
            this.disablePlugin(plugin);
        }
        throw new NeverImplementException("disablePlugin(Plugin plugin, boolean closeClassLoader)");
    }
}
