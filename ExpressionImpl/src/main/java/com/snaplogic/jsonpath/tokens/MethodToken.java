/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.jsonpath.tokens;

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.CompiledExpressionHolder;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;

/**
 * Base-class for json-path methods.
 *
 * @author tstack
 */
public abstract class MethodToken extends PathToken {
    protected final String value;
    protected final CompiledExpressionHolder code;

    public MethodToken(final String value, final CompiledExpressionHolder code) {
        this.value = value;
        this.code = code;
    }

    public abstract Object process(PathWalker walker, PathVisitor visitor, Object obj) throws
            InvalidPathException;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final MethodToken that = (MethodToken) o;

        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
