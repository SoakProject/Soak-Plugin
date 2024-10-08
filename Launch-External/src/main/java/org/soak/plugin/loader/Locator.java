package org.soak.plugin.loader;

import org.soak.config.SoakConfiguration;
import org.soak.plugin.SoakPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class Locator {

    public static Collection<File> files() {
        SoakConfiguration config = SoakPlugin.plugin().config();
        File pluginFolder = config.pluginFolder();
        SoakPlugin.plugin().logger().info("Looking for bukkit plugins in '" + pluginFolder.getAbsolutePath() + "'");
        File[] files = pluginFolder.listFiles((file, s) -> s.endsWith(".jar"));
        if (files == null || files.length == 0) {
            pluginFolder.mkdirs();
            SoakPlugin.plugin().logger().warn("Could not find any bukkit plugins");
            return Collections.emptySet();
        }
        return Arrays.stream(files).filter(file -> {
            try {
                JarFile jarFile = new JarFile(file);
                return null != jarFile.getEntry("plugin.yml");
            } catch (IOException e) {
                return false;
            }
        }).collect(Collectors.toSet());
    }
}
