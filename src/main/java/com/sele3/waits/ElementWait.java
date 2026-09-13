package com.sele3.waits;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static com.sele3.waits.WaitFactory.createWait;

public final class ElementWait {

    private ElementWait() {
    }

    /**
     * Waits until the element is present in the DOM.
     *
     * @param locator element locator
     * @return the located element
     */
    public static WebElement untilPresent(By locator) {
        return createWait().until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
    }

    /**
     * Waits until the element is present in the DOM within the specified timeout.
     *
     * @param locator element locator
     * @param timeout maximum time to wait
     * @return the located element
     */
    public static WebElement untilPresent(By locator, Duration timeout) {
        return createWait(timeout).until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
    }

    /**
     * Waits until the element is visible.
     *
     * @param locator element locator
     * @return the visible element
     */
    public static WebElement untilVisible(By locator) {
        return createWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    /**
     * Waits until the element is visible within the specified timeout.
     *
     * @param locator element locator
     * @param timeout maximum time to wait
     * @return the visible element
     */
    public static WebElement untilVisible(By locator, Duration timeout) {
        return createWait(timeout).until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    /**
     * Waits until the element is clickable.
     *
     * @param locator element locator
     * @return the clickable element
     */
    public static WebElement untilClickable(By locator) {
        return createWait().until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    /**
     * Waits until the element is invisible.
     *
     * @param locator element locator
     */
    public static void untilInvisible(By locator) {
        createWait().until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    /**
     * Waits until the element is invisible within the specified timeout.
     *
     * @param locator element locator
     * @param timeout maximum time to wait
     */
    public static void untilInvisible(By locator, Duration timeout) {
        createWait(timeout).until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    /**
     * Waits until the element is enabled.
     *
     * @param locator element locator
     * @return the enabled element
     */
    public static WebElement untilEnabled(By locator) {
        return createWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.isEnabled() ? element : null;
        });
    }

    /**
     * Waits until the element is enabled within the specified timeout.
     *
     * @param locator element locator
     * @param timeout maximum time to wait
     * @return the enabled element
     */
    public static WebElement untilEnabled(By locator, Duration timeout) {
        return createWait(timeout).until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.isEnabled() ? element : null;
        });
    }

    /**
     * Waits until the element is disabled.
     *
     * @param locator element locator
     * @return the disabled element
     */
    public static WebElement untilDisabled(By locator) {
        return createWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return !element.isEnabled() ? element : null;
        });
    }

    /**
     * Waits until the element is disabled within the specified timeout.
     *
     * @param locator element locator
     * @param timeout maximum time to wait
     * @return the disabled element
     */
    public static WebElement untilDisabled(By locator, Duration timeout) {
        return createWait(timeout).until(driver -> {
            WebElement element = driver.findElement(locator);
            return !element.isEnabled() ? element : null;
        });
    }

    /**
     * Waits until the element is selected.
     *
     * @param locator element locator
     * @return the selected element
     */
    public static WebElement untilSelected(By locator) {
        return createWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.isSelected() ? element : null;
        });
    }

    /**
     * Waits until the element is selected within the specified timeout.
     *
     * @param locator element locator
     * @param timeout maximum time to wait
     * @return the selected element
     */
    public static WebElement untilSelected(By locator, Duration timeout) {
        return createWait(timeout).until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.isSelected() ? element : null;
        });
    }

    /**
     * Waits until the element text contains the specified text.
     *
     * @param locator element locator
     * @param text expected text
     */
    public static void untilTextContains(By locator, String text) {
        createWait().until(
                ExpectedConditions.textToBePresentInElementLocated(
                        locator,
                        text
                )
        );
    }

    /**
     * Waits until the element text contains the specified text
     * within the specified timeout.
     *
     * @param locator element locator
     * @param text expected text
     * @param timeout maximum time to wait
     */
    public static void untilTextContains(By locator, String text, Duration timeout) {
        createWait(timeout).until(
                ExpectedConditions.textToBePresentInElementLocated(
                        locator,
                        text
                )
        );
    }

    /**
     * Waits until the element text is different from the specified text.
     *
     * @param locator element locator
     * @param text text that the element should no longer equal
     */
    public static void untilTextNotEqual(By locator, String text) {
        createWait().until(driver ->
                !driver.findElement(locator)
                        .getText()
                        .equals(text)
        );
    }

    /**
     * Waits until the element text is different from the specified text
     * within the specified timeout.
     *
     * @param locator element locator
     * @param text text that the element should no longer equal
     * @param timeout maximum time to wait
     */
    public static void untilTextNotEqual(By locator, String text, Duration timeout) {
        createWait(timeout).until(driver ->
                !driver.findElement(locator)
                        .getText()
                        .equals(text)
        );
    }
}
