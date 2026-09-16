package com.sele3.waits;

import com.sele3.configs.ConfigManager;
import com.sele3.element.Element;

import java.time.Duration;

/**
 * Provides wait functionality for {@link Element} instances.
 *
 * <p>The wait uses the configured timeout and polling interval
 * and automatically retries the supplied condition until it is
 * satisfied or the timeout is reached.</p>
 */
public class ElementWait extends SeleniumWait<Element> {

    /**
     * Creates an element wait using the configured default timeout
     * and polling interval.
     *
     * @param element the element to wait on
     */
    public ElementWait(Element element) {
        super(element);

        withTimeout(ConfigManager.get().getTimeout());
        pollingEvery(ConfigManager.get().getPollingInterval());
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

        withTimeout(timeout);
        pollingEvery(ConfigManager.get().getPollingInterval());
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
