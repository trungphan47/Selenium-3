package com.sele3.report.providers;

import com.google.auto.service.AutoService;
import com.sele3.report.ReportProvider;

/**
 * Lightweight report provider that writes test execution information
 * directly to the console.
 *
 * <p>This provider does not generate report files or store attachments.
 * It can be used when a lightweight reporting option is preferred.</p>
 */
@AutoService(ReportProvider.class)
public class ConsoleReportProvider implements ReportProvider {

    /**
     * Logs the start of a test to the console.
     *
     * @param testName the name of the test
     */
    @Override
    public void startTest(String testName) {
        System.out.println("[TEST] " + testName);
    }

    /**
     * Logs a test step to the console.
     *
     * @param message the step message
     */
    @Override
    public void step(String message) {
        System.out.println("[STEP] " + message);
    }

    /**
     * Logs a successful test result to the console.
     *
     * @param message the success message
     */
    @Override
    public void pass(String message) {
        System.out.println("[PASS] " + message);
    }

    /**
     * Logs a failed test result to the console.
     *
     * @param message the failure message
     */
    @Override
    public void fail(String message) {
        System.out.println("[FAIL] " + message);
    }

    /**
     * Ignores attachments because console reporting
     * does not support storing attachment content.
     *
     * @param name the attachment name
     * @param content the attachment content
     */
    @Override
    public void attach(String name, byte[] content) {
        // Attachments are not supported by console reporting
    }

    /**
     * Performs no test-specific cleanup because the console provider
     * does not maintain test reporting resources.
     */
    @Override
    public void endTest() {
        // No cleanup required
    }
}
