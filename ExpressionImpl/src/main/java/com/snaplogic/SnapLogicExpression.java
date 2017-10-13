package com.snaplogic;

import com.snaplogic.api.common.DataValueHandler;
import com.snaplogic.api.common.expressions.ScopeStack;

/**
 * Interface to a compiled expression.
 *
 * @author tstack
 */
public interface SnapLogicExpression {
    /**
     * Evaluate the expression with the given document, variable scopes, and value handler.
     *
     * @param doc The object to use as '$' in the expression.
     * @param scopes The global variables to use during evaluation.
     * @param handler The value handler.
     * @return The result of the expression.
     * @throws Throwable Anything can be thrown, prepare yourself.
     */
    Object evaluate(Object doc, ScopeStack scopes, final DataValueHandler handler) throws
            Throwable;
}