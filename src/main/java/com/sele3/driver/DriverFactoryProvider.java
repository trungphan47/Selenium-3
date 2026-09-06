package com.sele3.driver;

import com.sele3.configs.Configuration;
import com.sele3.driver.browserFactories.ChromeDriverFactory;
import com.sele3.driver.browserFactories.EdgeDriverFactory;
import com.sele3.driver.browserFactories.FirefoxDriverFactory;

public class DriverFactoryProvider {

    private DriverFactoryProvider() {
    }

    /**
     * Returns a DriverFactory based on the configured platform and execution mode.
     *
     * @param configuration driver configuration
     * @return appropriate DriverFactory
     * @throws IllegalArgumentException if the platform is not supported
     */
    public static DriverFactory getFactory(Configuration configuration) {
        if (configuration.isRemote()) {
            return new RemoteDriverFactory();
        }

        switch (configuration.getPlatform().toLowerCase()) {
            case "chrome":
                return new ChromeDriverFactory();

            case "firefox":
                return new FirefoxDriverFactory();

            case "edge":
                return new EdgeDriverFactory();

            default:
                throw new IllegalArgumentException(
                        "Unsupported platform: " + configuration.getPlatform()
                );
        }
    }
}
