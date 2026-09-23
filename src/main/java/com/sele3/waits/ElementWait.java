package com.sele3.waits;

import com.sele3.driver.DriverManager;
import com.sele3.element.Element;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;
import java.util.List;

/**
 * Provides wait functionality for {@link Element} instances.
 *
 * <p>The wait uses the configured timeout and polling interval
 * and automatically retries the supplied condition until it is
 * satisfied or the timeout is reached.</p>
 */
public class ElementWait extends SeleniumWait<Element> {

    private static final List<Class<? extends Throwable>> WAIT_EXCEPTIONS =
            List.of(
                    NoSuchElementException.class,
                    StaleElementReferenceException.class
            );

    /**
     * Creates an element wait using the configured default timeout
     * and polling interval.
     *
     * @param element the element to wait on
     */
    public ElementWait(Element element) {
        this(element, DriverManager.getConfiguration().getTimeout());
    }

    /**
     * Creates an element wait using the specified timeout
     * and configured polling interval.
     *
     * @param element the element to wait on
     * @param timeout maximum time to wait
     */
    public ElementWait(Element element, Duration timeout) {
        super(element);

        withTimeout(timeout)
                .pollingEvery(DriverManager.getConfiguration().getPollingInterval())
                .ignoreAll(WAIT_EXCEPTIONS);
    }

    /**
     * Waits until the specified element condition is satisfied.
     *
     * @param condition condition to evaluate
     */
    public void until(ElementCondition condition) {
        super.until(condition::matches);
    }
}
