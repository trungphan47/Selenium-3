package com.sele3.waits;

import com.sele3.driver.DriverManager;
import com.sele3.element.Element;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

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

    private Function<? super Element, ?> currentCondition;

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

        withMessage(() ->
                (this.timeout.isZero()
                        ? "Element condition was not satisfied in a single attempt"
                        + " (timeout: 0 ms)."
                        : "Element condition was not satisfied within "
                        + this.timeout.toMillis() + " ms.")
                        + "\nCondition: " + currentCondition
                        + "\nLocator: " + input
        );
    }

    /**
     * Evaluates the condition using this wait's configured timeout.
     *
     * <p>A zero timeout evaluates the condition once without starting a
     * polling loop. Exceptions from that evaluation propagate unchanged.</p>
     *
     * @param condition condition to evaluate against the element
     * @param <V>       condition result type
     * @return the value returned when the condition is satisfied
     * @throws TimeoutException if the timeout is reached, or a zero-timeout
     *         condition returns {@code null} or {@code false}
     */
    @Override
    public <V> @NonNull V until(Function<? super Element, ? extends V> condition) {
        currentCondition = condition;

        try {
            if (timeout.isZero()) {
                V result = condition.apply(input);

                if (result == null || Boolean.FALSE.equals(result)) {
                    throw new TimeoutException(messageSupplier.get());
                }

                return result;
            }

            return super.until(condition);
        } finally {
            currentCondition = null;
        }
    }

    /**
     * Waits until the specified element condition is satisfied.
     *
     * @param condition condition to evaluate
     */
    public void until(ElementCondition condition) {
        Function<Element, Boolean> evaluation = new Function<>() {

            @Override
            public Boolean apply(Element element) {
                return condition.matches(element);
            }

            @Override
            public String toString() {
                return condition.description();
            }
        };

        until(evaluation);
    }
}
