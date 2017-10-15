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

import com.google.common.base.Joiner;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;
import com.snaplogic.jsonpath.UnexpectedTypeException;

import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.tokens.Messages.*;

/**
 * Container for the expressions that show up in a union subscript.
 *
 * @author tstack
 */
public class UnionToken extends TraversalToken {
    public static final String UNION_SEPARATOR = ",";
    private final List<ExpressionToken> children;

    public UnionToken(List<ExpressionToken> children) {
        this.children = children;
    }

    public List<ExpressionToken> getChildren() {
        return this.children;
    }

    @Override
    public void traverse(final PathWalker walker, final int index, final Object obj,
                         final PathVisitor visitor) throws InvalidPathException {
        String[] fieldNames = null;
        int[] arrayIndexes = null;
        for (int i = 0; i < children.size(); i++) {
            ExpressionToken expr = children.get(i);
            Object name = walker.evaluate(expr.getCode(), visitor, null, obj);

            if (name instanceof Number) {
                if (arrayIndexes == null) {
                    arrayIndexes = new int[children.size()];
                }
                arrayIndexes[i] = walker.convertListIndex(index, (Number) name);
            } else {
                if (fieldNames == null) {
                    fieldNames = new String[children.size()];
                }
                fieldNames[i] = name.toString();
            }
        }
        if (arrayIndexes != null && fieldNames != null) {
            throw new UnexpectedTypeException(Integer.class, String.format(DIFFERENT_TYPES,
                    this.toString()))
                    .withPath(walker.subpath(index))
                    .withResolution(UNION_SUBSCRIPT_VALUES_MUST_BE_THE_SAME_TYPE);
        }

        if (arrayIndexes != null) {
            if (!(obj instanceof List)) {
                throw new UnexpectedTypeException(List.class,
                        String.format(EXPECTING_ARRAY, obj))
                        .withPath(walker.subpath(index))
                        .withParent(obj)
                        .withResolution(CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_ARRAY);
            }
            walker.traverseList(index, (List) obj, arrayIndexes, visitor);
        } else if (fieldNames != null) {
            if (!(obj instanceof Map)) {
                throw new UnexpectedTypeException(Map.class,
                        String.format(EXPECTING_OBJECT, obj))
                        .withPath(walker.subpath(index))
                        .withParent(obj)
                        .withResolution(CHANCE_THE_PRECEDING_PATH_TO_REFER_TO_AN_OBJECT);
            }
            for (String field : fieldNames) {
                walker.traverseMap(index, (Map<String, Object>) obj, field, visitor);
            }
        }
    }

    @Override
    public boolean isBranchingToken() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnionToken)) {
            return false;
        }

        final UnionToken that = (UnionToken) o;

        if (!children.equals(that.children)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return children.hashCode();
    }

    public String toString() {
        return Joiner.on(UNION_SEPARATOR).join(this.children);
    }
}
