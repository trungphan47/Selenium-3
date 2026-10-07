package com.sele3.waits;

import com.sele3.element.Element;

import java.time.Duration;

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
        return described(
                "Element is present in the DOM",
                Element::isPresent
        );
    }

    /**
     * Returns a condition that checks whether the element is visible.
     *
     * @return condition that is satisfied when the element is visible
     */
    public static ElementCondition visible() {
        return described(
                "Element is visible",
                element -> element.isDisplayed(Duration.ZERO)
        );
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
        return described(
                "Element is invisible or absent from the DOM",
                element -> !element.isPresent()
                        || !element.isDisplayed(Duration.ZERO)
        );
    }

    /**
     * Returns a condition that checks whether the element is enabled.
     *
     * @return condition that is satisfied when the element is enabled
     */
    public static ElementCondition enabled() {
        return described(
                "Element is enabled",
                element -> element.isEnabled(Duration.ZERO)
        );
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
        return described(
                "Element is disabled",
                element -> element.isDisabled(Duration.ZERO)
        );
    }

    /**
     * Returns a condition that checks whether the element is selected.
     *
     * @return condition that is satisfied when the element is selected
     */
    public static ElementCondition selected() {
        return described(
                "Element is selected",
                element -> element.isSelected(Duration.ZERO)
        );
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
        return described(
                "Element text contains \"" + expectedText + "\"",
                element -> element.getText(Duration.ZERO).contains(expectedText)
        );
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
        return described(
                "Element text is different from \"" + text + "\"",
                element -> !element.getText(Duration.ZERO).equals(text)
        );
    }

    /**
     * Wraps an element condition with a description for timeout messages.
     *
     * @param description expected condition description
     * @param condition   condition to evaluate
     * @return condition with the supplied description
     */
    private static ElementCondition described(
            String description,
            ElementCondition condition
    ) {
        return new ElementCondition() {

            @Override
            public boolean matches(Element element) {
                return condition.matches(element);
            }

            @Override
            public String description() {
                return description;
            }
        };
    }
}
