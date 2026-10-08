package com.sele3.tests;

import com.sele3.assertions.Assertion;
import com.sele3.base.TestBase;
import com.sele3.driver.DriverManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelExecutionTest extends TestBase {

    private final Set<Long> threadIds = ConcurrentHashMap.newKeySet();
    private final Set<String> driverIds = ConcurrentHashMap.newKeySet();

    @BeforeClass(alwaysRun = true)
    public void clearExecutionData() {
        threadIds.clear();
        driverIds.clear();
    }

    @Test
    public void shouldRunFirstTestOnSeparateThread() {
        verifyThreadAndDriver("First test");
    }

    @Test
    public void shouldRunSecondTestOnSeparateThread() {
        verifyThreadAndDriver("Second test");
    }

    @Test
    public void shouldRunThirdTestOnSeparateThread() {
        verifyThreadAndDriver("Third test");
    }

    private void verifyThreadAndDriver(String testName) {
        long threadId = Thread.currentThread().getId();
        String driverId = ((RemoteWebDriver) DriverManager.getDriver()).getSessionId().toString();

        threadIds.add(threadId);
        driverIds.add(driverId);

        Assertion.assertTrue(DriverManager.getDriver() != null, testName + " should have its own driver");
    }

    @AfterClass(alwaysRun = true)
    public void verifyParallelExecution() {
        Assert.assertEquals(threadIds.size(), 3, "Tests should run on three separate threads");
        Assert.assertEquals(driverIds.size(), 3, "Tests should use three separate WebDriver sessions");
    }
}