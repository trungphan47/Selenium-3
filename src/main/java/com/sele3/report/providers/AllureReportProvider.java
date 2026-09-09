package com.sele3.report.providers;

import com.google.auto.service.AutoService;
import com.sele3.report.ReportProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Allure implementation of {@link ReportProvider}.
 *
 * <p>Provides reporting operations such as recording steps,
 * test results, and attachments using Allure.</p>
 *
 * <p>The test lifecycle is managed by the Allure TestNG integration,
 * so this provider does not explicitly start or end tests.</p>
 */
@AutoService(ReportProvider.class)
public class AllureReportProvider implements ReportProvider {

    /**
     * Does not explicitly start an Allure test because the test lifecycle
     * is managed by TestNG and the Allure integration.
     *
     * @param testName the name of the test
     */
    @Override
    public void startTest(String testName) {
        // Test lifecycle is managed by TestNG + Allure
    }

    /**
     * Adds a step to the current Allure test.
     *
     * @param message the step message
     */
    @Override
    public void step(String message) {
        Allure.step(message);
    }

    /**
     * Adds a passed step to the current Allure test.
     *
     * @param message the success message
     */
    @Override
    public void pass(String message) {
        Allure.step(message, Status.PASSED);
    }

    /**
     * Adds a failed step to the current Allure test.
     *
     * @param message the failure message
     */
    @Override
    public void fail(String message) {
        Allure.step(message, Status.FAILED);
    }

    /**
     * Attaches binary data to the current Allure test.
     *
     * @param name the attachment name
     * @param data the attachment data
     */
    @Override
    public void attach(String name, byte[] data) {
        // TODO: implement later
    }

    /**
     * Does not explicitly end an Allure test because the test lifecycle
     * is managed by TestNG and the Allure integration.
     */
    @Override
    public void endTest() {
        // Test lifecycle is managed by TestNG + Allure
    }
}