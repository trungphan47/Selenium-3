package com.sele3.waits;

import com.sele3.driver.DriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Provides retry support for actions that may temporarily fail
 * because of transient Selenium conditions.
 */
public final class RetryAction {

    private static final List<Class<? extends Throwable>> DEFAULT_EXCEPTIONS =
            List.of(
                    NoSuchElementException.class,
                    StaleElementReferenceException.class
            );

    private RetryAction() {
    }

    /**
     * Repeatedly executes the specified action until it succeeds
     * or the configured timeout is reached.
     *
     * <p>The configured timeout and polling interval are used.
     * The specified exceptions are ignored while retrying.</p>
     *
     * @param action     action to execute
     * @param exceptions exceptions to ignore while retrying
     */
    public static void retry(
            Supplier<Boolean> action,
            List<Class<? extends Throwable>> exceptions
    ) {
        retry(
                action,
                DriverManager.getConfiguration().getTimeout(),
                DriverManager.getConfiguration().getPollingInterval(),
                exceptions
        );
    }

    /**
     * Repeatedly executes the specified action until it succeeds
     * or the specified timeout is reached.
     *
     * @param action          action to execute
     * @param timeout         maximum time to retry the action
     * @param pollingInterval interval between retry attempts
     * @param exceptions      exceptions to ignore while retrying
     */
    public static void retry(
            Supplier<Boolean> action,
            Duration timeout,
            Duration pollingInterval,
            List<Class<? extends Throwable>> exceptions
    ) {
        if (timeout.isZero()) {
            if (!Boolean.TRUE.equals(action.get())) {
                throw new TimeoutException(
                        "Condition was not satisfied in a single attempt (timeout: 0 ms)."
                );
            }
            return;
        }

        SeleniumWait<WebDriver> wait =
                new SeleniumWait<>(DriverManager.getDriver());

        wait.withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoreAll(exceptions)
                .until(ignored -> action.get());
    }

    /**
     * Executes an operation that returns a value with automatic retry.
     *
     * <p>The configured timeout and polling interval are used.
     * The operation is retried when one of the specified exceptions
     * is thrown. Any returned value, including {@code null},
     * {@code false}, or an empty string, is considered successful.</p>
     *
     * @param action     operation that returns a value
     * @param exceptions exceptions to ignore while retrying
     * @param <T>        returned value type
     * @return value returned by the successfully executed operation
     */
    public static <T> T retryForValue(
            Supplier<T> action,
            List<Class<? extends Throwable>> exceptions
    ) {
        return retryForValue(
                action,
                DriverManager.getConfiguration().getTimeout(),
                DriverManager.getConfiguration().getPollingInterval(),
                exceptions
        );
    }

    /**
     * Executes a value operation using the specified timeout and configured
     * polling interval.
     *
     * <p>A zero timeout executes the operation once and propagates its
     * exception without starting a wait. Any returned value is successful.</p>
     *
     * @param action     operation that returns a value
     * @param timeout    maximum time to retry the operation
     * @param exceptions exceptions to ignore while retrying
     * @param <T>        returned value type
     * @return value returned by the successfully executed operation
     */
    public static <T> T retryForValue(
            Supplier<T> action,
            Duration timeout,
            List<Class<? extends Throwable>> exceptions
    ) {
        if (timeout.isZero()) {
            return action.get();
        }

        return retryForValue(
                action,
                timeout,
                DriverManager.getConfiguration().getPollingInterval(),
                exceptions
        );
    }

    /**
     * Executes an operation that returns a value with automatic retry.
     *
     * <p>The operation is retried when one of the specified exceptions
     * is thrown. Any returned value, including {@code null},
     * {@code false}, or an empty string, is considered successful.</p>
     *
     * @param action          operation that returns a value
     * @param timeout         maximum time to retry the operation
     * @param pollingInterval interval between retry attempts
     * @param exceptions      exceptions to ignore while retrying
     * @param <T>             returned value type
     * @return value returned by the successfully executed operation
     */
    public static <T> T retryForValue(
            Supplier<T> action,
            Duration timeout,
            Duration pollingInterval,
            List<Class<? extends Throwable>> exceptions
    ) {
        if (timeout.isZero()) {
            return action.get();
        }

        AtomicReference<T> result = new AtomicReference<>();

        retry(
                () -> {
                    result.set(action.get());
                    return true;
                },
                timeout,
                pollingInterval,
                exceptions
        );

        return result.get();
    }


    /**
     * Repeatedly executes the specified action using the configured
     * timeout and polling interval.
     *
     * @param action action to execute
     */
    public static void retry(Supplier<Boolean> action) {
        retry(action, DEFAULT_EXCEPTIONS);
    }

    /**
     * Repeatedly executes the specified action using the specified
     * timeout and polling interval.
     *
     * @param action action to execute
     * @param timeout maximum time to retry the action
     * @param pollingInterval interval between retry attempts
     */
    public static void retry(
            Supplier<Boolean> action,
            Duration timeout,
            Duration pollingInterval
    ) {
        retry(
                action,
                timeout,
                pollingInterval,
                DEFAULT_EXCEPTIONS
        );
    }
}
