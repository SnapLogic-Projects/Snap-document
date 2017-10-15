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
import com.snaplogic.jsonpath.UnexpectedTypeException;
import org.apache.commons.lang.math.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.tokens.Messages.CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_OBJECT_OR_ARRAY_VALUE;
import static com.snaplogic.jsonpath.tokens.Messages.EXPECTED_OBJ_OR_ARRAY;

/**
 * Represents the wildcard token (i.e. *) in a JSON-Path.
 *
 * @author tstack
 */
public class WildcardToken extends TraversalToken {
    public static final String WILDCARD_TOKEN = "*";

    @Override
    public void traverse(final PathWalker walker, final int index, final Object obj,
                         final PathVisitor visitor) throws InvalidPathException {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;

            if (walker.isLastToken(index)) {
                for (String key : new ArrayList<>(map.keySet())) {
                    walker.traverseMap(index, map, key, visitor);
                }
            } else {
                for (String key : new ArrayList<>(map.keySet())) {
                    try {
                        walker.traverseMap(index, map, key, visitor);
                    } catch (InvalidPathException e) {
                        visitor.handleExceptionOnBranch(walker, index, obj, e);
                    }
                }
            }
        } else if (obj instanceof List) {
            List list = (List) obj;

            if (!list.isEmpty()) {
                walker.traverseList(index, list, new IntRange(0, list.size() - 1).toArray(),
                        visitor);
            }
        } else {
            throw new UnexpectedTypeException(List.class,
                    String.format(EXPECTED_OBJ_OR_ARRAY, obj))
                    .withParent(obj)
                    .withResolution(CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_OBJECT_OR_ARRAY_VALUE);
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
        return (obj instanceof WildcardToken);
    }

    public String toString() {
        return WILDCARD_TOKEN;
    }
}
