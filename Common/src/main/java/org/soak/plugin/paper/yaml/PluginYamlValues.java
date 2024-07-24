package org.soak.plugin.paper.yaml;

public interface PluginYamlValues {

    PluginYamlValue<String> NAME = new AbstractPluginYamlValue.StringPluginYamlValue("name");
    PluginYamlValue<String> VERSION = new AbstractPluginYamlValue.StringPluginYamlValue("version");
    PluginYamlValue<String> MAIN = new AbstractPluginYamlValue.StringPluginYamlValue("main");
    PluginYamlValue<String> DESCRIPTION = new AbstractPluginYamlValue.StringPluginYamlValue("description");
    PluginYamlValue<String> API_VERSION = new AbstractPluginYamlValue.StringPluginYamlValue("api-version");
    PluginYamlValue<String> BOOTSTRAP = new AbstractPluginYamlValue.StringPluginYamlValue("bootstrapper");
    PluginYamlValue<String> LOADER = new AbstractPluginYamlValue.StringPluginYamlValue("loader");


}
