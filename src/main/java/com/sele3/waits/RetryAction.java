package com.sele3.waits;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.function.Supplier;

import static com.sele3.waits.WaitFactory.createWait;

public final class RetryAction {

    private RetryAction() {
    }

    /**
     * Executes the specified action with automatic retry until it succeeds
     * or the configured timeout is reached.
     *
     * @param action             action to execute
     * @param exceptionsToIgnore exceptions to ignore while retrying
     * @param <T>                action result type
     * @return action result
     */
    public static <T> T retry(Supplier<T> action, List<Class<? extends Throwable>> exceptionsToIgnore) {
        WebDriverWait wait = createWait();

        wait.ignoreAll(exceptionsToIgnore);

        return wait.until(driver -> action.get());
    }
}