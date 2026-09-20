package com.sele3.driver;

import com.sele3.configs.Configuration;
import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    private static final ThreadLocal<Configuration> CONFIGURATION =
            new ThreadLocal<>();

    private DriverManager() {
    }

    /**
     * Creates and stores the WebDriver and configuration
     * for the current thread.
     *
     * @param configuration driver configuration
     */
    public static void start(Configuration configuration) {
        DriverFactory factory = DriverFactoryProvider.getFactory(configuration);

        WebDriver driver = factory.create(configuration);

        DRIVER.set(driver);
        CONFIGURATION.set(configuration);
    }

    /**
     * Returns the WebDriver for the current thread.
     *
     * @return current WebDriver
     * @throws IllegalStateException if WebDriver has not been initialized
     */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized. "
                            + "Call DriverManager.start() first."
            );
        }

        return driver;
    }

    /**
     * Returns the configuration for the current thread.
     *
     * @return current configuration
     * @throws IllegalStateException if configuration has not been initialized
     */
    public static Configuration getConfiguration() {
        Configuration configuration = CONFIGURATION.get();

        if (configuration == null) {
            throw new IllegalStateException(
                    "Configuration has not been initialized. "
                            + "Call DriverManager.start() first."
            );
        }

        return configuration;
    }

    /**
     * Quits the WebDriver and removes data for the current thread.
     */
    public static void quit() {
        WebDriver driver = DRIVER.get();

        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            DRIVER.remove();
            CONFIGURATION.remove();
        }
    }
}
