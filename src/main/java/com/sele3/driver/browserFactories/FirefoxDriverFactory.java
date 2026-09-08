package com.sele3.driver.browserFactories;

import com.google.auto.service.AutoService;
import com.sele3.configs.Configuration;
import com.sele3.driver.DriverFactory;
import com.sele3.driver.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

@AutoService(DriverFactory.class)
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

        options.setPageLoadStrategy(configuration.getPageLoadStrategy());

        return new FirefoxDriver(options);
    }
}
