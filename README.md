# Selenium Level 3 Framework

A Java Selenium framework for browser automation with reusable elements, explicit waits, soft assertions, parallel execution, and Allure, Extent, or Console reporting.

## Requirements

- JDK 21 and Maven (or IntelliJ IDEA’s Maven runner).
- Chrome, Firefox, Edge, or Safari for local execution; Selenium Grid for remote execution.

## Getting started

Run from the project root:

```bash
mvn clean test -Dplatform=chrome -Dheadless=true
```

Select a suite and override settings:

```bash
mvn clean test -DsuiteXmlFile=testng.xml -Dplatform=firefox -Dheadless=true -Dtimeout=15000 -DpollingInterval=300 -Dreport.provider=extent
```

The bundled `testng.xml` runs `ParallelExecutionTest` with three method threads.

## Configuration

Settings are loaded from `config.properties`. System properties (`-D...`) override file values. Timeouts and polling intervals are in milliseconds.

| Property | Bundled value | Purpose |
| --- | --- | --- |
| `platform` | `chrome` | Local factory: `chrome`, `firefox`, `edge`, or `safari` |
| `headless` | `false` | Headless mode where supported by the local factory |
| `remote` | `false` | Use RemoteWebDriver |
| `remoteUrl` | `http://localhost:4444` | Selenium Grid URL |
| `browserSize` | `1920,1080` | Browser size where supported |
| `startMaximized` | `true` | Maximize/start maximized where supported |
| `pageLoadStrategy` | `NORMAL` | `NORMAL`, `EAGER`, or `NONE` |
| `baseUrl` | `https://www.google.com` | URL available to test code; startup does not navigate automatically |
| `timeout` | `60000` | Default retry/wait timeout in milliseconds |
| `pollingInterval` | `500` | Retry polling interval in milliseconds |
| `clickViaJs` | `false` | Use JavaScript for `Element.click()` |
| `capabilities.browserName` | `chrome` | Browser requested for remote execution |

Reporting is selected separately with `-Dreport.provider=allure`, `extent`, or `console`. Allure is the default.

## Example test

Initialize the driver before each test and quit it afterward. You can use the repository’s example `TestBase` or the setup below.

```java
package example;

import com.sele3.assertions.Assertion;
import com.sele3.configs.ConfigLoader;
import com.sele3.driver.DriverManager;
import com.sele3.element.Element;
import com.sele3.report.ReportManager;
import com.sele3.waits.PageWait;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class HomePageTest {
    private final Element header = new Element(By.cssSelector("h1"));

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.start(ConfigLoader.load("config.properties"));
    }

    @Test
    public void verifyHomePageHeader() {
        ReportManager.getProvider().step("Open the home page");
        DriverManager.getDriver().get("https://the-internet.herokuapp.com/");
        PageWait.waitForPageLoad();

        Assertion.assertEquals(
                () -> header.getText(Duration.ZERO),
                "Welcome to the-internet",
                Duration.ofSeconds(15),
                "Verify the home page header"
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quit();
    }
}
```

Add the test class to a TestNG suite:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Smoke Suite">
    <test name="Home Page">
        <classes>
            <class name="example.HomePageTest"/>
        </classes>
    </test>
</suite>
```

TestNG automatically verifies soft assertions after each test. No manual `Assertion.start()` or `Assertion.assertAll()` calls are needed.

## Elements and waits

```java
Element username = new Element(By.id("username"));
Element loginButton = new Element(By.cssSelector("button[type='submit']"));

username.clear();
username.sendKeys("demo-user");
loginButton.click(Duration.ofSeconds(10));

// Find a child within its parent.
Element row = new Element(By.id("shipment-table")).find(By.cssSelector("tbody tr"));
row.find(By.cssSelector("button")).click();

// Wait for a specific state.
loginButton.waitUntil(ElementConditions.enabled(), Duration.ofSeconds(10));
```

Import `com.sele3.waits.ElementConditions` for wait conditions.

Available conditions: `present()`, `visible()`, `invisible()`, `enabled()`, `disabled()`, `selected()`, `textContains(text)`, and `textNotEqual(text)`.

Element actions retry temporary Selenium errors using the configured timeout. `getText()` returns the first successful read; it does not wait for expected text. `isEnabled()`, `isDisabled()`, and `isSelected()` return the current state after a successful read. Use a wait or retrying assertion when the value needs to change.

`isPresent()` checks once. `isDisplayed()` waits for visibility and returns false on timeout. Pass `Duration.ZERO` to supported methods for a single attempt.

Use `PageWait.waitForPageLoad()` to wait for `document.readyState` to become `complete`.

## Assertions

Soft assertions collect failures and allow the test to continue. With TestNG, failures are verified automatically at the end of each test.

### Immediate checks

```java
Assertion.assertTrue(result, "Verify the result");
Assertion.assertFalse(hasError, "Verify no error is present");
Assertion.assertEquals(actual, expected, "Verify the value");
```

### Checks with retry

Use a supplier (`() -> ...`) to recheck until the expected value is reached:

```java
Assertion.assertEquals(
        () -> header.getText(Duration.ZERO),
        "Welcome",
        Duration.ofSeconds(15),
        "Verify the header"
);

Assertion.assertTrue(
        () -> loginButton.isEnabled(Duration.ZERO),
        "Verify the login button becomes enabled"
);
```

Without an explicit duration, supplier overloads use the configured timeout.

**Avoid nested waits:** use `Duration.ZERO` for element reads inside retrying assertions or custom wait conditions. The outer assertion or wait controls polling.

## Reporting

| Provider | Maven option | Output |
| --- | --- | --- |
| Allure (default) | `-Dreport.provider=allure` | `target/allure-results` |
| Extent | `-Dreport.provider=extent` | `target/reports/extent-reports/index.html` |
| Console | `-Dreport.provider=console` | Execution logs |

Add report steps with `ReportManager.getProvider().step("Open the login page")`. Assertions record their results automatically.

After running tests, generate or serve the Allure report:

```bash
mvn allure:report
mvn allure:serve
```

Generated HTML is available under `target/site/allure-maven-plugin`.

## Parallel and remote execution

Enable parallel tests in your TestNG suite:

```xml
<suite name="Parallel Suite" parallel="methods" thread-count="3">
    <!-- Add your tests and classes here. -->
</suite>
```

Each test thread has its own driver and assertion state. Create and quit the driver per test method, and avoid shared mutable test data.

For Selenium Grid:

```bash
mvn clean test -Dremote=true -DremoteUrl=http://localhost:4444 -Dcapabilities.browserName=chrome
```

Remote browser options must be supplied through capabilities; local browser settings are not automatically applied to Grid.

## CI and releases

| Workflow | Trigger | Purpose |
| --- | --- | --- |
| `pr.yml` | Pull request to `main` | Build check without running tests |
| `ci.yml` | Manual run | Run tests and upload results; deploy Allure to GitHub Pages when selected |
| `publish.yml` | Published GitHub Release | Publish the framework to GitHub Packages |

To run tests, open **Actions → Sele3 CI/CD → Run workflow**, select the branch and parameters, then start the run. Reports are available from the run’s artifacts; the Allure deployment provides a Pages URL when successful.

To publish, update the version in `pom.xml`, create the corresponding tag (for example `v1.0.0`), and publish a GitHub Release. The package version comes from `pom.xml`.

## Use in another project

Install locally:

```bash
mvn clean install -DskipTests
```

Add the dependency:

```xml
<dependency>
    <groupId>com</groupId>
    <artifactId>sele3</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

For GitHub Packages, configure the Maven repository `https://maven.pkg.github.com/trungphan47/Selenium-3` and matching credentials in `settings.xml`.

Provide your own `src/test/resources/config.properties`, TestNG suite, and Maven test configuration. The example `TestBase` is not included in the published JAR. Refer to this repository’s `pom.xml` for Surefire and Allure configuration.
