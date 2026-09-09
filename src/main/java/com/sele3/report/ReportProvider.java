package com.sele3.report;

/**
 * Defines the contract for report provider implementations.
 *
 * <p>Implementations are responsible for managing the reporting lifecycle,
 * including starting tests, recording steps and results, attaching data,
 * and finalizing reports.</p>
 */
public interface ReportProvider {

    /**
     * Returns the name of this report provider.
     *
     * <p>By default, the name is derived from the implementation class name
     * by removing the {@code ReportProvider} suffix and converting the
     * remaining value to lowercase.</p>
     *
     * @return the report provider name
     */
    default String getName() {
        return getClass()
                .getSimpleName()
                .replace("ReportProvider", "")
                .toLowerCase();
    }

    /**
     * Starts reporting for a test.
     *
     * @param testName the name of the test
     */
    void startTest(String testName);

    /**
     * Records a test step.
     *
     * @param message the step message
     */
    void step(String message);

    /**
     * Records a passed result.
     *
     * @param message the result message
     */
    void pass(String message);

    /**
     * Records a failed result.
     *
     * @param message the failure message
     */
    void fail(String message);

    /**
     * Attaches binary data to the report.
     *
     * @param name the attachment name
     * @param data the attachment data
     */
    void attach(String name, byte[] data);

    /**
     * Finalizes reporting for the current test.
     */
    void endTest();
}