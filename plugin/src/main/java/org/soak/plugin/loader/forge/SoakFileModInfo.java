package org.soak.plugin.loader.forge;

import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SoakFileModInfo implements IModFileInfo {

    public static final SoakFileModInfo MOD_INFO = new SoakFileModInfo();

    private final List<IModInfo> soakPluginsCache = new LinkedList<>();

    @Override
    public List<IModInfo> getMods() {
        return Collections.unmodifiableList(this.soakPluginsCache);
    }

    public void addMod(SoakModInfo info) {
        this.soakPluginsCache.add(info);
    }

    @Override
    public String getModLoader() {
        return "soak";
    }

    @Override
    public VersionRange getModLoaderVersion() {
        return VersionRange.createFromVersion("0+");
    }

    @Override
    public boolean showAsResourcePack() {
        return false;
    }

    @Override
    public Map<String, Object> getFileProperties() {
        return Collections.emptyMap();
    }

    @Override
    public String getLicense() {
        return "MIT";
    }
}
