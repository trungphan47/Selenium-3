package com.sele3.waits;

import com.sele3.driver.DriverManager;
import com.sele3.element.Element;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

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
                .ignoreAll(WAIT_EXCEPTIONS)
                .ignoring(RetryContext.ConditionNotSatisfiedException.class);
    }

    /**
     * Repeatedly evaluates the specified condition until it returns a value
     * other than {@code null} or {@code false}, or the timeout is reached.
     *
     * <p>If an outer polling attempt is already active on the current thread,
     * the condition is evaluated once without starting another polling loop.
     * A {@code null} or {@code false} result signals that the outer loop must
     * try again.</p>
     *
     * @param condition condition to evaluate against the element
     * @param <V>       condition result type
     * @return the value returned when the condition is satisfied
     * @throws org.openqa.selenium.TimeoutException if the timeout is reached,
     *         or a nested condition returns {@code null} or {@code false}
     */
    @Override
    public <V> V until(Function<? super Element, ? extends V> condition) {
        if (RetryContext.isActive()) {
            return RetryContext.requireSatisfied(condition.apply(input));
        }

        return super.until(value ->
                RetryContext.evaluate(() -> condition.apply(value)));
    }

    /**
     * Waits until the specified element condition is satisfied.
     *
     * @param condition condition to evaluate
     */
    public void until(ElementCondition condition) {
        Function<Element, Boolean> evaluation = condition::matches;
        until(evaluation);
    }
}
