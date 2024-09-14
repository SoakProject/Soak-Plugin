package org.soak.map.event;

import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.soak.plugin.SoakManager;
import org.soak.utils.BasicEntry;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        try {
            var methods = listener.getClass().getDeclaredMethods();
            return Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(EventHandler.class))
                    .filter(method -> method.getParameterCount() == 1)
                    .map(method -> {
                        EventHandler handler = method.getAnnotation(EventHandler.class);
                        boolean ignoringCancelled = handler.ignoreCancelled();
                        EventPriority priority = handler.priority();
                        return new EventSingleListenerWrapper<>(listener,
                                plugin,
                                (Class<? extends Event>) method.getParameterTypes()[0],
                                priority,
                                ignoringCancelled);
                    })
                    .collect(Collectors.toSet());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to register events for plugin '" + plugin.getName() + "' of class '" + listener.getClass().getName() + "'", e);
        }
    }

    public void invoke(T event, EventPriority priority) {
        if (!this.priority.equals(priority)) {
            return;
        }
        if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && !this.ignoreCancelled) {
            return;
        }

        var eventExecutors = Arrays
                .stream(this.listener.getClass().getDeclaredFields())
                .filter(field -> EventExecutor.class.isAssignableFrom(field.getType()))
                .filter(field -> Modifier.isPublic(field.getModifiers())).toList();
        for (var field : eventExecutors) {
            if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && !this.ignoreCancelled) {
                break;
            }
            try {
                field.setAccessible(true);
                var executor = (EventExecutor) field.get(this.listener);
                executor.execute(this.listener, event);
            } catch (Throwable e) {
                SoakManager.getManager().displayError(e, this.plugin, new BasicEntry<>("event", event.getEventName()));
            }
            field.setAccessible(false);
        }

        List<Method> methods = Arrays.
                stream(this.listener.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventHandler.class))
                .filter(method -> method.getParameterTypes().length == 1)
                .filter(method -> method.getParameterTypes()[0].getName().equals(event.getClass().getName()))
                .collect(Collectors.toList());
        for (Method method : methods) {
            if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && !this.ignoreCancelled) {
                break;
            }
            try {
                method.setAccessible(true);
                method.invoke(this.listener, event);
            } catch (Throwable e) {
                SoakManager.getManager().displayError(e, this.plugin, new BasicEntry<>("event", event.getEventName()));
            }
            method.setAccessible(false);
        }
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
