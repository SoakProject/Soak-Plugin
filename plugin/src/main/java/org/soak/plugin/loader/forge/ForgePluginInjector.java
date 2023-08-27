package org.soak.plugin.loader.forge;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.soak.plugin.loader.common.SoakPluginContainer;
import org.soak.plugin.loader.common.SoakPluginInjector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ForgePluginInjector implements SoakPluginInjector {

    public static void injectPluginToPlatform(SoakPluginContainer container) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException, InstantiationException {
        var modContainer = new SoakModContainer(container);
        var modFileInfo = SoakFileModInfo.MOD_INFO;
        var pluginFileScanData = new ModFileScanData();
        pluginFileScanData.addModFileInfo(modFileInfo);

        var modListAccessor = ModList.get();

        /*var modFileScanData = modListAccessor.getAllScanData();
        modFileScanData.add(pluginFileScanData);*/

        var modsField = modListAccessor.getClass().getDeclaredField("mods");
        modsField.setAccessible(true);
        var mods = (List<ModContainer>) modsField.get(modListAccessor);
        mods.add(modContainer);
        modsField.set(modListAccessor, mods);
    }
}
