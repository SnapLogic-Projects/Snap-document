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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.Messages.*;

/**
 * A PathVisitor that records all of the actual paths that were traversed
 * for a particular JSON-path and document.
 *
 * NOTE: This visitor must be used in conjunction with a TracingPathWalker
 * since that walker records the parent path of the values that were found.
 *
 * @author tstack
 */
public class WalkVisitor extends ReadVisitor {
    public WalkVisitor(Object scopes, Object root) {
        super(scopes, root);
    }

    @Override
    public void visitField(final PathWalker walker, final Map<String, Object> parent,
            final String field) throws InvalidPathException {
        Object value = parent.get(field);

        if (value == null && !parent.containsKey(field)) {
            throw new FieldNotFoundException(field,
                    String.format(FIELD_NOT_FOUND_IN_JSON_OBJECT, field));
        }
        addResult(walker, value);
    }

    @Override
    public void visitElement(final PathWalker walker, final List<Object> parent,
            final int[] indexes) throws InvalidPathException {
        walker.computeListIndexForLastToken(parent, indexes, this);
        for (int index : indexes) {
            try {
                addResult(walker, parent.get(index));
            } catch (IndexOutOfBoundsException e) {
                throw new IndexNotFoundException(index,
                        String.format(JSON_PATH_ARRAY_INDEX_IS_TOO_LARGE, index, parent.size()))
                        .withParent(parent)
                        .withResolution(ENSURE_THE_INDEX_IS_WITHIN_THE_ARRAY_BOUNDS);
            }
        }
    }

    /**
     * Add a result to the result set.
     *
     * @param walker The TracingPathWalker that is managing the traversal.
     * @param value The value that was found.
     */
    private void addResult(final PathWalker walker, Object value) {
        TracingPathWalker tracingPathWalker = (TracingPathWalker) walker;
        getResultSet().add(new WalkResultImpl(
                new ArrayList(tracingPathWalker.getTraversedPath()),
                value));
    }
}
