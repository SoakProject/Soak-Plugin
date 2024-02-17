package org.soak.plugin.loader.papar.meta;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginLoadOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SoakPluginMetaBuilder {

    private String name;
    private String main;
    private PluginLoadOrder loadOrder = PluginLoadOrder.STARTUP;
    private String version;
    private String prefix;
    private String description;
    private String apiVersion;
    private String website;
    private Map<String, DependType> pluginDepends = new ConcurrentHashMap<>();
    private List<Permission> permissions = new ArrayList<>();

    public SoakPluginMeta build() {
        return new SoakPluginMeta(this);
    }

    public String getName() {
        return name;
    }

    public SoakPluginMetaBuilder setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public String getMain() {
        return main;
    }

    public SoakPluginMetaBuilder setMain(@NotNull String main) {
        this.main = main;
        return this;
    }

    public PluginLoadOrder getLoadOrder() {
        return loadOrder;
    }

    public SoakPluginMetaBuilder setLoadOrder(@NotNull PluginLoadOrder loadOrder) {
        this.loadOrder = loadOrder;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public SoakPluginMetaBuilder setVersion(@NotNull String version) {
        this.version = version;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public SoakPluginMetaBuilder setPrefix(@Nullable String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SoakPluginMetaBuilder setDescription(@Nullable String description) {
        this.description = description;
        return this;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public SoakPluginMetaBuilder setApiVersion(@Nullable String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public SoakPluginMetaBuilder setWebsite(@Nullable String website) {
        this.website = website;
        return this;
    }

    public Map<String, DependType> getPluginDepends() {
        return pluginDepends;
    }

    public SoakPluginMetaBuilder setPluginDepends(@NotNull Map<String, DependType> pluginDepends) {
        this.pluginDepends = pluginDepends;
        return this;
    }

    public SoakPluginMetaBuilder addPluginDepends(DependType type, Spliterator<String> plugins) {
        var map = StreamSupport.stream(plugins, true).collect(Collectors.toMap(v -> v, v -> type));
        this.pluginDepends.putAll(map);
        return this;
    }

    public SoakPluginMetaBuilder addPluginDepends(DependType type, Collection<String> plugins) {
        return this.addPluginDepends(type, plugins.spliterator());
    }

    @Deprecated
    private SoakPluginMetaBuilder addPluginDepends(DependType type) {
        //why are you using this .... your adding 0 plugins
        return this;
    }

    public SoakPluginMetaBuilder addPluginDepends(DependType type, String... plugins) {
        return this.addPluginDepends(type, Arrays.stream(plugins).spliterator());
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public SoakPluginMetaBuilder setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
        return this;
    }
}
