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

package com.snaplogic.jsonpath;

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.tokens.NameToken;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * A PathWalker that records the actual path taken while traversing a document.
 * The actual path may differ from the original JSON-path when there are
 * branching tokens in the path, like a descent or union.  For example, the
 * path "$..foo" will descend into all objects and lists in the document.
 * This walker will keep track of the current path that is taken before the
 * PathVisitor is called.
 *
 * @author tstack
 */
public class TracingPathWalker extends PathWalker {
    private final Stack<Pair<Object, Object>> traversedPath = new Stack<>();

    public TracingPathWalker(PathWalker original) {
        super(original);
    }

    /**
     * @return A list of pairs containing an object and the field-name/array-index
     *   that was traversed while walking a JSON-path.
     */
    public Stack<Pair<Object, Object>> getTraversedPath() {
        return this.traversedPath;
    }

    @Override
    public void traverseWithWritableRoot(final Object root, final PathVisitor visitor) throws
            InvalidPathException {
        if (simplePath != null) {
            traversedPath.push(Pair.<Object, Object>of(new RootMap(root), NameToken.ROOT_ELEMENT));
        }
        super.traverseWithWritableRoot(root, visitor);
        if (simplePath != null) {
            traversedPath.pop();
        }
    }

    @Override
    public void traverseList(final int index, final List<Object> list, final int[] elements,
            final PathVisitor visitor) throws InvalidPathException {
        for (int elementIndex = 0; elementIndex < elements.length; elementIndex++) {
            try {
                this.traversedPath.push(Pair.<Object, Object>of(list, elements[elementIndex]));
                super.traverseList(index, list, new int[] { elements[elementIndex] }, visitor);
            } finally {
                this.traversedPath.pop();
            }
        }
    }

    @Override
    public void traverseMap(final int index, final Map<String, Object> map, final String field,
            final PathVisitor visitor) throws InvalidPathException {
        try {
            this.traversedPath.push(Pair.<Object, Object>of(map, field));
            super.traverseMap(index, map, field, visitor);
        } finally {
            this.traversedPath.pop();
        }
    }
}
