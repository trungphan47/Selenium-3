package com.sele3.assertions;

import com.google.auto.service.AutoService;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;

/** Manages soft assertions around each TestNG test method. */
@AutoService(ITestNGListener.class)
public class AssertionListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            Assertion.start();
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (!method.isTestMethod()) {
            return;
        }

        try {
            Assertion.assertAll("Completed running test case!");
        } catch (AssertionError assertionError) {
            Throwable originalError = result.getThrowable();
            if (originalError == null) {
                result.setThrowable(assertionError);
            } else {
                originalError.addSuppressed(assertionError);
            }
            result.setStatus(ITestResult.FAILURE);
        }
    }
}
