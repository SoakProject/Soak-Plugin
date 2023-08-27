package org.soak.plugin.loader.vanilla;

import org.soak.plugin.loader.common.SoakPluginContainer;
import org.soak.plugin.loader.common.SoakPluginInjector;
import org.soak.plugin.utils.ReflectionHelper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginManager;

import java.lang.reflect.InvocationTargetException;

public class VanillaPluginInjector implements SoakPluginInjector {
    public static void injectPluginToPlatform(SoakPluginContainer container) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        PluginManager manager = Sponge.pluginManager();
        ReflectionHelper.runMethod(manager, "addPlugin", container);
    }
}
