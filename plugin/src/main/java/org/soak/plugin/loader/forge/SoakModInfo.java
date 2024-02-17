package org.soak.plugin.loader.forge;

import net.minecraftforge.forgespi.language.IConfigurable;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.ForgeFeature;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.soak.plugin.loader.common.SoakPluginContainer;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SoakModInfo implements IModInfo {

    private final SoakPluginContainer metadata;

    public SoakModInfo(SoakPluginContainer metadata) {
        this.metadata = metadata;
    }

    @Override
    public IModFileInfo getOwningFile() {
        return SoakFileModInfo.MOD_INFO;
    }

    @Override
    public String getModId() {
        return this.metadata.metadata().id();
    }

    @Override
    public String getDisplayName() {
        return this.metadata.metadata().name().orElse(this.metadata.metadata().id());
    }

    @Override
    public String getDescription() {
        return this.metadata.metadata().description().orElse("");
    }

    @Override
    public ArtifactVersion getVersion() {
        return this.metadata.metadata().version();
    }

    @Override
    public List<? extends ModVersion> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends ForgeFeature.Bound> getForgeFeatures() {
        return Collections.emptyList();
    }

    @Override
    public String getNamespace() {
        return this.metadata.plugin().getClass().getPackageName();
    }

    @Override
    public Map<String, Object> getModProperties() {
        return Collections.emptyMap();
    }

    @Override
    public Optional<URL> getUpdateURL() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<URL> getModURL() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getLogoFile() {
        return Optional.empty();
    }

    @Override
    public boolean getLogoBlur() {
        return false;
    }

    @Override
    public IConfigurable getConfig() {
        throw new RuntimeException("Not implemented");
    }
}
