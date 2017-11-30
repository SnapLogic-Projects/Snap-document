/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.common.expressions.DataValueHandler;
import com.snaplogic.common.expressions.ScopeStack;

import java.io.Serializable;

/**
 * Interface to a compiled expression.
 *
 * @author tstack
 */
public interface SnapLogicExpression extends Serializable{
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
