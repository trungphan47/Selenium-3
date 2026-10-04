package com.sele3.tests;

import com.sele3.assertions.Assertion;
import com.sele3.report.ReportManager;
import com.sele3.report.ReportProvider;
import org.testng.annotations.Test;

public class AssertionTest {

    private final ReportProvider report = ReportManager.getProvider();

    @Test
    public void shouldAssertTrueCondition() {
        report.step("Step 1: Verify that the condition is true");
        Assertion.assertTrue(true, "Condition should be true");
    }

    @Test
    public void shouldAssertFalseCondition() {
        report.step("Step 1: Verify that the condition is false");
        Assertion.assertFalse(false, "Condition should be false");
    }

    @Test
    public void shouldAssertEqualValues() {
        report.step("Step 1: Verify that the status is Ready");
        Assertion.assertEquals("Ready", "Ready", "Status should match");
    }

    @Test
    public void shouldContinueAfterSuccessfulAssertions() {
        report.step("Step 1: Verify that the first condition is true");
        Assertion.assertTrue(true, "First condition should be true");

        report.step("Step 2: Verify that the second condition is false");
        Assertion.assertFalse(false, "Second condition should be false");

        report.step("Step 3: Verify that the actual value matches the expected value");
        Assertion.assertEquals(3, 3, "Values should match");
    }

    @Test
    public void shouldContinueAfterFailedCheckpoint() {
        report.step("Step 1: Verify that the first condition is true");
        Assertion.assertTrue(false, "First condition should be true");

        report.step("Step 2: Verify that the status is Ready");
        Assertion.assertEquals("Pending", "Ready", "Status should match");

        report.step("Step 3: Verify that the last condition is false");
        Assertion.assertFalse(false, "Last condition should be false");
    }
}
