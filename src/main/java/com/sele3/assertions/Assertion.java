package com.sele3.assertions;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Provides thread-safe soft assertions with automatic retry support.
 *
 * <p>Assertions are stored separately for each thread, allowing tests
 * to execute in parallel without sharing assertion failures.</p>
 *
 * <p>When running with TestNG, {@link AssertionListener} manages the assertion
 * lifecycle for each test. Other runners can call {@link #start()} and
 * {@link #assertAll()} explicitly.</p>
 */
public final class Assertion {

    /**
     * Stores the soft assertion instance associated with each test thread.
     */
    private static final ThreadLocal<SoftAssertion> ASSERTION = new ThreadLocal<>();

    /**
     * Prevents instantiation of this utility class.
     */
    private Assertion() {
    }

    /**
     * Initializes a new soft assertion instance for the current thread.
     *
     * <p>This method should be called before each test method.</p>
     */
    public static void start() {
        ASSERTION.set(new SoftAssertion());
    }

    /**
     * Immediately verifies that the specified condition is {@code true}.
     *
     * <p>This overload does not provide automatic retry because the
     * condition has already been evaluated.</p>
     *
     * @param condition evaluated condition
     * @param message   failure message
     */
    public static void assertTrue(boolean condition, String message) {
        get().assertTrue(condition, message);
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code true} or the configured timeout is reached.
     *
     * @param condition condition to evaluate
     * @param message   failure message
     */
    public static void assertTrue(Supplier<Boolean> condition, String message) {
        get().assertTrue(condition, message);
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code true} or the specified timeout is reached.
     *
     * @param condition       condition to evaluate
     * @param timeout         maximum time to wait
     * @param message         failure message
     */
    public static void assertTrue(
            Supplier<Boolean> condition,
            Duration timeout,
            String message
    ) {
        get().assertTrue(condition, timeout, message);
    }

    /**
     * Immediately verifies that the specified condition is {@code false}.
     *
     * <p>This overload does not provide automatic retry because the
     * condition has already been evaluated.</p>
     *
     * @param condition evaluated condition
     * @param message   failure message
     */
    public static void assertFalse(boolean condition, String message) {
        get().assertFalse(condition, message);
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code false} or the configured timeout is reached.
     *
     * @param condition condition to evaluate
     * @param message   failure message
     */
    public static void assertFalse(Supplier<Boolean> condition, String message) {
        get().assertFalse(condition, message);
    }

    /**
     * Repeatedly evaluates the specified condition until it becomes
     * {@code false} or the specified timeout is reached.
     *
     * @param condition       condition to evaluate
     * @param timeout         maximum time to wait
     * @param message         failure message
     */
    public static void assertFalse(
            Supplier<Boolean> condition,
            Duration timeout,
            String message
    ) {
        get().assertFalse(condition, timeout, message);
    }

    /**
     * Immediately verifies that the actual value equals the expected value.
     *
     * <p>This overload does not provide automatic retry because the
     * actual value has already been retrieved.</p>
     *
     * @param actual   actual value
     * @param expected expected value
     * @param message  failure message
     * @param <T>      value type
     */
    public static <T> void assertEquals(T actual, T expected, String message) {
        get().assertEquals(actual, expected, message);
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
    public static <T> void assertEquals(
            Supplier<T> actualSupplier,
            T expected,
            String message
    ) {
        get().assertEquals(actualSupplier, expected, message);
    }

    /**
     * Repeatedly retrieves the actual value until it equals the expected
     * value or the specified timeout is reached.
     *
     * @param actualSupplier  supplier that retrieves the actual value
     * @param expected        expected value
     * @param timeout         maximum time to wait
     * @param message         failure message
     * @param <T>             value type
     */
    public static <T> void assertEquals(
            Supplier<T> actualSupplier,
            T expected,
            Duration timeout,
            String message
    ) {
        get().assertEquals(actualSupplier, expected, timeout, message);
    }

    /**
     * Throws an aggregated {@link AssertionError} if any checkpoint
     * failed for the current thread.
     *
     * <p>The assertion instance is removed from the current thread
     * after all failures have been processed.</p>
     */
    public static void assertAll() {
        assertAll("Soft assertion failed.");
    }

    /**
     * Throws an aggregated {@link AssertionError} with the specified
     * message if any checkpoint failed for the current thread.
     *
     * <p>The assertion instance is removed from the current thread
     * after all failures have been processed.</p>
     *
     * @param message aggregated failure message
     */
    public static void assertAll(String message) {
        try {
            get().assertAll(message);
        } finally {
            ASSERTION.remove();
        }
    }

    /**
     * Returns the soft assertion associated with the current thread.
     *
     * @return current thread soft assertion
     * @throws IllegalStateException if assertions have not been initialized
     */
    private static SoftAssertion get() {
        SoftAssertion assertion = ASSERTION.get();

        if (assertion == null) {
            throw new IllegalStateException(
                    "Assertion has not been initialized. Call Assertion.start() first."
            );
        }

        return assertion;
    }

}
