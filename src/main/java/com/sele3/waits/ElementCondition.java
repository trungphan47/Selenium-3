package com.sele3.waits;

import com.sele3.element.Element;

@FunctionalInterface
public interface ElementCondition {

    boolean matches(Element element);
}