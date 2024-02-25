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

    static void removePluginFromPlatform(String id) {
        try {
            VanillaPluginInjector.removePluginFromPlatform(id);
        } catch (NoSuchFieldException e) {
            try {
                ForgePluginInjector.removePluginFromPlatform(id);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static void removePluginFromPlatform(SoakPluginContainer container) {
        try {
            VanillaPluginInjector.removePluginFromPlatform(container);
        } catch (NoSuchFieldException e) {
            try {
                ForgePluginInjector.removePluginFromPlatform(container);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
