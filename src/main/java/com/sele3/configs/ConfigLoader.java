package com.sele3.configs;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;


public class ConfigLoader {

    private ConfigLoader() {
    }

    /**
     * Loads configuration from the specified properties file.
     *
     * @param fileName name of the configuration file
     * @return populated Configuration object
     */
    public static Configuration load(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Configuration file not found: " + fileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + fileName, e);
        }
        Configuration configuration = new Configuration();
        configuration.setPlatform(properties.getProperty("platform"));
        configuration.setHeadless(Boolean.parseBoolean(properties.getProperty("headless", "false")));
        configuration.setRemote(Boolean.parseBoolean(properties.getProperty("remote")));
        configuration.setRemoteUrl(properties.getProperty("remoteUrl"));
        configuration.setBrowserSize(properties.getProperty("browserSize"));
        configuration.setStartMaximized(Boolean.parseBoolean(properties.getProperty("startMaximized", "false")));
        configuration.setPageLoadStrategy(PageLoadStrategy.valueOf(properties.getProperty("pageLoadStrategy")));
        configuration.setBaseUrl(properties.getProperty("baseUrl"));
        configuration.setTimeout(Duration.ofSeconds(Long.parseLong(properties.getProperty("timeout", "30"))));
        configuration.setPollingInterval(Duration.ofMillis(Long.parseLong(properties.getProperty("pollingInterval", "500"))));
        configuration.setClickViaJs(Boolean.parseBoolean(properties.getProperty("clickViaJs", "false")));
        configuration.setCapabilities(loadCapabilities(properties));
        return configuration;
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
