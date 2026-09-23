package com.sele3.tests;

import com.sele3.assertions.Assertion;
import com.sele3.base.TestBase;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class AssertionRetryTest extends TestBase {


    @Test
    public void shouldRetryUntilConditionBecomesTrue() {
        AtomicInteger attempts = new AtomicInteger();

        report.step("Step 1: Verify that assertTrue retries until the condition becomes true");
        Assertion.assertTrue(() -> attempts.incrementAndGet() >= 3, Duration.ofSeconds(3), "Condition should become true after retry");

        report.step("Step 2: Verify that the condition was evaluated multiple times");
        Assertion.assertTrue(attempts.get() >= 3, "Condition should be evaluated at least three times");
    }

    @Test
    public void shouldRetryUntilConditionBecomesFalse() {
        AtomicInteger attempts = new AtomicInteger();

        report.step("Step 1: Verify that assertFalse retries until the condition becomes false");
        Assertion.assertFalse(() -> attempts.incrementAndGet() < 3, Duration.ofSeconds(3), "Condition should become false after retry");

        report.step("Step 2: Verify that the condition was evaluated multiple times");
        Assertion.assertTrue(attempts.get() >= 3, "Condition should be evaluated at least three times");
    }

    @Test
    public void shouldRetryUntilActualValueMatchesExpectedValue() {
        AtomicInteger attempts = new AtomicInteger();

        report.step("Step 1: Verify that assertEquals retries until the actual value becomes Ready");
        Assertion.assertEquals(() -> attempts.incrementAndGet() >= 3 ? "Ready" : "Pending", "Ready", Duration.ofSeconds(3), "Status should become Ready after retry");

        report.step("Step 2: Verify that the actual value was retrieved multiple times");
        Assertion.assertTrue(attempts.get() >= 3, "Actual value should be retrieved at least three times");
    }
}
