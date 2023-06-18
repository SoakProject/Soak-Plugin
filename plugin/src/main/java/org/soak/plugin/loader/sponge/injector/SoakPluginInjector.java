package org.soak.plugin.loader.sponge.injector;

import org.soak.plugin.loader.sponge.SoakPluginContainer;
import org.spongepowered.plugin.PluginContainer;

import java.lang.reflect.InvocationTargetException;

public interface SoakPluginInjector {

    static void injectPlugin(SoakPluginContainer container){
        try {
            VanillaPluginInjector.injectPluginToPlatform(container);
        }catch (NoSuchMethodException e){
            //load forge
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
