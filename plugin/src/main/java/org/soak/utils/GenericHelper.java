package org.soak.utils;

import org.soak.plugin.SoakPlugin;
import org.soak.plugin.loader.common.SoakPluginContainer;

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
                                    var context = SoakPlugin.server().getPluginManager().getContext(soakPluginContainer.plugin());
                                    var classLoader = context.loader();
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
