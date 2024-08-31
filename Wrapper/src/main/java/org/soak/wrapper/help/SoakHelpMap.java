package org.soak.wrapper.help;

import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.exception.NotImplementedException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class SoakHelpMap implements HelpMap {

    private final Collection<HelpTopic> topics = new LinkedTransferQueue<>();

    @Override
    public @Nullable HelpTopic getHelpTopic(@NotNull String s) {
        return this.topics.stream().filter(topic -> topic.getName().equalsIgnoreCase(s)).findAny().orElse(null);
    }

    @Override
    public @NotNull Collection<HelpTopic> getHelpTopics() {
        return this.topics;
    }

    @Override
    public void addTopic(@NotNull HelpTopic helpTopic) {
        this.topics.add(helpTopic);
    }

    @Override
    public void clear() {
        this.topics.clear();
    }

    @Override
    public void registerHelpTopicFactory(@NotNull Class<?> aClass, @NotNull HelpTopicFactory<?> helpTopicFactory) {
        throw NotImplementedException.createByLazy(HelpMap.class, "registerHelpTopicFactory", Class.class, HelpTopicFactory.class);
    }

    @Override
    public @NotNull List<String> getIgnoredPlugins() {
        throw NotImplementedException.createByLazy(HelpMap.class, "getIgnoredPlugins");
    }
}
