/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.jsonpath.tokens;

import com.snaplogic.jsonpath.CompiledExpressionHolder;
import com.snaplogic.jsonpath.UnsupportedPathException;

import static com.snaplogic.jsonpath.tokens.Messages.EXPRESSION_VALUE_CANNOT_BE_NULL;

/**
 * Container for a JavaScript expression that shows up in a JSON-Path.
 * We also use this token to hold the strings and numbers that show up
 * in a subscript path component.  This token does not directly implement
 * any traversal logic, it merely holds the expression to be evaluated
 * by the other tokens.
 *
 * @author tstack
 */
public class ExpressionToken extends PathToken {
    private final String value;
    private final CompiledExpressionHolder code;

    /**
     * @param value The expression string.
     * @param code The compiled version of the expression string.
     */
    public ExpressionToken(String value, CompiledExpressionHolder code) {
        if (value == null) {
            throw new IllegalArgumentException(EXPRESSION_VALUE_CANNOT_BE_NULL);
        }
        this.value = value;
        this.code = code;
    }

    public CompiledExpressionHolder getCode() {
        return code;
    }

    @Override
    public Object resolve() throws UnsupportedPathException {
        return code;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpressionToken)) {
            return false;
        }

        final ExpressionToken that = (ExpressionToken) o;

        if (!value.equals(that.value)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return this.value;
    }
}
