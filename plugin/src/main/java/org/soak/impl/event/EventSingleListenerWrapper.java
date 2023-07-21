package org.soak.impl.event;

import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EventSingleListenerWrapper<T extends Event> {

    private final Class<T> event;
    private final Listener listener;
    private final EventPriority priority;
    private final Plugin plugin;
    private final boolean ignoreCancelled;

    public EventSingleListenerWrapper(Listener listener,
                                      Plugin plugin,
                                      Class<T> eventType,
                                      EventPriority priority,
                                      boolean ignoreCancelled) {
        this.event = eventType;
        this.plugin = plugin;
        this.listener = listener;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public static Collection<EventSingleListenerWrapper<?>> findEventHandlers(Plugin plugin, Listener listener) {
        return Arrays.stream(listener.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventHandler.class))
                .filter(method -> method.getParameterCount() == 1)
                .map(method -> {
                    EventHandler handler = method.getAnnotation(EventHandler.class);
                    boolean ignoringCancelled = handler.ignoreCancelled();
                    EventPriority priority = handler.priority();
                    return new EventSingleListenerWrapper<>(listener, plugin, (Class<? extends Event>) method.getParameterTypes()[0], priority, ignoringCancelled);
                })
                .collect(Collectors.toSet());
    }

    public void invoke(T event, EventPriority priority) {
        if (!this.priority.equals(priority)) {
            return;
        }
        if (event instanceof Cancellable cancellable && cancellable.isCancelled() && !this.ignoreCancelled) {
            return;
        }
        List<Method> methods = Arrays.
                stream(this.listener.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventHandler.class))
                .filter(method -> method.getParameterTypes().length == 1)
                .filter(method -> method.getParameterTypes()[0].getName().equals(event.getClass().getName()))
                .toList();
    }

    public Class<T> event() {
        return event;
    }

    public Listener listener() {
        return listener;
    }

    public EventPriority priority() {
        return priority;
    }

    public Plugin plugin() {
        return plugin;
    }

    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }
}
