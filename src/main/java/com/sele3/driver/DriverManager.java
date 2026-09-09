package com.sele3.driver;

import com.sele3.configs.Configuration;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    /**
     * Creates and stores a WebDriver instance for the current thread.
     *
     * @param configuration driver configuration
     */
    public static void start(Configuration configuration) {
        DriverFactory factory = DriverFactoryProvider.getFactory(configuration);

        DRIVER.set(factory.create(configuration));
    }

    /**
     * Returns the WebDriver instance for the current thread.
     *
     * @return WebDriver instance
     * @throws IllegalStateException if WebDriver has not been initialized
     */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized. Call DriverManager.start() first.");
        }

        return driver;
    }

    /**
     * Quits and removes the WebDriver instance for the current thread.
     */
    public static void quit() {
        WebDriver driver = DRIVER.get();

        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
