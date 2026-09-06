package com.sele3.driver.browserFactories;

import com.sele3.configs.Configuration;
import com.sele3.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverFactory implements DriverFactory {

    /**
     * Creates a FireFoxDriver using the provided configuration.
     *
     * @param configuration driver configuration
     * @return configured FireFoxDriver
     */
    @Override
    public WebDriver create(Configuration configuration) {
        FirefoxOptions options = new FirefoxOptions();

        if (configuration.isHeadless()) {
            options.addArguments("-headless");
        }

        if (configuration.getPageLoadStrategy() != null) {
            options.setPageLoadStrategy(
                    configuration.getPageLoadStrategy()
            );
        }

        return new FirefoxDriver(options);
    }
}