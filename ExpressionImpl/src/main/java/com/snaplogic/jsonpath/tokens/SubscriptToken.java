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

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;
import com.snaplogic.jsonpath.UnsupportedPathException;

/**
 * Represents the subscript component of a path (i.e. [...]).
 *
 * @author tstack
 */
public class SubscriptToken extends TraversalToken {
    private static final String SUBSCRIPT_FORMAT = "[%s]";
    private final PathToken child;

    public SubscriptToken(PathToken child) {
        this.child = child;
    }

    public PathToken getChild() {
        return this.child;
    }

    @Override
    public void traverse(final PathWalker walker, final int index, final Object obj,
                         final PathVisitor visitor) throws InvalidPathException {
        if (child instanceof ExpressionToken) {
            try {
                ExpressionToken expressionToken = (ExpressionToken) child;
                Object name = walker.evaluate(expressionToken.getCode(), visitor, null, obj);

                walker.traverseObject(index, obj, name, visitor);
            } catch (InvalidPathException e) {
                e.withPath(walker.subpath(index));
                throw e;
            }
        } else {
            ((TraversalToken) child).traverse(walker, index, obj, visitor);
        }
    }

    @Override
    public boolean isBranchingToken() {
        if (child instanceof TraversalToken) {
            return ((TraversalToken) child).isBranchingToken();
        }
        return false;
    }

    @Override
    public Object resolve() throws UnsupportedPathException {
        return child.resolve();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptToken)) {
            return false;
        }

        final SubscriptToken that = (SubscriptToken) o;

        if (!child.equals(that.child)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return child.hashCode();
    }

    public String toString() {
        return String.format(SUBSCRIPT_FORMAT, this.child.toString());
    }
}
