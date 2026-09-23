package com.sele3.base;

import com.sele3.assertions.Assertion;
import com.sele3.configs.ConfigLoader;
import com.sele3.configs.Configuration;
import com.sele3.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        Assertion.start();

        Configuration configuration = ConfigLoader.load("config.properties");

        DriverManager.start(configuration);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            Assertion.assertAll("Completed running test case!");
        } finally {
            DriverManager.quit();
        }
    }
}
