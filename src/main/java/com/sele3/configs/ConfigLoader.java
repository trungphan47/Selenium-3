package com.sele3.configs;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Locale;
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

        configuration.setPlatform(getProperty(properties, "platform", "chrome"));
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
     * Returns a trimmed configuration value using the following priority:
     * system property, properties file, and default value.
     *
     * @param properties configuration properties
     * @param key property key
     * @param defaultValue default value
     * @return trimmed property value, or {@code null} if no value is configured
     */
    private static String getProperty(Properties properties, String key, String defaultValue) {
        String value = System.getProperty(key);

        if (value == null) {
            value = properties.getProperty(key, defaultValue);
        }

        return value == null ? null : value.trim();
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
            return PageLoadStrategy.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid pageLoadStrategy: " + value,
                    e
            );
        }
    }

    /**
     * Loads only {@code capabilities.*} entries from the configuration file
     * and system properties. System properties override file values.
     *
     * @param properties configuration properties
     * @return Selenium capabilities
     */
    private static MutableCapabilities loadCapabilities(Properties properties) {
        MutableCapabilities capabilities = new MutableCapabilities();

        addCapabilities(capabilities, properties);
        addCapabilities(capabilities, System.getProperties());

        return capabilities;
    }

    /**
     * Applies only {@code capabilities.*} entries from the supplied properties.
     *
     * @param capabilities destination Selenium capabilities
     * @param properties source properties
     */
    private static void addCapabilities(MutableCapabilities capabilities, Properties properties) {
        String prefix = "capabilities.";

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();

            if (key.startsWith(prefix)) {
                Object value = entry.getValue();

                capabilities.setCapability(
                        key.substring(prefix.length()),
                        value instanceof String ? ((String) value).trim() : value
                );
            }
        }
    }
}
