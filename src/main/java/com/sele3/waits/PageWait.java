package com.sele3.waits;

import com.sele3.configs.ConfigManager;
import com.sele3.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Provides wait operations related to page state.
 */
public final class PageWait {

    private PageWait() {
    }

    /**
     * Waits until the current page has finished loading.
     *
     * <p>The page is considered loaded when
     * {@code document.readyState} becomes {@code complete}.</p>
     */
    public static void waitForPageLoad() {
        SeleniumWait<WebDriver> wait =
                new SeleniumWait<>(DriverManager.getDriver());

        wait.withTimeout(ConfigManager.get().getTimeout())
                .pollingEvery(ConfigManager.get().getPollingInterval())
                .until(driver ->
                        "complete".equals(
                                ((JavascriptExecutor) driver)
                                        .executeScript(
                                                "return document.readyState"
                                        )
                        )
                );
    }
}
