package org.bukkit.plugin.java;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.Warning.WarningState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.plugin.SoakPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jar
 */
public final class JavaPluginLoader implements PluginLoader {
    private static final boolean DISABLE_CLASS_PRIORITIZATION = Boolean.getBoolean("Paper.DisableClassPrioritization"); // Paper
    final Server server;
    private final Pattern[] fileFilters = new Pattern[]{Pattern.compile("\\.jar$")};
    private final Map<String, java.util.concurrent.locks.ReentrantReadWriteLock> classLoadLock = new java.util.HashMap<String, java.util.concurrent.locks.ReentrantReadWriteLock>(); // Paper
    private final Map<String, Integer> classLoadLockCount = new java.util.HashMap<String, Integer>(); // Paper
    private final List<PluginClassLoader> loaders = new CopyOnWriteArrayList<PluginClassLoader>();
    private final LibraryLoader libraryLoader;

    /**
     * This class was not meant to be constructed explicitly
     *
     * @param instance the server instance
     */
    @Deprecated
    public JavaPluginLoader(@NotNull Server instance) {
        Validate.notNull(instance, "Server cannot be null");
        server = instance;

        LibraryLoader libraryLoader = null;
        try {
            libraryLoader = new LibraryLoader(server.getLogger());
        } catch (NoClassDefFoundError ex) {
            // Provided depends were not added back
            server.getLogger().warning("Could not initialize LibraryLoader (missing dependencies?)");
        }
        this.libraryLoader = libraryLoader;
    }

    @Override
    @NotNull
    public Plugin loadPlugin(@NotNull final File file) throws InvalidPluginException {
        Validate.notNull(file, "File cannot be null");

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        final PluginDescriptionFile description;
        try {
            description = getPluginDescription(file);
        } catch (InvalidDescriptionException ex) {
            throw new InvalidPluginException(ex);
        }

        //final File parentFile = SoakPlugin.plugin().config().pluginFolder();
        //too many plugins assume the plugin directory is '/plugins'
        final File parentFile = new File("plugins");
        final File dataFolder = new File(parentFile, description.getName());

        @SuppressWarnings("deprecation") final File oldDataFolder = new File(parentFile, description.getRawName());

        // Found old data folder
        if (dataFolder.equals(oldDataFolder)) {
            // They are equal -- nothing needs to be done!
        } else if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
            server.getLogger().warning(String.format(
                    "While loading %s (%s) found old-data folder: `%s' next to the new one `%s'",
                    description.getFullName(),
                    file,
                    oldDataFolder,
                    dataFolder
            ));
        } else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
            if (!oldDataFolder.renameTo(dataFolder)) {
                throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "' to: `" + dataFolder + "'");
            }
            server.getLogger().log(Level.INFO, String.format(
                    "While loading %s (%s) renamed data folder: `%s' to `%s'",
                    description.getFullName(),
                    file,
                    oldDataFolder,
                    dataFolder
            ));
        }

        if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new InvalidPluginException(String.format(
                    "Projected datafolder: `%s' for %s (%s) exists and is not a directory",
                    dataFolder,
                    description.getFullName(),
                    file
            ));
        }

        Set<String> missingHardDependencies = new HashSet<>(description.getDepend()
                .size()); // Paper - list all missing hard depends
        for (final String pluginName : description.getDepend()) {
            Plugin current = server.getPluginManager().getPlugin(pluginName);

            if (current == null) {
                missingHardDependencies.add(pluginName); // Paper - list all missing hard depends
            }
        }
        // Paper start - list all missing hard depends
        if (!missingHardDependencies.isEmpty()) {
            throw new UnknownDependencyException(missingHardDependencies, description.getFullName());
        }
        // Paper end

        server.getUnsafe().checkSupported(description);

        final PluginClassLoader loader;
        try {
            loader = new PluginClassLoader(this,
                    getClass().getClassLoader(),
                    description,
                    dataFolder,
                    file,
                    (libraryLoader != null) ? libraryLoader.createLoader(description) : null);
        } catch (InvalidPluginException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }

        loaders.add(loader);

        return loader.plugin;
    }

    @Override
    @NotNull
    public PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException {
        Validate.notNull(file, "File cannot be null");

        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(entry);

            return new PluginDescriptionFile(stream);

        } catch (IOException ex) {
            throw new InvalidDescriptionException(ex);
        } catch (YAMLException ex) {
            throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    @NotNull
    public Pattern[] getPluginFileFilters() {
        return fileFilters.clone();
    }

    public Optional<PluginClassLoader> getLoader(JavaPlugin plugin) {
        return this.loaders.stream().filter(loader -> loader.plugin.equals(plugin)).findAny();
    }

    @Nullable
    Class<?> getClassByName(final String name, boolean resolve, PluginDescriptionFile description) {
        // Paper start - prioritize self
        return getClassByName(name, resolve, description, null);
    }

    Class<?> getClassByName(final String name, boolean resolve, PluginDescriptionFile description, PluginClassLoader requester) {
        // Paper end
        // Paper start - make MT safe
        java.util.concurrent.locks.ReentrantReadWriteLock lock;
        synchronized (classLoadLock) {
            lock = classLoadLock.computeIfAbsent(name, (x) -> new java.util.concurrent.locks.ReentrantReadWriteLock());
            classLoadLockCount.compute(name, (x, prev) -> prev != null ? prev + 1 : 1);
        }
        lock.writeLock().lock();
        try {
            // Paper start - prioritize self
            if (!DISABLE_CLASS_PRIORITIZATION && requester != null) {
                try {
                    return requester.loadClass0(name, false, false, false);
                } catch (ClassNotFoundException cnfe) {
                }
            }
            // Paper end
            // Paper end
            for (PluginClassLoader loader : loaders) {
                try {
                    return loader.loadClass0(name, resolve, false, false);
                } catch (ClassNotFoundException cnfe) {
                }
            }
            // Paper start - make MT safe
        } finally {
            synchronized (classLoadLock) {
                lock.writeLock().unlock();
                if (classLoadLockCount.get(name) == 1) {
                    classLoadLock.remove(name);
                    classLoadLockCount.remove(name);
                } else {
                    classLoadLockCount.compute(name, (x, prev) -> prev - 1);
                }
            }
        }
        // Paper end
        return null;
    }

    void setClass(@NotNull final String name, @NotNull final Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.registerClass(serializable);
        }
    }

    private void removeClass(@NotNull Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.unregisterClass(serializable);
        }
    }

    @Override
    @NotNull
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener, @NotNull final Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");

        boolean useTimings = server.getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<Method>(publicMethods.length + privateMethods.length, 1.0f);
            for (Method method : publicMethods) {
                methods.add(method);
            }
            for (Method method : privateMethods) {
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            plugin.getLogger()
                    .severe("Plugin " + plugin.getDescription()
                            .getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger()
                        .severe(plugin.getDescription()
                                .getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.get(eventClass);
            if (eventSet == null) {
                eventSet = new HashSet<RegisteredListener>();
                ret.put(eventClass, eventSet);
            }

            for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    Warning warning = clazz.getAnnotation(Warning.class);
                    WarningState warningState = server.getWarningState();
                    if (!warningState.printFor(warning)) {
                        break;
                    }
                    plugin.getLogger().log(
                            Level.WARNING,
                            String.format(
                                    "\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated. \"%s\"; please notify the authors %s.",
                                    plugin.getDescription().getFullName(),
                                    clazz.getName(),
                                    method.toGenericString(),
                                    (warning != null && warning.reason()
                                            .length() != 0) ? warning.reason() : "Server performance will be affected",
                                    Arrays.toString(plugin.getDescription().getAuthors().toArray())),
                            warningState == WarningState.ON ? new AuthorNagException(null) : null);
                    break;
                }
            }

            EventExecutor executor = new co.aikar.timings.TimedEventExecutor(EventExecutor.create(method, eventClass),
                    plugin,
                    method,
                    eventClass); // Paper // Paper (Yes.) - Use factory method `EventExecutor.create()`
            if (false) { // Spigot - RL handles useTimings check now
                eventSet.add(new TimedRegisteredListener(listener,
                        executor,
                        eh.priority(),
                        plugin,
                        eh.ignoreCancelled()));
            } else {
                eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            }
        }
        return ret;
    }

    @Override
    public void enablePlugin(@NotNull final Plugin plugin) {
        Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (!plugin.isEnabled()) {
            // Paper start - Add an asterisk to legacy plugins (so admins are aware)
            String enableMsg = "Enabling " + plugin.getDescription().getFullName();
            if (org.bukkit.UnsafeValues.isLegacyPlugin(plugin)) {
                enableMsg += "*";
            }

            plugin.getLogger().info(enableMsg);
            // Paper end

            JavaPlugin jPlugin = (JavaPlugin) plugin;

            PluginClassLoader pluginLoader = (PluginClassLoader) jPlugin.getClassLoader();

            if (!loaders.contains(pluginLoader)) {
                loaders.add(pluginLoader);
                server.getLogger()
                        .log(Level.WARNING,
                                "Enabled plugin with unregistered PluginClassLoader " + plugin.getDescription()
                                        .getFullName());
            }

            try {
                jPlugin.setEnabled(true);
            } catch (Throwable ex) {
                /*server.getLogger().log(Level.SEVERE, "Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
                // Paper start - Disable plugins that fail to load
                server.getPluginManager().disablePlugin(jPlugin, true);*/ // Paper - close Classloader on disable - She's dead jim
                SoakPlugin.plugin().displayError(ex, jPlugin);
                return;
                // Paper end
            }

            // Perhaps abort here, rather than continue going, but as it stands,
            // an abort is not possible the way it's currently written
            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        // Paper start - close Classloader on disable
        disablePlugin(plugin, false); // Retain old behavior unless requested
    }

    public void disablePlugin(@NotNull Plugin plugin, boolean closeClassloader) {
        // Paper end - close Class Loader on disable
        Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");

        if (plugin.isEnabled()) {
            String message = String.format("Disabling %s", plugin.getDescription().getFullName());
            plugin.getLogger().info(message);

            server.getPluginManager().callEvent(new PluginDisableEvent(plugin));

            JavaPlugin jPlugin = (JavaPlugin) plugin;
            ClassLoader cloader = jPlugin.getClassLoader();

            try {
                jPlugin.setEnabled(false);
            } catch (Throwable ex) {
                server.getLogger()
                        .log(Level.SEVERE,
                                "Error occurred while disabling " + plugin.getDescription()
                                        .getFullName() + " (Is it up to date?)",
                                ex);
            }

            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader) cloader;
                loaders.remove(loader);

                Collection<Class<?>> classes = loader.getClasses();

                for (Class<?> clazz : classes) {
                    removeClass(clazz);
                }

                try {
                    loader.close();
                } catch (IOException ex) {
                    //
                }
                // Paper start - close Class Loader on disable
                try {
                    if (closeClassloader) {
                        loader.close();
                    }
                } catch (IOException e) {
                    server.getLogger()
                            .log(Level.WARNING,
                                    "Error closing the Plugin Class Loader for " + plugin.getDescription()
                                            .getFullName());
                    e.printStackTrace();
                }
                // Paper end
            }
        }
    }
}
