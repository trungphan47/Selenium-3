package com.sele3.report.listeners;

import com.sele3.report.ReportFactory;
import com.sele3.report.ReportProvider;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener responsible for managing report providers
 * throughout the test execution lifecycle.
 *
 * <p>A report provider is created for each test and stored in a
 * {@link ThreadLocal} to support parallel test execution.</p>
 */
public class TestReportListener implements ITestListener {

    private final ThreadLocal<ReportProvider> reportProvider = new ThreadLocal<>();

    /**
     * Initializes the report provider and starts reporting
     * when a test begins.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(">>> onTestStart");

        ReportProvider provider = ReportFactory.create();

        System.out.println(">>> Provider created: " + provider.getName());

        reportProvider.set(provider);

        System.out.println(">>> Provider set: " + reportProvider.get());

        provider.startTest(
                result.getMethod().getMethodName()
        );
    }

    /**
     * Marks the current test as passed and cleans up
     * the report provider.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        provider().pass(
                "Test passed: " + result.getMethod().getMethodName()
        );

        cleanup();
    }

    /**
     * Marks the current test as failed and cleans up
     * the report provider.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        provider().fail(
                "Test failed: " + result.getMethod().getMethodName()
        );

        cleanup();
    }

    /**
     * Records the current test as skipped and cleans up
     * the report provider if it has been initialized.
     *
     * @param result the TestNG test result
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        ReportProvider provider = reportProvider.get();

        if (provider != null) {
            provider.step(
                    "Test skipped: " + result.getMethod().getMethodName()
            );

            cleanup();
        }
    }

    /**
     * Returns the report provider associated with the current thread.
     *
     * @return the current report provider
     * @throws IllegalStateException if the report provider has not been initialized
     */
    private ReportProvider provider() {
        ReportProvider provider = reportProvider.get();

        if (provider == null) {
            throw new IllegalStateException(
                    "ReportProvider has not been initialized for the current test."
            );
        }

        return provider;
    }

    /**
     * Ends reporting for the current test and removes
     * the report provider from the current thread.
     */
    private void cleanup() {
        ReportProvider provider = reportProvider.get();

        if (provider != null) {
            try {
                provider.endTest();
            } finally {
                reportProvider.remove();
            }
        }
    }
}