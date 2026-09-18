package com.sele3.waits;

import com.sele3.element.Element;

/**
 * Provides reusable conditions for waiting on {@link Element} states.
 *
 * <p>These conditions are intended to be used with {@link ElementWait}
 * and are repeatedly evaluated until they are satisfied or the timeout
 * is reached.</p>
 */
public final class ElementConditions {

    private ElementConditions() {
    }

    /**
     * Returns a condition that checks whether the element is present
     * in the DOM.
     *
     * @return condition that is satisfied when the element is present
     */
    public static ElementCondition present() {
        return Element::isPresent;
    }

    /**
     * Returns a condition that checks whether the element is visible.
     *
     * @return condition that is satisfied when the element is visible
     */
    public static ElementCondition visible() {
        return Element::isDisplayed;
    }

    /**
     * Returns a condition that checks whether the element is invisible.
     *
     * <p>The condition is satisfied when the element is either not
     * displayed or no longer present in the DOM.</p>
     *
     * @return condition that is satisfied when the element is invisible
     */
    public static ElementCondition invisible() {
        return element -> !element.isDisplayed();
    }

    /**
     * Returns a condition that checks whether the element is enabled.
     *
     * @return condition that is satisfied when the element is enabled
     */
    public static ElementCondition enabled() {
        return Element::isEnabled;
    }

    /**
     * Returns a condition that checks whether the element is disabled.
     *
     * <p>Missing and stale elements are handled through the element's
     * automatic retry mechanism.</p>
     *
     * @return condition that is satisfied when the element is disabled
     */
    public static ElementCondition disabled() {
        return Element::isDisabled;
    }

    /**
     * Returns a condition that checks whether the element is selected.
     *
     * @return condition that is satisfied when the element is selected
     */
    public static ElementCondition selected() {
        return Element::isSelected;
    }

    /**
     * Returns a condition that checks whether the element text contains
     * the expected text.
     *
     * <p>Text retrieval handles missing and stale elements through the
     * element's automatic retry mechanism.</p>
     *
     * @param expectedText expected text fragment
     * @return condition that is satisfied when the element text contains
     * the expected text
     */
    public static ElementCondition textContains(String expectedText) {
        return element ->
                element.getText().contains(expectedText);
    }

    /**
     * Returns a condition that checks whether the element text is
     * different from the specified text.
     *
     * <p>Text retrieval handles missing and stale elements through the
     * element's automatic retry mechanism.</p>
     *
     * @param text text that the element should no longer equal
     * @return condition that is satisfied when the element text differs
     * from the specified text
     */
    public static ElementCondition textNotEqual(String text) {
        return element ->
                !element.getText().equals(text);
    }
}
