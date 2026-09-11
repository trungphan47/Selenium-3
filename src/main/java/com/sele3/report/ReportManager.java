package com.sele3.report;

/**
 * Provides access to the active report provider.
 */
public final class ReportManager {

    private static final ReportProvider PROVIDER = ReportFactory.create();

    private ReportManager() {
    }

    /**
     * Returns the active report provider.
     *
     * @return the active report provider
     */
    public static ReportProvider getProvider() {
        return PROVIDER;
    }
}