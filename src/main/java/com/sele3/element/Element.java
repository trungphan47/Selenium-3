package com.sele3.element;

import com.sele3.configs.ConfigManager;
import com.sele3.driver.DriverManager;
import com.sele3.waits.ElementWait;
import com.sele3.waits.RetryAction;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class Element extends BaseElement {

    private static final List<Class<? extends Throwable>> CLICK_EXCEPTIONS =
            List.of(
                    NoSuchElementException.class,
                    StaleElementReferenceException.class,
                    ElementClickInterceptedException.class,
                    ElementNotInteractableException.class
            );

    private static final List<Class<? extends Throwable>> INPUT_EXCEPTIONS =
            List.of(
                    NoSuchElementException.class,
                    StaleElementReferenceException.class,
                    ElementNotInteractableException.class,
                    InvalidElementStateException.class
            );

    public Element(By locator) {
        super(locator);
    }

    public Element(By locator, String name) {
        super(locator, name);
    }

    /**
     * Clicks the element with automatic wait and retry.
     * Uses JavaScript click when enabled in the configuration.
     */
    @Override
    public void click() {
        RetryAction.retry(
                () -> {
                    WebElement element =
                            DriverManager.getDriver().findElement(locator);

                    if (!element.isDisplayed() || !element.isEnabled()) {
                        return false;
                    }

                    click(element);
                    return true;
                },
                CLICK_EXCEPTIONS
        );
    }

    /**
     * Sends keys to the element with automatic wait and retry.
     *
     * @param keys keys to send
     */
    @Override
    public void sendKeys(CharSequence... keys) {
        RetryAction.retry(
                () -> {
                    WebElement element =
                            DriverManager.getDriver().findElement(locator);

                    if (!element.isDisplayed() || !element.isEnabled()) {
                        return false;
                    }

                    element.sendKeys(keys);
                    return true;
                },
                INPUT_EXCEPTIONS
        );
    }

    /**
     * Clears the element with automatic wait and retry.
     */
    @Override
    public void clear() {
        RetryAction.retry(
                () -> {
                    WebElement element =
                            DriverManager.getDriver().findElement(locator);

                    if (!element.isDisplayed() || !element.isEnabled()) {
                        return false;
                    }

                    element.clear();
                    return true;
                },
                INPUT_EXCEPTIONS
        );
    }

    /**
     * Returns the visible text of the element.
     *
     * @return element text
     */
    public String getText() {
        return ElementWait.untilVisible(locator).getText();
    }

    /**
     * Returns the value of the specified DOM attribute.
     *
     * @param attributeName attribute name
     * @return DOM attribute value
     */
    public String getDomAttribute(String attributeName) {
        return ElementWait.untilPresent(locator)
                .getDomAttribute(attributeName);
    }

    /**
     * Returns the value of the specified DOM property.
     *
     * @param propertyName property name
     * @return DOM property value
     */
    public String getDomProperty(String propertyName) {
        return ElementWait.untilPresent(locator)
                .getDomProperty(propertyName);
    }

    /**
     * Returns whether the element is displayed within the default timeout.
     *
     * @return true if the element is displayed, otherwise false
     */
    public boolean isDisplayed() {
        return isDisplayedWithin(
                ConfigManager.get().getTimeout()
        );
    }

    /**
     * Returns whether the element is displayed within the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return true if the element is displayed, otherwise false
     */
    public boolean isDisplayedWithin(Duration timeout) {
        try {
            ElementWait.untilVisible(locator, timeout);
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns whether the element is enabled.
     *
     * @return true if the element is enabled, otherwise false
     */
    public boolean isEnabled() {
        return ElementWait.untilPresent(locator)
                .isEnabled();
    }

    /**
     * Returns whether the element is selected.
     *
     * @return true if the element is selected, otherwise false
     */
    public boolean isSelected() {
        return ElementWait.untilPresent(locator)
                .isSelected();
    }

    /**
     * Waits until the element is present in the DOM.
     */
    public void waitUntilPresent() {
        ElementWait.untilPresent(locator);
    }

    /**
     * Waits until the element is present in the DOM within the specified timeout.
     *
     * @param timeout maximum time to wait
     */
    public void waitUntilPresent(Duration timeout) {
        ElementWait.untilPresent(locator, timeout);
    }

    /**
     * Waits until the element is visible.
     */
    public void waitUntilVisible() {
        ElementWait.untilVisible(locator);
    }

    /**
     * Waits until the element is visible within the specified timeout.
     *
     * @param timeout maximum time to wait
     */
    public void waitUntilVisible(Duration timeout) {
        ElementWait.untilVisible(locator, timeout);
    }

    /**
     * Waits until the element is invisible.
     */
    public void waitUntilInvisible() {
        ElementWait.untilInvisible(locator);
    }

    /**
     * Waits until the element is invisible within the specified timeout.
     *
     * @param timeout maximum time to wait
     */
    public void waitUntilInvisible(Duration timeout) {
        ElementWait.untilInvisible(locator, timeout);
    }

    /**
     * Waits until the element is enabled.
     */
    public void waitUntilEnabled() {
        ElementWait.untilEnabled(locator);
    }

    /**
     * Waits until the element is enabled within the specified timeout.
     *
     * @param timeout maximum time to wait
     */
    public void waitUntilEnabled(Duration timeout) {
        ElementWait.untilEnabled(locator, timeout);
    }

    /**
     * Waits until the element is disabled.
     */
    public void waitUntilDisabled() {
        ElementWait.untilDisabled(locator);
    }

    /**
     * Waits until the element is disabled within the specified timeout.
     *
     * @param timeout maximum time to wait
     */
    public void waitUntilDisabled(Duration timeout) {
        ElementWait.untilDisabled(locator, timeout);
    }

    /**
     * Waits until the element is selected.
     */
    public void waitUntilSelected() {
        ElementWait.untilSelected(locator);
    }

    /**
     * Waits until the element is selected within the specified timeout.
     *
     * @param timeout maximum time to wait
     */
    public void waitUntilSelected(Duration timeout) {
        ElementWait.untilSelected(locator, timeout);
    }

    /**
     * Waits until the element text contains the specified text.
     *
     * @param text expected text
     */
    public void waitUntilTextContains(String text) {
        ElementWait.untilTextContains(locator, text);
    }

    /**
     * Waits until the element text contains the specified text
     * within the specified timeout.
     *
     * @param text expected text
     * @param timeout maximum time to wait
     */
    public void waitUntilTextContains(String text, Duration timeout) {
        ElementWait.untilTextContains(locator, text, timeout);
    }

    /**
     * Waits until the element text is different from the specified text.
     *
     * @param text text that the element should no longer equal
     */
    public void waitUntilTextNotEqual(String text) {
        ElementWait.untilTextNotEqual(locator, text);
    }

    /**
     * Waits until the element text is different from the specified text
     * within the specified timeout.
     *
     * @param text text that the element should no longer equal
     * @param timeout maximum time to wait
     */
    public void waitUntilTextNotEqual(String text, Duration timeout) {
        ElementWait.untilTextNotEqual(locator, text, timeout);
    }

    /**
     * Clicks the element using the configured click strategy.
     *
     * @param element element to click
     */
    private void click(WebElement element) {
        if (ConfigManager.get().isClickViaJs()) {
            clickViaJs(element);
            return;
        }

        element.click();
    }

    /**
     * Clicks the element using JavaScript.
     *
     * @param element element to click
     */
    private void clickViaJs(WebElement element) {
        JavascriptExecutor js =
                (JavascriptExecutor) DriverManager.getDriver();

        js.executeScript(
                "arguments[0].click();",
                element
        );
    }
}
