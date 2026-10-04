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
     * <p>System properties passed through the command line take
     * precedence over values defined in the properties file.</p>
     *
     * @param fileName name of the configuration file
     * @return populated Configuration object
     */
    public static Configuration load(String fileName) {
        Properties properties = loadProperties(fileName);

        Configuration configuration = new Configuration();

        configuration.setPlatform(getBrowser(properties));
        configuration.setHeadless(Boolean.parseBoolean(getProperty(properties, "headless", "false")));
        configuration.setRemote(Boolean.parseBoolean(getProperty(properties, "remote", "false")));
        configuration.setRemoteUrl(getProperty(properties, "remoteUrl", null));
        configuration.setBrowserSize(getProperty(properties, "browserSize", "1366,768"));
        configuration.setStartMaximized(Boolean.parseBoolean(getProperty(properties, "startMaximized", "false")));
        configuration.setPageLoadStrategy(getPageLoadStrategy(properties));
        configuration.setBaseUrl(getProperty(properties, "baseUrl", null));
        configuration.setTimeout(Duration.ofMillis(Long.parseLong(getProperty(properties, "timeout", "10000"))));
        configuration.setPollingInterval(Duration.ofMillis(Long.parseLong(getProperty(properties, "pollingInterval", "500"))));
        configuration.setClickViaJs(Boolean.parseBoolean(getProperty(properties, "clickViaJs", "false")));
        configuration.setCapabilities(loadCapabilities(properties));

        return configuration;
    }

    /**
     * Returns a configuration value using the following priority:
     * system property, properties file, and default value.
     *
     * @param properties configuration properties
     * @param key property key
     * @param defaultValue default value
     * @return resolved property value
     */
    private static String getProperty(Properties properties, String key, String defaultValue) {
        String systemValue = System.getProperty(key);

        if (systemValue != null) {
            return systemValue;
        }

        return properties.getProperty(key, defaultValue);
    }

    /**
     * Returns the configured browser.
     *
     * <p>The "browser" system property is used as a command-line alias
     * for the existing "platform" configuration property.</p>
     *
     * @param properties configuration properties
     * @return configured browser
     */
    private static String getBrowser(Properties properties) {
        String configuredBrowser = getProperty(properties, "platform", "chrome");

        return System.getProperty("browser", configuredBrowser);
    }

    /**
     * Loads properties from the specified configuration file.
     *
     * @param fileName name of the configuration file
     * @return loaded properties
     * @throws IllegalArgumentException if the configuration file is not found
     * @throws RuntimeException if the configuration file cannot be loaded
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
     * Returns the configured page load strategy.
     *
     * @param properties configuration properties
     * @return configured page load strategy
     * @throws IllegalArgumentException if the page load strategy is invalid
     */
    private static PageLoadStrategy getPageLoadStrategy(Properties properties) {
        String value = getProperty(properties, "pageLoadStrategy", "NORMAL");

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
     * Loads Selenium capabilities from the properties file and
     * command-line system properties.
     *
     * @param properties configuration properties
     * @return Selenium capabilities
     */
    private static MutableCapabilities loadCapabilities(Properties properties) {
        Properties capabilityProperties = new Properties();
        capabilityProperties.putAll(properties);

        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            String key = entry.getKey().toString();

            if (key.startsWith("capabilities.")) {
                capabilityProperties.put(key, entry.getValue());
            }
        }

        MutableCapabilities capabilities = new MutableCapabilities();

        for (Map.Entry<Object, Object> entry : capabilityProperties.entrySet()) {
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
