package org.soak.plugin.loader.papar.meta;

import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SoakPluginMeta implements PluginMeta {

    private final @NotNull String name;
    private final @NotNull String main;
    private final @NotNull PluginLoadOrder loadOrder;
    private final @NotNull String version;
    private final @Nullable String prefix;
    private final @Nullable String description;
    private final @Nullable String apiVersion;
    private final @Nullable String website;
    private final @NotNull Map<String, DependType> pluginDepends = new ConcurrentHashMap<>();
    private final @NotNull List<Permission> permissions = new ArrayList<>();

    public SoakPluginMeta(@NotNull SoakPluginMetaBuilder builder) {
        this.name = Objects.requireNonNull(builder.getName(), "'setName' must be used");
        this.main = Objects.requireNonNull(builder.getMain(), "'setMain' must be used");
        this.loadOrder = Objects.requireNonNull(builder.getLoadOrder(), "'setLoadOrder' cannot be null");
        this.version = Objects.requireNonNull(builder.getVersion(), "'setVersion' must be used");
        this.prefix = builder.getPrefix();
        this.description = builder.getDescription();
        this.apiVersion = builder.getApiVersion();
        this.website = builder.getWebsite();
        pluginDepends.putAll(builder.getPluginDepends());
        permissions.addAll(builder.getPermissions());
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull String getMainClass() {
        return this.main;
    }

    @Override
    public @NotNull PluginLoadOrder getLoadOrder() {
        return this.loadOrder;
    }

    @Override
    public @NotNull String getVersion() {
        return this.version;
    }

    @Override
    public @Nullable String getLoggerPrefix() {
        return this.prefix;
    }

    private List<String> getPlugins(DependType type) {
        return this.pluginDepends.entrySet().stream().filter(entry -> entry.getValue().equals(type)).map(Map.Entry::getKey).toList();
    }

    @Override
    public @NotNull List<String> getPluginDependencies() {
        return getPlugins(DependType.LOAD_AFTER);
    }

    @Override
    public @NotNull List<String> getPluginSoftDependencies() {
        return getPlugins(DependType.SOFT_LOAD_AFTER);
    }

    @Override
    public @NotNull List<String> getLoadBeforePlugins() {
        return getPlugins(DependType.SOFT_LOAD_BEFORE);
    }

    @Override
    public @NotNull List<String> getProvidedPlugins() {
        return getPlugins(DependType.INCLUDED);
    }

    @Override
    public @NotNull List<String> getAuthors() {
        return null;
    }

    @Override
    public @NotNull List<String> getContributors() {
        return null;
    }

    @Override
    public @Nullable String getDescription() {
        return this.description;
    }

    @Override
    public @Nullable String getWebsite() {
        return this.website;
    }

    @Override
    public @NotNull List<Permission> getPermissions() {
        return this.permissions;
    }

    @Override
    public @NotNull PermissionDefault getPermissionDefault() {
        return PermissionDefault.FALSE;
    }

    @Override
    public @Nullable String getAPIVersion() {
        return this.apiVersion;
    }
}
