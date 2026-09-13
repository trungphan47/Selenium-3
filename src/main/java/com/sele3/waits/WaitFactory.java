package com.sele3.waits;

import com.sele3.configs.ConfigManager;
import com.sele3.driver.DriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitFactory {

    private WaitFactory() {
    }

    /**
     * Creates a WebDriverWait using the configured default timeout
     * and polling interval.
     *
     * @return configured WebDriverWait instance
     */
    public static WebDriverWait createWait() {
        return createWait(ConfigManager.get().getTimeout());
    }

    /**
     * Creates a WebDriverWait using the specified timeout
     * and the configured polling interval.
     *
     * @param timeout maximum time to wait
     * @return configured WebDriverWait instance
     */
    public static WebDriverWait createWait(Duration timeout) {
        return new WebDriverWait(
                DriverManager.getDriver(),
                timeout,
                ConfigManager.get().getPollingInterval()
        );
    }
}
