package com.sele3.element;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public abstract class BaseElement {

    protected final By locator;
    protected final String name;

    /**
     * Creates an element using the specified locator.
     *
     * @param locator element locator
     */
    protected BaseElement(By locator) {
        this(locator, null);
    }

    /**
     * Creates an element using the specified locator and name.
     *
     * @param locator element locator
     * @param name element name
     */
    protected BaseElement(By locator, String name) {
        this.locator = locator;
        this.name = name;
    }

    /**
     * Clicks the element.
     */
    public abstract void click();

    /**
     * Sends keys to the element.
     *
     * @param keys keys to send
     */
    public abstract void sendKeys(CharSequence... keys);

    /**
     * Clears the element value.
     */
    public abstract void clear();
}
