package org.soak.plugin.loader.sponge;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.plugin.utils.StringHelper;
import org.spongepowered.plugin.metadata.Container;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.model.PluginBranding;
import org.spongepowered.plugin.metadata.model.PluginContributor;
import org.spongepowered.plugin.metadata.model.PluginDependency;
import org.spongepowered.plugin.metadata.model.PluginLinks;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SoakPluginMetadata implements PluginMetadata {

    private Plugin plugin;
    private final ArtifactVersion artifactVersion;

    private SoakPluginMetadata(Plugin plugin,
            ArtifactVersion artifactVersion
    ) {
        this.plugin = plugin;
        this.artifactVersion = artifactVersion;
    }

    public static SoakPluginMetadata fromPlugin(Plugin plugin) {
        ArtifactVersion version = new SoakPluginVersion(plugin.getDescription().getVersion());
        return new SoakPluginMetadata(plugin, version);
    }

    @Override
    public Container container() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public String id() {
        return StringHelper.toId(this.plugin.getName());
    }

    @Override
    public String entrypoint() {
        return this.plugin.getDescription().getMain();
    }

    @Override
    public Optional<String> name() {
        return Optional.of(this.plugin.getName());
    }

    @Override
    public Optional<String> description() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public ArtifactVersion version() {
        return this.artifactVersion;
    }

    @Override
    public PluginBranding branding() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public PluginLinks links() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public List<PluginContributor> contributors() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public Optional<PluginDependency> dependency(String id) {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public Set<PluginDependency> dependencies() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public <T> Optional<T> property(String key) {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }

    @Override
    public Map<String, Object> properties() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "container");
    }
}
