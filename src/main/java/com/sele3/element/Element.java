package com.sele3.element;

import com.sele3.configs.ConfigManager;
import com.sele3.driver.DriverManager;
import com.sele3.waits.ElementCondition;
import com.sele3.waits.ElementConditions;
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
 * <p>Element interactions such as click, send keys, and clear
 * are automatically retried when transient Selenium exceptions occur.</p>
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

    /**
     * Creates a root element using the specified locator.
     *
     * <p>The element will be located directly from the current WebDriver.</p>
     *
     * @param locator locator used to find the element
     */
    public Element(By locator) {
        super(locator);
    }

    /**
     * Creates a nested element within the specified parent element.
     *
     * <p>The element will be located relative to the parent element
     * rather than directly from the WebDriver.</p>
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
     * <p>This allows element hierarchies to be built fluently, for example:</p>
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
     * <p>The action is retried until the element becomes interactable
     * or the configured timeout is reached.</p>
     *
     * <p>JavaScript click is used when enabled in the configuration.</p>
     */
    @Override
    public void click() {
        RetryAction.retry(
                () -> performWhenInteractable(this::click),
                CLICK_EXCEPTIONS
        );
    }

    /**
     * Sends keys to the element with automatic retry.
     *
     * <p>The action is retried until the element becomes interactable
     * or the configured timeout is reached.</p>
     *
     * @param keys keys to send
     */
    @Override
    public void sendKeys(CharSequence... keys) {
        RetryAction.retry(
                () -> performWhenInteractable(
                        element -> element.sendKeys(keys)
                ),
                INPUT_EXCEPTIONS
        );
    }

    /**
     * Clears the element with automatic retry.
     *
     * <p>The action is retried until the element becomes interactable
     * or the configured timeout is reached.</p>
     */
    @Override
    public void clear() {
        RetryAction.retry(
                () -> performWhenInteractable(WebElement::clear),
                INPUT_EXCEPTIONS
        );
    }

    /**
     * Returns the current visible text of the element.
     *
     * <p>This method performs an immediate lookup and does not wait
     * for the element to become visible.</p>
     *
     * @return current element text
     */
    public String getText() {
        return findElement().getText();
    }

    /**
     * Returns the current value of the specified DOM property.
     *
     * @param propertyName property name
     * @return DOM property value
     */
    public String getDomProperty(String propertyName) {
        return findElement().getDomProperty(propertyName);
    }

    /**
     * Returns whether the element is currently present in the DOM.
     *
     * @return {@code true} if the element is present,
     * otherwise {@code false}
     */
    public boolean isPresent() {
        try {
            findElement();
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Returns whether the element is currently displayed.
     *
     * @return {@code true} if the element is displayed,
     * otherwise {@code false}
     */
    public boolean isDisplayed() {
        try {
            return findElement().isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Returns whether the element becomes displayed within
     * the specified timeout.
     *
     * @param timeout maximum time to wait
     * @return {@code true} if the element becomes displayed,
     * otherwise {@code false}
     */
    public boolean isDisplayedWithin(Duration timeout) {
        try {
            waitUntil(ElementConditions.visible(), timeout);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Returns whether the element is currently enabled.
     *
     * @return {@code true} if the element is enabled,
     * otherwise {@code false}
     */
    public boolean isEnabled() {
        try {
            return findElement().isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Returns whether the element is currently selected.
     *
     * @return {@code true} if the element is selected,
     * otherwise {@code false}
     */
    public boolean isSelected() {
        try {
            return findElement().isSelected();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Waits until the specified condition is satisfied using
     * the configured default timeout and polling interval.
     *
     * <p>The condition is automatically retried until it is satisfied
     * or the configured timeout is reached.</p>
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
     * <p>The condition is automatically retried using the configured
     * polling interval until it is satisfied or the timeout is reached.</p>
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
     * Performs the specified action when the element is interactable.
     *
     * <p>An element is considered interactable when it is both displayed
     * and enabled. Returning {@code false} allows the retry mechanism
     * to attempt the action again.</p>
     *
     * @param action action to perform on the underlying Selenium element
     * @return {@code true} if the action was performed,
     * otherwise {@code false}
     */
    private boolean performWhenInteractable(Consumer<WebElement> action) {
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
    private void click(WebElement element) {
        if (ConfigManager.get().isClickViaJs()) {
            clickViaJs(element);
            return;
        }

        element.click();
    }

    /**
     * Clicks the specified element using JavaScript.
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
