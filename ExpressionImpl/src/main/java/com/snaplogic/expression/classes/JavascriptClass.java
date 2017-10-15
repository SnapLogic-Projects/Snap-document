package com.snaplogic.expression.classes;


import com.snaplogic.common.expressions.ScopeStack;

import java.util.List;

/**
 * Interface for javascript class.
 *
 * @author jinloes
 */
public interface JavascriptClass {
    /**
     * Evaluates a static method of the class.
     *
     *
     * @param scopes
     * @param methodName method name
     * @param args       arguments
     *
     * @return object
     */
    Object evalStaticMethod(ScopeStack scopes, String methodName, List<Object> args);

    /**
     * Test if an object is an instance of this JavaScript class.
     *
     * @param value The value to test.
     * @return True if the value is an instance of this JavaScript
     * type, false otherwise..
     */
    boolean evalInstanceOf(Object value);
}