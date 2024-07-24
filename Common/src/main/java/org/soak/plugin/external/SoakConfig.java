package org.soak.plugin.external;

import net.kyori.adventure.text.Component;

import java.io.File;

public interface SoakConfig {

    File getConfigPath();
    Component getNoPermissionMessage();
}
