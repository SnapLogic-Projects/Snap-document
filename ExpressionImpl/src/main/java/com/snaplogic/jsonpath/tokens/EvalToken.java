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
 * Implements the '.eval()' json-path method.
 *
 * @author tstack
 */
public class EvalToken extends MethodToken {
    public EvalToken(final String value, final CompiledExpressionHolder code) {
        super(value, code);
    }

    @Override
    public String toString() {
        return String.format("eval(%s)", this.value);
    }

    @Override
    public Object process(PathWalker walker, PathVisitor visitor, final Object obj) throws
            InvalidPathException {
        return walker.evaluate(code, visitor, null, obj);
    }
}
