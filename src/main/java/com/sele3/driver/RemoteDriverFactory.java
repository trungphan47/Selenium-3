package com.sele3.driver;

import com.sele3.configs.Configuration;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDriverFactory implements DriverFactory {

    /**
     * Creates a RemoteWebDriver using the provided configuration.
     *
     * @param configuration driver configuration
     * @return configured RemoteWebDriver
     * @throws IllegalArgumentException if the remote URL is invalid
     */
    @Override
    public WebDriver create(Configuration configuration) {
        try {
            URL remoteUrl = new URL(configuration.getRemoteUrl());

            MutableCapabilities capabilities = configuration.getCapabilities();

            if (capabilities == null) {
                capabilities = new MutableCapabilities();
            }

            return new RemoteWebDriver(remoteUrl, capabilities);

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "Invalid remote URL: " + configuration.getRemoteUrl(),
                    e
            );
        }
    }
}
