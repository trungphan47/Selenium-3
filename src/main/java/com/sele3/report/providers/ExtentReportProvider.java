package com.sele3.report.providers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.auto.service.AutoService;
import com.sele3.report.ReportProvider;

/**
 * ExtentReports implementation of {@link ReportProvider}.
 *
 * <p>Provides reporting operations such as recording steps,
 * test results, and attachments using ExtentReports.</p>
 *
 * <p>Each test is stored in a {@link ThreadLocal} to support
 * parallel test execution.</p>
 */
@AutoService(ReportProvider.class)
public class ExtentReportProvider implements ReportProvider {

    private static final String OUTPUT_PATH = System.getProperty(
            "extent.report.path",
            "target/reports/extent-reports/index.html"
    );

    private final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

    /**
     * Holds the shared ExtentReports instance.
     *
     * <p>The instance is initialized lazily when ExtentReports
     * is actually used.</p>
     */
    private static final class ExtentHolder {

        private static final ExtentReports INSTANCE = createReport();

        private ExtentHolder() {
        }
    }

    /**
     * Returns the shared ExtentReports instance.
     *
     * @return the ExtentReports instance
     */
    private static ExtentReports extent() {
        return ExtentHolder.INSTANCE;
    }

    /**
     * Creates and configures the ExtentReports instance.
     *
     * @return the configured ExtentReports instance
     */
    private static ExtentReports createReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(OUTPUT_PATH);

        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        return extentReports;
    }

    /**
     * Creates a new Extent test for the current thread.
     *
     * @param testName the name of the test
     */
    @Override
    public void startTest(String testName) {
        currentTest.set(extent().createTest(testName));
    }

    /**
     * Adds an informational step to the current Extent test.
     *
     * @param message the step message
     */
    @Override
    public void step(String message) {
        currentTest().info(
                MarkupHelper.createLabel(
                        message,
                        ExtentColor.BLUE
                )
        );
    }

    /**
     * Marks the current Extent test step as passed.
     *
     * @param message the success message
     */
    @Override
    public void pass(String message) {
        currentTest().pass(
                MarkupHelper.createLabel(
                        message,
                        ExtentColor.GREEN
                )
        );
    }

    /**
     * Marks the current Extent test step as failed.
     *
     * @param message the failure message
     */
    @Override
    public void fail(String message) {
        currentTest().fail(
                MarkupHelper.createLabel(
                        message,
                        ExtentColor.RED
                )
        );
    }

    /**
     * Attaches binary data to the current Extent test.
     *
     * @param name the attachment name
     * @param data the attachment data
     */
    @Override
    public void attach(String name, byte[] data) {
        // TODO: implement later
    }

    /**
     * Ends reporting for the current test and removes
     * the Extent test associated with the current thread.
     */
    @Override
    public void endTest() {
        currentTest.remove();
    }

    /**
     * Finalizes the Extent report by flushing all report data
     * to the output file.
     *
     * <p>This should be called once after all tests have completed.</p>
     */
    @Override
    public void finishReport() {
        extent().flush();
    }

    /**
     * Returns the Extent test associated with the current thread.
     *
     * @return the current Extent test
     * @throws IllegalStateException if the Extent test has not been initialized
     */
    private ExtentTest currentTest() {
        ExtentTest test = currentTest.get();

        if (test == null) {
            throw new IllegalStateException(
                    "Extent test has not been initialized."
            );
        }

        return test;
    }
}
