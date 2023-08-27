package org.soak.plugin.loader.forge;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;
import org.soak.plugin.loader.common.SoakPluginContainer;
import org.soak.plugin.loader.common.SoakPluginWrapper;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.net.URI;
import java.util.Optional;

public class SoakModContainer extends ModContainer implements SoakPluginContainer {

    private final SoakPluginContainer container;

    SoakModContainer(SoakPluginContainer container) {
        super(new SoakModInfo(container));
        this.contextExtension = () -> null; //gets the language config
        this.modLoadingStage = ModLoadingStage.COMMON_SETUP;
        this.container = container;
    }

    @Override
    public Plugin plugin() {
        return this.container.plugin();
    }

    @Override
    public PluginMetadata metadata() {
        return this.container.metadata();
    }

    @Override
    public Logger logger() {
        return this.container.logger();
    }

    @Override
    public SoakPluginWrapper instance() {
        return this.container.instance();
    }

    @Override
    public Optional<URI> locateResource(URI relative) {
        return this.container.locateResource(relative);
    }

    @Override
    public boolean matches(Object o) {
        System.out.println("Matches: " + o);
        return false;
    }

    @Override
    public Object getMod() {
        return this.instance();
    }
}
