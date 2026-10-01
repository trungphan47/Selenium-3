package com.sele3.element;

import com.sele3.driver.DriverManager;
import com.sele3.waits.ElementCondition;
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
import java.util.function.Consumer;

/**
 * Represents a web element and provides common interaction,
 * state-checking, waiting, and nested-element operations.
 *
 * <p>Element interactions such as click, send keys, clear,
 * and value retrieval are automatically retried when transient
 * Selenium exceptions occur.</p>
 *
 * <p>Wait operations use {@link ElementCondition} so that Selenium
 * {@link WebElement} instances remain hidden from framework users.</p>
 *
 * <p>An element may also be nested inside another element. Nested
 * elements are resolved relative to their parent element rather than
 * directly from the WebDriver.</p>
 */
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

    private static final List<Class<? extends Throwable>> READ_EXCEPTIONS =
            List.of(
                    NoSuchElementException.class,
                    StaleElementReferenceException.class
            );

    /**
     * Creates a root element using the specified locator.
     *
     * @param locator locator used to find the element
     */
    public Element(By locator) {
        super(locator);
    }

    /**
     * Creates a nested element within the specified parent element.
     *
     * @param parent  parent element used as the search context
     * @param locator locator used to find the nested element
     */
    public Element(BaseElement parent, By locator) {
        super(parent, locator);
    }

    /**
     * Creates a nested element using this element as the parent.
     *
     * <pre>
     * Element row = table.find(By.cssSelector("tr"));
     * Element button = row.find(By.cssSelector("button"));
     * </pre>
     *
     * @param locator locator used to find the nested element
     * @return nested element
     */
    public Element find(By locator) {
        return new Element(this, locator);
    }

    /**
     * Clicks the element with automatic retry.
     *
     * <p>The element is located again on every retry to prevent
     * reuse of a stale element reference.</p>
     */
    @Override
    public void click() {
        click(DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Clicks the element using the specified timeout.
     *
     * <p>A zero timeout attempts the interaction once. A positive timeout
     * retries until the interaction succeeds or the timeout is reached.</p>
     *
     * @param timeout maximum time to retry the interaction
     */
    public void click(Duration timeout) {
        RetryAction.retry(
                () -> performWhenInteractable(this::performClick),
                timeout,
                DriverManager.getConfiguration().getPollingInterval(),
                CLICK_EXCEPTIONS
        );
    }

    /**
     * Sends keys to the element with automatic retry.
     *
     * <p>The element is located again on every retry to prevent
     * reuse of a stale element reference.</p>
     *
     * @param keys keys to send
     */
    @Override
    public void sendKeys(CharSequence... keys) {
        sendKeys(DriverManager.getConfiguration().getTimeout(), keys);
    }

    /**
     * Sends keys to the element using the specified timeout.
     *
     * <p>A zero timeout attempts the interaction once. A positive timeout
     * retries until the interaction succeeds or the timeout is reached.</p>
     *
     * @param timeout maximum time to retry the interaction
     * @param keys    keys to send
     */
    public void sendKeys(Duration timeout, CharSequence... keys) {
        RetryAction.retry(
                () -> performWhenInteractable(
                        element -> element.sendKeys(keys)
                ),
                timeout,
                DriverManager.getConfiguration().getPollingInterval(),
                INPUT_EXCEPTIONS
        );
    }

    /**
     * Clears the element with automatic retry.
     *
     * <p>The element is located again on every retry to prevent
     * reuse of a stale element reference.</p>
     */
    @Override
    public void clear() {
        clear(DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Clears the element using the specified timeout.
     *
     * <p>A zero timeout attempts the interaction once. A positive timeout
     * retries until the interaction succeeds or the timeout is reached.</p>
     *
     * @param timeout maximum time to retry the interaction
     */
    public void clear(Duration timeout) {
        RetryAction.retry(
                () -> performWhenInteractable(WebElement::clear),
                timeout,
                DriverManager.getConfiguration().getPollingInterval(),
                INPUT_EXCEPTIONS
        );
    }

    /**
     * Returns the visible text of the element with automatic retry.
     *
     * <p>The element is located again if it has not been rendered yet
     * or its reference becomes stale.</p>
     *
     * @return visible element text
     */
    public String getText() {
        return getText(DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Returns the visible text using the specified timeout.
     *
     * <p>A zero timeout reads once and propagates any Selenium exception.
     * A positive timeout retries temporarily missing or stale elements.
     * Any returned text, including an empty string, is successful.</p>
     *
     * @param timeout maximum time to retry the read operation
     * @return visible element text
     */
    public String getText(Duration timeout) {
        return RetryAction.retryForValue(
                () -> findElement().getText(),
                timeout,
                READ_EXCEPTIONS
        );
    }


    /**
     * Returns whether the element is currently present in the DOM.
     *
     * @return {@code true} if the element is present;
     * otherwise {@code false}
     */
    public boolean isPresent() {
        try {
            findElement();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns whether the element becomes displayed within
     * the configured timeout.
     *
     * @return {@code true} if the element becomes displayed;
     * otherwise {@code false}
     */
    public boolean isDisplayed() {
        return isDisplayedWithin(DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Returns whether the element becomes displayed within the specified timeout.
     *
     * <p>A zero timeout checks the current state once. A missing element
     * returns {@code false}; a stale reference is propagated to the caller.</p>
     *
     * @param timeout maximum time to wait
     * @return {@code true} if the element becomes displayed;
     * otherwise {@code false}
     */
    public boolean isDisplayed(Duration timeout) {
        return isDisplayedWithin(timeout);
    }

    /**
     * Returns whether the element becomes displayed within
     * the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return {@code true} if the element becomes displayed;
     * otherwise {@code false}
     */
    public boolean isDisplayedWithin(Duration timeout) {
        if (timeout.isZero()) {
            try {
                return findElement().isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        }

        try {
            RetryAction.retry(
                    () -> findElement().isDisplayed(),
                    timeout,
                    DriverManager.getConfiguration().getPollingInterval(),
                    READ_EXCEPTIONS
            );

            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Returns whether the element is enabled.
     *
     * <p>If the element has not been rendered yet or its reference
     * becomes stale, the Selenium exception is propagated so that
     * {@link ElementWait} can retry the condition.</p>
     *
     * @return {@code true} if the element is enabled;
     * otherwise {@code false}
     */
    public boolean isEnabled() {
        return isEnabled(DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Returns whether the element is enabled using the specified timeout.
     *
     * <p>A zero timeout reads the state once and propagates any Selenium
     * exception. A positive timeout retries temporarily missing or stale
     * elements. Either boolean value is a successful read.</p>
     *
     * @param timeout maximum time to retry the state read
     * @return {@code true} if the element is enabled;
     * otherwise {@code false}
     */
    public boolean isEnabled(Duration timeout) {
        return RetryAction.retryForValue(
                () -> findElement().isEnabled(),
                timeout,
                READ_EXCEPTIONS
        );
    }

    /**
     * Returns whether the element is selected.
     *
     * <p>If the element has not been rendered yet or its reference
     * becomes stale, the Selenium exception is propagated so that
     * {@link ElementWait} can retry the condition.</p>
     *
     * @return {@code true} if the element is selected;
     * otherwise {@code false}
     */
    public boolean isSelected() {
        return isSelected(DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Returns whether the element is selected using the specified timeout.
     *
     * <p>A zero timeout reads the state once and propagates any Selenium
     * exception. A positive timeout retries temporarily missing or stale
     * elements. Either boolean value is a successful read.</p>
     *
     * @param timeout maximum time to retry the state read
     * @return {@code true} if the element is selected;
     * otherwise {@code false}
     */
    public boolean isSelected(Duration timeout) {
        return RetryAction.retryForValue(
                () -> findElement().isSelected(),
                timeout,
                READ_EXCEPTIONS
        );
    }

    /**
     * Waits until the specified condition is satisfied using
     * the configured default timeout and polling interval.
     *
     * @param condition condition to evaluate
     */
    public void waitUntil(ElementCondition condition) {
        new ElementWait(this).until(condition);
    }

    /**
     * Waits until the specified condition is satisfied within
     * the specified timeout.
     *
     * @param condition condition to evaluate
     * @param timeout   maximum time to wait
     */
    public void waitUntil(
            ElementCondition condition,
            Duration timeout
    ) {
        new ElementWait(this, timeout).until(condition);
    }

    /**
     * Finds the element and performs an action when it is both
     * displayed and enabled.
     *
     * <p>The lookup is part of the retry operation. Therefore, a new
     * Selenium element reference is obtained on every attempt.</p>
     *
     * @param action action to perform on the Selenium element
     * @return {@code true} if the action was performed;
     * otherwise {@code false}
     */
    private boolean performWhenInteractable(
            Consumer<WebElement> action
    ) {
        WebElement element = findElement();

        if (!element.isDisplayed() || !element.isEnabled()) {
            return false;
        }

        action.accept(element);
        return true;
    }

    /**
     * Clicks the specified Selenium element using the configured
     * click strategy.
     *
     * @param element element to click
     */
    private void performClick(WebElement element) {
        if (DriverManager.getConfiguration().isClickViaJs()) {
            clickViaJs(element);
            return;
        }

        element.click();
    }

    /**
     * Clicks the specified Selenium element using JavaScript.
     *
     * @param element element to click
     */
    private void clickViaJs(WebElement element) {
        JavascriptExecutor javascriptExecutor =
                (JavascriptExecutor) DriverManager.getDriver();

        javascriptExecutor.executeScript(
                "arguments[0].click();",
                element
        );
    }

    /**
     * Returns whether the resolved element is disabled.
     *
     * <p>If the element is not present or becomes stale during
     * evaluation, {@code false} is returned so the surrounding wait
     * continues polling.</p>
     *
     * @return {@code true} if the element is disabled;
     * otherwise {@code false}
     */
    public boolean isDisabled() {
        return isDisabled(DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Returns whether the element is disabled using the specified timeout.
     *
     * <p>A zero timeout reads the state once and propagates any Selenium
     * exception. A positive timeout retries temporarily missing or stale
     * elements. Either boolean value is a successful read.</p>
     *
     * @param timeout maximum time to retry the state read
     * @return {@code true} if the element is disabled;
     * otherwise {@code false}
     */
    public boolean isDisabled(Duration timeout) {
        return RetryAction.retryForValue(
                () -> !findElement().isEnabled(),
                timeout,
                READ_EXCEPTIONS
        );
    }
}
