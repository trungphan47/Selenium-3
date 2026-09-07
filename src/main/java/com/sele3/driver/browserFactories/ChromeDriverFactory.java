package com.sele3.driver.browserFactories;

import com.sele3.configs.Configuration;
import com.sele3.driver.DriverFactory;
import com.sele3.driver.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory implements DriverFactory {

    /**
     * Returns the platform name supported by this driver factory.
     *
     * @return the platform name in lowercase, e.g. {@code "safari"}
     */
    @Override
    public String  getPlatform() {
        return Platform.Chrome.name().toLowerCase();
    }

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
