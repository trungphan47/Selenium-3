package com.sele3.driver.browserFactories;

import com.sele3.configs.Configuration;
import com.sele3.driver.DriverFactory;
import com.sele3.driver.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class SafariDriverFactory implements DriverFactory {

    /**
     * Returns the platform name supported by this driver factory.
     *
     * @return the platform name in lowercase, e.g. {@code "safari"}
     */
    @Override
    public String  getPlatform() {
        return Platform.Safari.name().toLowerCase();
    }

    /**
     * Creates a Safari using the provided configuration.
     *
     * @param configuration driver configuration
     * @return configured Safari
     */
    @Override
    public WebDriver create(Configuration configuration) {
        SafariOptions options = new SafariOptions();

        if (configuration.getPageLoadStrategy() != null) {
            options.setPageLoadStrategy(
                    configuration.getPageLoadStrategy()
            );
        }

        return new SafariDriver(options);
    }
}
