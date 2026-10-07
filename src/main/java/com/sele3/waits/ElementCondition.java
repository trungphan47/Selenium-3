package com.sele3.waits;

import com.sele3.element.Element;

@FunctionalInterface
public interface ElementCondition {

    boolean matches(Element element);

    /**
     * Returns a description of the expected element condition
     * for timeout messages.
     *
     * <p>Custom conditions may override this method to provide
     * more specific failure details.</p>
     *
     * @return expected condition description
     */
    default String description() {
        return "Custom element condition";
    }
}