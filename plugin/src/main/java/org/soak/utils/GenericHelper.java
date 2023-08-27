package org.soak.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.loader.common.SoakPluginContainer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class GenericHelper {

    //sometimes Sponge requires a plugin for tracking ... while bukkit doesn't. This is a janky way of getting the plugin
    //ideally use other ways to grab the plugin
    public static SoakPluginContainer fromStackTrace() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .map(StackTraceElement::getClassName)
                .filter(className -> !className.startsWith("org.soak"))
                .filter(className -> !className.startsWith("org.bukkit"))
                .filter(className -> !className.startsWith("io.papermc"))
                .filter(className -> !className.startsWith("java.lang"))
                .map(className -> SoakPlugin.plugin().getPlugins().map(soakPluginContainer -> {
                                    var loader = soakPluginContainer.plugin().getPluginLoader();
                                    if (!(loader instanceof JavaPluginLoader)) {
                                        return null;
                                    }
                                    var jpl = (JavaPluginLoader) loader;
                                    //compile issue with this now .... its added in the local project
                                    Optional<PluginClassLoader> opClassLoader;
                                    try {
                                        opClassLoader = (Optional<PluginClassLoader>) jpl.getClass().getDeclaredMethod("getLoader", JavaPlugin.class).invoke((JavaPlugin) soakPluginContainer.plugin());
                                    } catch (IllegalAccessException | InvocationTargetException |
                                             NoSuchMethodException e) {
                                        throw new RuntimeException(e);
                                    }

                                    if (opClassLoader.isEmpty()) {
                                        return null;
                                    }
                                    var classLoader = opClassLoader.get();
                                    try {
                                        Class.forName(className, false, classLoader);
                                        return soakPluginContainer;
                                    } catch (ClassNotFoundException e) {
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull).findAny()
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseThrow(() -> new RuntimeException("could not find the plugin from the stacktrace"));
    }
}
