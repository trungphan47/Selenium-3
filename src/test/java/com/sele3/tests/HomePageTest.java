package com.sele3.tests;

import com.sele3.assertions.Assertion;
import com.sele3.base.TestBase;
import com.sele3.driver.DriverManager;
import com.sele3.element.Element;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.time.Duration;


public class HomePageTest extends TestBase {

    private final Element headerLabel = new Element(By.xpath("//h1"));

    @Test(description = "Test")
    public void loginWithUnknownUsernameFails() {
        report.step("Open the My Account page");
        DriverManager.getDriver().get("https://the-internet.herokuapp.com/");
        Assertion.assertEquals(
                () -> headerLabel.getText(Duration.ZERO),
                "Welcome to the-internet",
                Duration.ofMillis(19),
                "Verify label is displayed correctly"
        );
    }
}
