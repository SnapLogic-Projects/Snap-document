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
import com.snaplogic.jsonpath.PathNotFoundException;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;

import java.util.List;
import java.util.Map;

/**
 * Represents the descendant token (i.e. '..') in a JSON-Path.
 *
 * @author tstack
 */
public class DescentToken extends TraversalToken {
    public static final String DESCENT_TOKEN = "..";

    @Override
    public void traverse(PathWalker walker, int index, Object obj, PathVisitor visitor) throws
            InvalidPathException {
        if (!((obj instanceof List) || (obj instanceof Map))) {
            return;
        }

        try {
            walker.traverse(index + 1, obj, visitor);
        } catch (PathNotFoundException e) {
            visitor.handleExceptionOnBranch(walker, index, obj, e);
        }
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;

            for (Object value : map.keySet()) {
                walker.traverseObject(index - 1, map, value, visitor);
            }
        } else {
            List list = (List) obj;

            for (int arrayIndex = 0; arrayIndex < list.size(); arrayIndex++) {
                walker.traverseObject(index - 1, list, arrayIndex, visitor);
            }
        }
    }

    @Override
    public boolean isBranchingToken() {
        return true;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof DescentToken);
    }

    @Override
    public String toString() {
        return DESCENT_TOKEN;
    }
}
