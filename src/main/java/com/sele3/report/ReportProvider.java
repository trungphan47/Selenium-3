package com.sele3.report;

/**
 * Defines the contract for report provider implementations.
 *
 * <p>Implementations are responsible for managing the reporting lifecycle,
 * including starting tests, recording steps and results, attaching data,
 * and finalizing reports.</p>
 */
public interface ReportProvider {

    default String getName() {
        return getClass()
                .getSimpleName()
                .replace("ReportProvider", "")
                .toLowerCase();
    }

    void startTest(String testName);

    void step(String message);

    void pass(String message);

    void fail(String message);

    void attach(String name, byte[] data);

    /**
     * Finalizes reporting for the current test.
     */
    void endTest();

    /**
     * Finalizes the report after all tests have completed.
     *
     * <p>Providers that do not require explicit report finalization
     * may use the default no-op implementation.</p>
     */
    default void finishReport() {
        // No-op by default
    }
}
