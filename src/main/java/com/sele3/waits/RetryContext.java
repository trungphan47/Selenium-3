package com.sele3.waits;

import org.openqa.selenium.TimeoutException;

import java.util.function.Supplier;

/** Coordinates polling on the current thread. */
final class RetryContext {

    private static final ThreadLocal<Boolean> ACTIVE = new ThreadLocal<>();

    private RetryContext() {
    }

    static boolean isActive() {
        return Boolean.TRUE.equals(ACTIVE.get());
    }

    static <T> T evaluate(Supplier<T> action) {
        Boolean previous = ACTIVE.get();
        ACTIVE.set(true);
        try {
            return action.get();
        } finally {
            if (previous == null) {
                ACTIVE.remove();
            } else {
                ACTIVE.set(previous);
            }
        }
    }

    /** Preserves the success contract of a nested condition wait. */
    static <T> T requireSatisfied(T result) {
        if (result == null || Boolean.FALSE.equals(result)) {
            throw new ConditionNotSatisfiedException();
        }
        return result;
    }

    /** Signals a pending nested condition, rather than an expired timeout. */
    static final class ConditionNotSatisfiedException extends TimeoutException {
        private static final long serialVersionUID = 1L;

        private ConditionNotSatisfiedException() {
            super("Nested wait condition is not satisfied in this polling attempt.");
        }
    }
}
