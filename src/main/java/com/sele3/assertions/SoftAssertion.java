package com.sele3.assertions;

import com.sele3.driver.DriverManager;
import com.sele3.report.ReportManager;
import com.sele3.waits.RetryAction;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Evaluates assertions and collects failures without immediately
 * stopping test execution.
 *
 * <p>Conditions supplied through {@link Supplier} are automatically
 * retried until they meet the expectation or the timeout is reached.</p>
 */
final class SoftAssertion {

    /**
     * Stores assertion failures collected during the current test.
     */
    private final List<AssertionError> errors = new ArrayList<>();

    /**
     * Immediately verifies that the specified condition is {@code true}.
     *
     * @param condition evaluated condition
     * @param message   failure message
     */
    void assertTrue(boolean condition, String message) {
        if (condition) {
            reportPassed(message, true, condition);
            return;
        }

        addFailure(message, true, condition, null);
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code true} or the configured timeout is reached.
     *
     * @param condition condition to evaluate
     * @param message   failure message
     */
    void assertTrue(Supplier<Boolean> condition, String message) {
        assertBoolean(
                condition,
                true,
                DriverManager.getConfiguration().getTimeout(),
                message
        );
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code true} or the specified timeout is reached.
     *
     * @param condition condition to evaluate
     * @param timeout   maximum time to wait
     * @param message   failure message
     */
    void assertTrue(Supplier<Boolean> condition, Duration timeout, String message) {
        assertBoolean(condition, true, timeout, message);
    }

    /**
     * Immediately verifies that the specified condition is {@code false}.
     *
     * @param condition evaluated condition
     * @param message   failure message
     */
    void assertFalse(boolean condition, String message) {
        if (!condition) {
            reportPassed(message, false, condition);
            return;
        }

        addFailure(message, false, condition, null);
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code false} or the configured timeout is reached.
     *
     * @param condition condition to evaluate
     * @param message   failure message
     */
    void assertFalse(Supplier<Boolean> condition, String message) {
        assertBoolean(
                condition,
                false,
                DriverManager.getConfiguration().getTimeout(),
                message
        );
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code false} or the specified timeout is reached.
     *
     * @param condition condition to evaluate
     * @param timeout   maximum time to wait
     * @param message   failure message
     */
    void assertFalse(Supplier<Boolean> condition, Duration timeout, String message) {
        assertBoolean(condition, false, timeout, message);
    }

    /**
     * Immediately verifies that the actual value equals the expected value.
     *
     * @param actual   actual value
     * @param expected expected value
     * @param message  failure message
     * @param <T>      value type
     */
    <T> void assertEquals(T actual, T expected, String message) {
        if (Objects.deepEquals(actual, expected)) {
            reportPassed(message, expected, actual);
            return;
        }

        addFailure(message, expected, actual, null);
    }

    /**
     * Repeatedly retrieves the actual value until it equals the expected
     * value or the configured timeout is reached.
     *
     * @param actualSupplier supplier that retrieves the actual value
     * @param expected       expected value
     * @param message        failure message
     * @param <T>            value type
     */
    <T> void assertEquals(Supplier<T> actualSupplier, T expected, String message) {
        assertEquals(
                actualSupplier,
                expected,
                DriverManager.getConfiguration().getTimeout(),
                message
        );
    }

    /**
     * Repeatedly retrieves the actual value until it equals the expected
     * value or the specified timeout is reached.
     *
     * @param actualSupplier supplier that retrieves the actual value
     * @param expected       expected value
     * @param timeout        maximum time to wait
     * @param message        failure message
     * @param <T>            value type
     */
    <T> void assertEquals(
            Supplier<T> actualSupplier,
            T expected,
            Duration timeout,
            String message
    ) {
        AtomicReference<T> lastActual = new AtomicReference<>();
        AtomicBoolean actualRetrieved = new AtomicBoolean(false);

        Supplier<Boolean> comparison = () -> {
            T actual = actualSupplier.get();
            lastActual.set(actual);
            actualRetrieved.set(true);
            return Objects.deepEquals(actual, expected);
        };

        try {
            retry(comparison, timeout, message);
            reportPassed(message, expected, lastActual.get());
        } catch (TimeoutException e) {
            addRetryFailure(
                    message,
                    expected,
                    lastActual.get(),
                    actualRetrieved.get(),
                    timeout,
                    e
            );
        }
    }

    /**
     * Throws an aggregated assertion error containing all collected
     * failures.
     *
     * <p>The collected failures are cleared before the aggregated
     * error is thrown.</p>
     *
     * @param message aggregated failure message
     */
    void assertAll(String message) {
        if (errors.isEmpty()) {
            return;
        }

        List<AssertionError> failures = List.copyOf(errors);
        errors.clear();

        StringBuilder details = new StringBuilder(
                message + " Total failed checkpoints: " + failures.size()
        );

        for (int i = 0; i < failures.size(); i++) {
            details.append("\n\n")
                    .append(i + 1)
                    .append(") ")
                    .append(failures.get(i).getMessage());
        }

        AssertionError aggregatedError =
                new AssertionError(details.toString());

        failures.forEach(aggregatedError::addSuppressed);

        throw aggregatedError;
    }

    /**
     * Repeatedly evaluates a boolean condition until its value matches
     * the expected value.
     *
     * @param condition condition to evaluate
     * @param expected  expected boolean value
     * @param timeout   maximum time to wait
     * @param message   failure message
     */
    private void assertBoolean(
            Supplier<Boolean> condition,
            boolean expected,
            Duration timeout,
            String message
    ) {
        AtomicReference<Boolean> lastActual = new AtomicReference<>();
        AtomicBoolean actualRetrieved = new AtomicBoolean(false);

        Supplier<Boolean> comparison = () -> {
            Boolean actual = condition.get();
            lastActual.set(actual);
            actualRetrieved.set(true);
            return Objects.equals(actual, expected);
        };

        try {
            retry(comparison, timeout, message);
            reportPassed(message, expected, lastActual.get());
        } catch (TimeoutException e) {
            addRetryFailure(
                    message,
                    expected,
                    lastActual.get(),
                    actualRetrieved.get(),
                    timeout,
                    e
            );
        }
    }

    /**
     * Repeatedly evaluates a condition using the specified timeout
     * and the configured polling interval.
     *
     * @param condition condition to evaluate
     * @param timeout   maximum time to wait
     * @param message   assertion description for timeout messages
     */
    private void retry(
            Supplier<Boolean> condition,
            Duration timeout,
            String message
    ) {
        RetryAction.retry(
                condition,
                timeout,
                DriverManager.getConfiguration().getPollingInterval(),
                List.of(
                        NoSuchElementException.class,
                        StaleElementReferenceException.class
                ),
                () -> "Evaluate assertion: " + message
        );
    }

    /**
     * Creates and stores an assertion failure.
     *
     * @param message  assertion message
     * @param expected expected value
     * @param actual   actual value
     * @param cause    failure cause, or {@code null} when no cause is available
     */
    private void addFailure(
            String message,
            Object expected,
            Object actual,
            Throwable cause
    ) {
        String details = formatDetails(message, expected, actual);

        AssertionError error = cause == null
                ? new AssertionError(details)
                : new AssertionError(details, cause);

        errors.add(error);

        reportFailed(details);
    }

    /**
     * Creates and stores a retry assertion failure with the configured
     * timeout and the last successfully retrieved actual value.
     *
     * <p>If no evaluation returned a value, the actual value is reported
     * as {@code <not retrieved>}. A successfully retrieved {@code null}
     * value remains {@code null}.</p>
     *
     * @param message         assertion message
     * @param expected        expected value
     * @param lastActual      last successfully retrieved actual value
     * @param actualRetrieved whether any evaluation returned an actual value
     * @param timeout         configured assertion timeout
     * @param cause           failure cause
     */
    private void addRetryFailure(
            String message,
            Object expected,
            Object lastActual,
            boolean actualRetrieved,
            Duration timeout,
            Throwable cause
    ) {
        String details = String.format(
                "%s%nTimeout: %d ms%nExpected: %s%nLast actual: %s",
                message,
                timeout.toMillis(),
                expected,
                actualRetrieved ? lastActual : "<not retrieved>"
        ).trim();

        AssertionError error = new AssertionError(details, cause);

        errors.add(error);

        reportFailed(details);
    }

    /**
     * Formats an assertion message with its expected and actual values.
     *
     * @param message  assertion message
     * @param expected expected value
     * @param actual   actual value
     * @return formatted assertion details
     */
    private String formatDetails(String message, Object expected, Object actual) {
        return String.format(
                "%s Expected: %s, Actual: %s.",
                message,
                expected,
                actual
        ).trim();
    }

    /**
     * Records a successful assertion in the report.
     *
     * @param message  assertion message
     * @param expected expected value
     * @param actual   actual value
     */
    private void reportPassed(String message, Object expected, Object actual) {
        ReportManager.getProvider().pass(
                "Assertion Passed: " + formatDetails(message, expected, actual)
        );
    }

    /**
     * Records a failed assertion in the report.
     *
     * @param details assertion failure details
     */
    private void reportFailed(String details) {
        ReportManager.getProvider().fail("Assertion Failed: " + details);
    }
}
