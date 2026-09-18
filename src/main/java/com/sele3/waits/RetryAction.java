package com.sele3.waits;

import com.sele3.configs.ConfigManager;
import com.sele3.driver.DriverManager;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Provides retry support for actions that may temporarily fail
 * because of transient Selenium conditions.
 */
public final class RetryAction {

    private RetryAction() {
    }

    /**
     * Repeatedly executes the specified action until it succeeds
     * or the configured timeout is reached.
     *
     * <p>The action is retried using the configured polling interval.
     * The specified exceptions are ignored while retrying.</p>
     *
     * @param action     action to execute
     * @param exceptions exceptions to ignore while retrying
     */
    public static void retry(Supplier<Boolean> action, List<Class<? extends Throwable>> exceptions) {

        SeleniumWait<WebDriver> wait =
                new SeleniumWait<>(DriverManager.getDriver());

        wait.withTimeout(ConfigManager.get().getTimeout())
                .pollingEvery(ConfigManager.get().getPollingInterval())
                .ignoreAll(exceptions)
                .until(driver -> action.get());
    }

    /**
     * Executes an operation that returns a value with automatic retry.
     *
     * <p>The operation is retried when one of the specified exceptions
     * is thrown. Returning {@code null}, {@code false}, or an empty
     * string is considered a successful execution.</p>
     *
     * @param action     operation that returns a value
     * @param exceptions transient exceptions to ignore and retry
     * @param <T>        returned value type
     * @return value returned by the successfully executed operation
     */
    public static <T> T retryForValue(Supplier<T> action, List<Class<? extends Throwable>> exceptions) {
        AtomicReference<T> result = new AtomicReference<>();

        retry(
                () -> {
                    result.set(action.get());
                    return true;
                },
                exceptions
        );

        return result.get();
    }
}
