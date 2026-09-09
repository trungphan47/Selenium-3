package com.sele3.driver.browserFactories;

import com.google.auto.service.AutoService;
import com.sele3.configs.Configuration;
import com.sele3.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@AutoService(DriverFactory.class)
public class ChromeDriverFactory implements DriverFactory {

    /**
     * Creates a ChromeDriver using the provided configuration.
     *
     * @param configuration driver configuration
     * @return configured ChromeDriver
     */
    @Override
    public WebDriver create(Configuration configuration) {
        ChromeOptions options = new ChromeOptions();

        if (configuration.isHeadless()) {
            options.addArguments("--headless=new");
        }

        if (configuration.isStartMaximized()) {
            options.addArguments("--start-maximized");
        }

        if (configuration.getBrowserSize() != null) {
            options.addArguments(
                    "--window-size=" + configuration.getBrowserSize()
            );
        }

        options.setPageLoadStrategy(configuration.getPageLoadStrategy());

        return new ChromeDriver(options);
    }
}
