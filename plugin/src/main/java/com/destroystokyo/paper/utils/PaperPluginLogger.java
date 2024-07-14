package com.destroystokyo.paper.utils;

import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.soak.config.SoakConfiguration;
import org.soak.plugin.SoakPlugin;
import org.soak.utils.log.CustomLoggerFormat;

import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.*;

/**
 * Prevents plugins (e.g. Essentials) from changing the parent of the plugin logger.
 */
public class PaperPluginLogger extends Logger {

    Supplier<SoakConfiguration> config = () -> SoakPlugin.plugin().config();

    private PaperPluginLogger(@NotNull PluginDescriptionFile description) {
        super(description.getPrefix() != null ? description.getPrefix() : description.getName(), null);
        this.setUseParentHandlers(false);
        var consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomLoggerFormat());
        this.addHandler(consoleHandler);
    }

    @NotNull
    public static Logger getLogger(@NotNull PluginDescriptionFile description) {
        Logger logger = new PaperPluginLogger(description);
        if (!LogManager.getLogManager().addLogger(logger)) {
            // Disable this if it's going to happen across reloads anyways...
            //logger.log(Level.WARNING, "Could not insert plugin logger - one was already found: {}", LogManager.getLogManager().getLogger(this.getName()));
            logger = LogManager.getLogManager().getLogger(description.getPrefix() != null ? description.getPrefix() : description.getName());
        }

        return logger;
    }

    @Override
    public void setParent(@NotNull Logger parent) {
        if (getParent() != null) {
            warning("Ignoring attempt to change parent of plugin logger");
        } else {
            this.log(Level.FINE, "Setting plugin logger parent to {0}", parent);
            super.setParent(parent);
        }
    }

    @Override
    public void log(LogRecord record) {
        if (this.config.get().showDebugLog()) {
            super.log(new LogRecord(Level.INFO, "[Debug] " + record.getMessage()));
        }
        super.log(record);
    }

    @Override
    public void log(Level level, String msg) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.log(level, msg);
    }

    @Override
    public void log(Level level, Supplier<String> msgSupplier) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
        }
        super.log(level, msgSupplier);
    }

    @Override
    public void log(Level level, String msg, Object param1) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.log(level, msg, param1);
    }

    @Override
    public void log(Level level, String msg, Object[] params) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.log(level, msg, params);
    }

    @Override
    public void log(Level level, String msg, Throwable thrown) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.log(level, msg, thrown);
    }

    @Override
    public void log(Level level, Throwable thrown, Supplier<String> msgSupplier) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
        }
        super.log(level, thrown, msgSupplier);
    }

    @Override
    public void logp(Level level, String sourceClass, String sourceMethod, String msg) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logp(level, sourceClass, sourceMethod, msg);
    }

    @Override
    public void logp(Level level, String sourceClass, String sourceMethod, Supplier<String> msgSupplier) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
        }
        super.logp(level, sourceClass, sourceMethod, msgSupplier);
    }

    @Override
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logp(level, sourceClass, sourceMethod, msg, param1);
    }

    @Override
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logp(level, sourceClass, sourceMethod, msg, params);
    }

    @Override
    public void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logp(level, sourceClass, sourceMethod, msg, thrown);
    }

    @Override
    public void logp(Level level, String sourceClass, String sourceMethod, Throwable thrown, Supplier<String> msgSupplier) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
        }
        super.logp(level, sourceClass, sourceMethod, thrown, msgSupplier);
    }

    @Override
    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg);
    }

    @Override
    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg, param1);
    }

    @Override
    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg, params);
    }

    @Override
    public void logrb(Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg, Object... params) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, sourceClass, sourceMethod, bundle, msg, params);
    }

    @Override
    public void logrb(Level level, ResourceBundle bundle, String msg, Object... params) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, bundle, msg, params);
    }

    @Override
    public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown);
    }

    @Override
    public void logrb(Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg, Throwable thrown) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, sourceClass, sourceMethod, bundle, msg, thrown);
    }

    @Override
    public void logrb(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        if (this.config.get().showDebugLog()) {
            level = Level.INFO;
            msg = "[Debug] " + msg;
        }
        super.logrb(level, bundle, msg, thrown);
    }
}
