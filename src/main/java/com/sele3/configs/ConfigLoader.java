package com.sele3.configs;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

public final class ConfigLoader {

    private ConfigLoader() {
    }

    /**
     * Loads configuration from the specified properties file.
     *
     * @param fileName name of the configuration file
     * @return populated Configuration object
     */
    public static Configuration load(String fileName) {
        Properties properties = loadProperties(fileName);

        Configuration configuration = new Configuration();

        configuration.setPlatform(properties.getProperty("platform", "chrome"));
        configuration.setHeadless(Boolean.parseBoolean(properties.getProperty("headless", "false")));
        configuration.setRemote(Boolean.parseBoolean(properties.getProperty("remote", "false")));
        configuration.setRemoteUrl(properties.getProperty("remoteUrl"));
        configuration.setBrowserSize(properties.getProperty("browserSize", "1366,768"));
        configuration.setStartMaximized(Boolean.parseBoolean(properties.getProperty("startMaximized", "false")));
        configuration.setPageLoadStrategy(getPageLoadStrategy(properties));
        configuration.setBaseUrl(properties.getProperty("baseUrl"));
        configuration.setTimeout(Duration.ofMillis(Long.parseLong(properties.getProperty("timeout", "10000"))));
        configuration.setPollingInterval(Duration.ofMillis(Long.parseLong(properties.getProperty("pollingInterval", "500"))));
        configuration.setClickViaJs(Boolean.parseBoolean(properties.getProperty("clickViaJs", "false")));
        configuration.setCapabilities(loadCapabilities(properties));

        return configuration;
    }

    /**
     * Loads properties from the specified configuration file.
     *
     * @param fileName name of the configuration file
     * @return loaded properties
     * @throws IllegalArgumentException if the configuration file is not found
     * @throws RuntimeException         if the configuration file cannot be loaded
     */
    private static Properties loadProperties(String fileName) {
        Properties properties = new Properties();

        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream == null) {
                throw new IllegalArgumentException(
                        "Configuration file not found: " + fileName
                );
            }

            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load configuration file: " + fileName,
                    e
            );
        }

        return properties;
    }

    /**
     * Returns the page load strategy from the configuration properties.
     * Defaults to {@link PageLoadStrategy#NORMAL} if the property is not specified.
     *
     * @param properties configuration properties
     * @return configured page load strategy
     * @throws IllegalArgumentException if the page load strategy is invalid
     */
    private static PageLoadStrategy getPageLoadStrategy(Properties properties) {
        String value = properties.getProperty("pageLoadStrategy", "NORMAL");

        try {
            return PageLoadStrategy.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid pageLoadStrategy: " + value,
                    e
            );
        }
    }


    /**
     * Loads Selenium capabilities from properties with the
     * "capabilities." prefix.
     *
     * @param properties configuration properties
     * @return Selenium capabilities
     */
    private static MutableCapabilities loadCapabilities(Properties properties) {
        MutableCapabilities capabilities = new MutableCapabilities();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();

            if (key.startsWith("capabilities.")) {
                String capabilityName = key.substring("capabilities.".length());

                capabilities.setCapability(
                        capabilityName,
                        entry.getValue()
                );
            }
        }

        return capabilities;
    }
}