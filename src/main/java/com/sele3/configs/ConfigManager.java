package com.sele3.configs;

public final class ConfigManager {

    private static final ThreadLocal<Configuration> CONFIGURATION =
            new ThreadLocal<>();

    private ConfigManager() {
    }

    /**
     * Stores the configuration for the current thread.
     *
     * @param configuration configuration to store
     */
    public static void set(Configuration configuration) {
        CONFIGURATION.set(configuration);
    }

    /**
     * Returns the configuration associated with the current thread.
     *
     * @return current thread configuration
     * @throws IllegalStateException if the configuration has not been initialized
     */
    public static Configuration get() {
        Configuration configuration = CONFIGURATION.get();

        if (configuration == null) {
            throw new IllegalStateException(
                    "Configuration has not been initialized."
            );
        }

        return configuration;
    }

    /**
     * Removes the configuration associated with the current thread.
     */
    public static void remove() {
        CONFIGURATION.remove();
    }
}
