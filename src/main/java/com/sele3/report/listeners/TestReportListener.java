package com.sele3.report.listeners;

import com.sele3.report.ReportFactory;
import com.sele3.report.ReportManager;
import com.sele3.report.ReportProvider;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener responsible for managing report providers
 * throughout the test execution lifecycle.
 *
 * <p>A single report provider is used throughout the suite execution.
 * Test-specific reporting resources are managed by the provider
 * to support parallel test execution.</p>
 */
public class TestReportListener implements ITestListener, ISuiteListener {

    private final ReportProvider reportProvider = ReportManager.getProvider();

    /**
     * Starts reporting when a test begins.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestStart(ITestResult result) {
        reportProvider.startTest(result.getMethod().getMethodName()
        );
    }

    /**
     * Marks the current test as passed and ensures
     * test-specific reporting resources are cleaned up.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            reportProvider.pass(
                    "Test passed: " + result.getMethod().getMethodName()
            );
        } finally {
            reportProvider.endTest();
        }
    }

    /**
     * Marks the current test as failed and ensures
     * test-specific reporting resources are cleaned up.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        try {
            reportProvider.fail(
                    "Test failed: " + result.getMethod().getMethodName()
            );
        } finally {
            reportProvider.endTest();
        }
    }

    /**
     * Records the current test as skipped and ensures
     * test-specific reporting resources are cleaned up.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            reportProvider.step(
                    "Test skipped: " + result.getMethod().getMethodName()
            );
        } finally {
            reportProvider.endTest();
        }
    }

    /**
     * Finalizes the report after all tests in the suite
     * have completed.
     *
     * @param suite the completed TestNG suite
     */
    @Override
    public void onFinish(ISuite suite) {
        reportProvider.finishReport();
    }
}
