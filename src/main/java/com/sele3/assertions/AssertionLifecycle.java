package com.sele3.assertions;

/**
 * Defines the assertion cleanup performed after a test finishes.
 *
 * <p>Test runner adapters call {@link #afterTest()} from their respective
 * test completion hooks. This keeps assertion lifecycle logic independent
 * of a specific test runner.</p>
 */
public interface AssertionLifecycle {

    /**
     * Verifies all soft assertions for the current thread and releases
     * their state.
     *
     * <p>If one or more checkpoints failed, {@link Assertion#assertAll()}
     * throws an {@link AssertionError}. The thread state is cleared even
     * when verification fails.</p>
     *
     * @throws AssertionError if at least one checkpoint failed
     */
    default void afterTest() {
        try {
            Assertion.assertAll();
        } finally {
            Assertion.clear();
        }
    }
}
