package com.sele3.driver.browserFactories;

import com.google.auto.service.AutoService;
import com.sele3.configs.Configuration;
import com.sele3.driver.DriverFactory;
import com.sele3.driver.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

@AutoService(DriverFactory.class)
public class SafariDriverFactory implements DriverFactory {

    /**
     * Creates a Safari using the provided configuration.
     *
     * @param configuration driver configuration
     * @return configured Safari
     */
    @Override
    public WebDriver create(Configuration configuration) {
        SafariOptions options = new SafariOptions();

        options.setPageLoadStrategy(configuration.getPageLoadStrategy());

        return new SafariDriver(options);
    }
}
