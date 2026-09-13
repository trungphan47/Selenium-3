package com.sele3.waits;

import org.openqa.selenium.JavascriptExecutor;

import static com.sele3.waits.WaitFactory.createWait;

public final class PageWait {

    private PageWait() {
    }

    /**
     * Waits until the current page has finished loading.
     */
    public static void waitForPageLoad() {
        createWait().until(driver ->
                "complete".equals(
                        ((JavascriptExecutor) driver)
                                .executeScript(
                                        "return document.readyState"
                                )
                )
        );
    }
}
