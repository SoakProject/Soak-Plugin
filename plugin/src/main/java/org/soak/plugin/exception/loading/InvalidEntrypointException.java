package org.soak.plugin.exception.loading;

public class InvalidEntrypointException extends IllegalStateException {

    public InvalidEntrypointException(String entrypoint) {
        this(entrypoint, null);
    }

    public InvalidEntrypointException(String entrypoint, Class<?> type) {
        super("Invalid entrypoint for " + entrypoint + ((type == null) ? "" : type.getName() + " is not extending JavaPlugin"));
    }
}
