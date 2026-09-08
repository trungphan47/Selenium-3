package com.sele3.driver.browserFactories;

import com.google.auto.service.AutoService;
import com.sele3.configs.Configuration;
import com.sele3.driver.DriverFactory;
import com.sele3.driver.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

@AutoService(DriverFactory.class)
public class EdgeDriverFactory implements DriverFactory {


    /**
     * Creates a EdgeDriver using the provided configuration.
     *
     * @param configuration driver configuration
     * @return configured EdgeDriver
     */
    @Override
    public WebDriver create(Configuration configuration) {
        EdgeOptions options = new EdgeOptions();

        if (configuration.isHeadless()) {
            options.addArguments("--headless=new");
        }

        if (configuration.getPageLoadStrategy() != null) {
            options.setPageLoadStrategy(
                    configuration.getPageLoadStrategy()
            );
        }

        return new EdgeDriver(options);
    }
}
