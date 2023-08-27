package org.soak.plugin.loader.common;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.bukkit.plugin.Plugin;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.plugin.utils.StringHelper;
import org.spongepowered.plugin.metadata.Container;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.model.PluginBranding;
import org.spongepowered.plugin.metadata.model.PluginContributor;
import org.spongepowered.plugin.metadata.model.PluginDependency;
import org.spongepowered.plugin.metadata.model.PluginLinks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class SoakPluginMetadata implements PluginMetadata {

    private final ArtifactVersion artifactVersion;
    private Plugin plugin;

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
        String description = this.plugin.getDescription().getDescription();
        return Optional.ofNullable(description);
    }

    @Override
    public ArtifactVersion version() {
        return this.artifactVersion;
    }

    @Override
    public PluginBranding branding() {
        return new PluginBranding() {

            @Override
            public Optional<String> icon() {
                return Optional.empty();
            }

            @Override
            public Optional<String> logo() {
                return Optional.empty();
            }
        };
    }

    @Override
    public PluginLinks links() {
        return new PluginLinks() {
            @Override
            public Optional<URL> homepage() {
                return Optional.ofNullable(plugin.getDescription().getWebsite()).flatMap(web -> {
                    try {
                        return Optional.of(new URL(web));
                    } catch (MalformedURLException e) {
                        return Optional.empty();
                    }
                });
            }

            @Override
            public Optional<URL> source() {
                return Optional.empty();
            }

            @Override
            public Optional<URL> issues() {
                return Optional.empty();
            }
        };
    }

    @Override
    public List<PluginContributor> contributors() {
        return this.plugin.getDescription().getContributors().stream().map(name -> {
            return new PluginContributor() {
                @Override
                public String name() {
                    return name;
                }

                @Override
                public Optional<String> description() {
                    return Optional.empty();
                }
            };
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<PluginDependency> dependency(String id) {
        return dependencies().stream().filter(pd -> pd.id().equals(id)).findFirst();
    }

    @Override
    public Set<PluginDependency> dependencies() {
        Set<PluginDependency> hardDepends = this.plugin.getDescription().getDepend().stream().map(name -> new BukkitPluginDependency(name, true, false)).collect(Collectors.toSet());
        Set<PluginDependency> softDepends = this.plugin.getDescription().getSoftDepend().stream().map(name -> new BukkitPluginDependency(name, false, false)).collect(Collectors.toSet());
        Set<PluginDependency> beforeDepends = this.plugin.getDescription().getLoadBefore().stream().map(name -> new BukkitPluginDependency(name, false, true)).collect(Collectors.toSet());

        Set<PluginDependency> ret = new HashSet<>();
        ret.addAll(hardDepends);
        ret.addAll(softDepends);
        ret.addAll(beforeDepends);

        ret.add(new BukkitPluginDependency(SoakPlugin.plugin().container().metadata().id(), true, false));
        return ret;
    }

    @Override
    public <T> Optional<T> property(String key) {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "property", String.class);
    }

    @Override
    public Map<String, Object> properties() {
        throw NotImplementedException.createByLazy(PluginMetadata.class, "properties");
    }

    private class BukkitPluginDependency implements PluginDependency {

        private final String name;
        private final boolean hardDependency;
        private final boolean loadBefore;

        public BukkitPluginDependency(String name, boolean hardDependency, boolean loadBefore) {
            this.hardDependency = hardDependency;
            this.loadBefore = loadBefore;
            this.name = name;
        }

        @Override
        public String id() {
            return StringHelper.toId(name);
        }

        @Override
        public VersionRange version() {
            return VersionRange.createFromVersion("0.0.0");
        }

        @Override
        public LoadOrder loadOrder() {
            return this.loadBefore ? LoadOrder.BEFORE : LoadOrder.AFTER;
        }

        @Override
        public boolean optional() {
            return !this.hardDependency;
        }

    }
}
