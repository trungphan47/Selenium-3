package com.sele3.driver;

import com.sele3.configs.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Provides the appropriate {@link DriverFactory} based on the current configuration.
 *
 * <p>Browser driver factories are automatically discovered using Java's
 * {@link ServiceLoader} mechanism.</p>
 */
public final class DriverFactoryProvider {

    private static final Map<String, DriverFactory> FACTORIES = new HashMap<>();

    static {

        ServiceLoader<DriverFactory> loader = ServiceLoader.load(DriverFactory.class);

        for (DriverFactory factory : loader) {
            FACTORIES.put(
                    factory.getPlatform(),
                    factory
            );
        }

    }

    private DriverFactoryProvider() {
    }

    /**
     * Returns the appropriate {@link DriverFactory} based on the given configuration.
     *
     * <p>If remote execution is enabled, a {@link RemoteDriverFactory} is returned.
     * Otherwise, the driver factory is selected from the factories discovered by
     * {@link java.util.ServiceLoader} based on the configured platform.</p>
     *
     * @param configuration the driver configuration
     * @return the driver factory matching the configuration
     * @throws IllegalArgumentException if the configured platform is not supported
     */
    public static DriverFactory getFactory(Configuration configuration) {

        if (configuration.isRemote()) {
            return new RemoteDriverFactory();
        }

        DriverFactory factory = FACTORIES.get(configuration.getPlatform().toLowerCase());

        if (factory == null) {
            throw new IllegalArgumentException(
                    "Unsupported platform: " + configuration.getPlatform()
            );
        }

        return factory;
    }
}