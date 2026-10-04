package com.sele3.assertions;

import com.google.auto.service.AutoService;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;

/**
 * Connects the assertion lifecycle to TestNG test methods.
 *
 * <p>After each test method, verifies its collected soft assertions
 * and clears the assertion state associated with the current thread.</p>
 */
@AutoService(ITestNGListener.class)
public class TestNgAssertionListener implements IInvokedMethodListener, AssertionLifecycle {

    /**
     * Completes soft assertion verification after a TestNG test method.
     *
     * @param method the invoked TestNG method
     * @param result the result of the invoked method
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            afterTest();
        }
    }
}
