package com.sele3.waits;

import org.openqa.selenium.support.ui.FluentWait;

/**
 * Generic Selenium wait implementation based on {@link FluentWait}.
 *
 * @param <T> the type of object being waited on
 */
public class SeleniumWait<T> extends FluentWait<T> {

    /**
     * Creates a Selenium wait for the specified input.
     *
     * @param input the object to wait on
     */
    public SeleniumWait(T input) {
        super(input);
    }
}
