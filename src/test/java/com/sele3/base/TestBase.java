package com.sele3.base;

import com.sele3.configs.ConfigLoader;
import com.sele3.configs.Configuration;
import com.sele3.driver.DriverManager;
import com.sele3.report.ReportManager;
import com.sele3.report.ReportProvider;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {

    protected final ReportProvider report = ReportManager.getProvider();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        Configuration configuration = ConfigLoader.load("config.properties");

        DriverManager.start(configuration);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quit();
    }
}
