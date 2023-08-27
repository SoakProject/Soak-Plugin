package org.soak.plugin.loader.common;

import org.soak.plugin.loader.forge.ForgePluginInjector;
import org.soak.plugin.loader.vanilla.VanillaPluginInjector;

import java.lang.reflect.InvocationTargetException;

public interface SoakPluginInjector {

    static void injectPlugin(SoakPluginContainer container) {
        try {
            VanillaPluginInjector.injectPluginToPlatform(container);
        } catch (NoSuchMethodException e) {
            //load forge
            try {
                ForgePluginInjector.injectPluginToPlatform(container);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
