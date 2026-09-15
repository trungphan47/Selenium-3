package com.sele3.report;

import java.util.ServiceLoader;

/**
 * Factory for creating {@link ReportProvider} instances.
 *
 * <p>The report provider is selected based on the
 * {@code report.provider} system property. If the property is not specified,
 * {@code allure} is used by default.</p>
 *
 * <p>Available report providers are discovered dynamically using
 * {@link ServiceLoader}.</p>
 */
public final class ReportFactory {

    private ReportFactory() {
    }

    /**
     * Creates the report provider configured by the
     * {@code report.provider} system property.
     *
     * @return the configured {@link ReportProvider}
     * @throws IllegalArgumentException if no matching report provider is found
     */
    public static ReportProvider create() {
        String providerName = System.getProperty("report.provider", "allure");

        return ServiceLoader.load(ReportProvider.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(provider ->
                        provider.getName()
                                .equalsIgnoreCase(providerName)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported report provider: "
                                        + providerName
                        )
                );
    }
}
