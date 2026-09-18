package com.sele3.element;

import com.sele3.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Base abstraction for framework elements.
 *
 * <p>A base element may represent either a root element located directly
 * from the current WebDriver or a nested element located within another
 * {@link BaseElement}.</p>
 *
 * <p>Nested element lookup is resolved recursively through the parent
 * element, allowing multiple levels of element hierarchy without exposing
 * Selenium {@link WebElement} instances to framework users.</p>
 */
public abstract class BaseElement {

    protected final BaseElement parent;
    protected final By locator;

    /**
     * Creates a root element using the specified locator.
     *
     * @param locator locator used to find the element
     */
    protected BaseElement(By locator) {
        this(null, locator);
    }

    /**
     * Creates a nested element within the specified parent element.
     *
     * @param parent parent element used as the search context
     * @param locator locator used to find the nested element
     */
    protected BaseElement(BaseElement parent, By locator) {
        this.parent = parent;
        this.locator = locator;
    }

    /**
     * Finds the underlying Selenium element.
     *
     * <p>If this element has no parent, the lookup starts from the current
     * WebDriver. If a parent exists, the lookup starts from the parent's
     * located {@link WebElement}.</p>
     *
     * <p>This lookup strategy supports multiple levels of nested elements,
     * for example: table -&gt; row -&gt; cell -&gt; button.</p>
     *
     * @return located Selenium element
     */
    protected WebElement findElement() {
        if (parent == null) {
            return DriverManager.getDriver()
                    .findElement(locator);
        }

        return parent.findElement()
                .findElement(locator);
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
