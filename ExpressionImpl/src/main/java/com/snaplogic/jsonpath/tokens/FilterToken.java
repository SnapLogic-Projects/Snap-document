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

import com.google.common.primitives.Ints;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;
import com.snaplogic.jsonpath.UnexpectedTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.tokens.Messages.CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_OBJECT_OR_ARRAY_VALUE;
import static com.snaplogic.jsonpath.tokens.Messages.EXPECTED_OBJ_OR_ARRAY_FOR_FILTER;

/**
 * Represents a filter expression (i.e. ?(...)) in a JSON-Path.
 *
 * @author tstack
 */
public class FilterToken extends TraversalToken {
    private static final String QUERY_FORMAT = "?%s";

    private ExpressionToken expr;

    /**
     * @param expr The expression used to test each element on the path.
     */
    public FilterToken(ExpressionToken expr) {
        this.expr = expr;
    }

    /**
     * Test a member of an object/array against the filter expression.
     *
     * @param walker The PathWalker used to traverse the object tree.
     * @param child The child value to test.
     * @return The result of the expression.
     * @throws InvalidPathException If there was an issue while executing the expression.
     */
    private boolean testMember(PathWalker walker, Object key, Object child, PathVisitor visitor)
            throws InvalidPathException {
        Object result = walker.evaluate(expr.getCode(), visitor, key, child);
        boolean isMember = ObjectType.toBoolean(result);

        return isMember;
    }

    @Override
    public void traverse(final PathWalker walker, final int index, final Object obj,
                         final PathVisitor visitor) throws InvalidPathException {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;

            for (String key : new ArrayList<>(map.keySet())) {
                Object value = map.get(key);
                if (testMember(walker, key, value, visitor)) {
                    walker.traverseMap(index, map, key, visitor);
                }
            }
        } else if (obj instanceof List) {
            List<Integer> elements = new ArrayList<>();
            List list = (List) obj;

            for (int i = 0; i < list.size(); i++) {
                Object child = list.get(i);

                if (testMember(walker, i, child, visitor)) {
                    elements.add(i);
                }
            }

            walker.traverseList(index, list, Ints.toArray(elements), visitor);
        } else {
            throw new UnexpectedTypeException(List.class,
                    String.format(EXPECTED_OBJ_OR_ARRAY_FOR_FILTER, this.toString(), obj))
                    .withParent(obj)
                    .withResolution(CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_OBJECT_OR_ARRAY_VALUE);
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
        if (!(o instanceof FilterToken)) {
            return false;
        }

        final FilterToken that = (FilterToken) o;

        if (!expr.equals(that.expr)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return expr.hashCode();
    }

    public String toString() {
        return String.format(QUERY_FORMAT, expr.toString());
    }
}
