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

package com.snaplogic.jsonpath;

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.tokens.TraversalToken;

import java.util.*;

/**
 * A PathVisitor that will write a value to the elements it visits.
 *
 * @author tstack
 */
public class WriteVisitor implements PathVisitor {
    private final Object scopes;
    private final Object root;
    private final boolean insert;
    private final Object value;

    /**
     * @param scopes
     * @param insert True if the value should be inserted into an
     *               array instead of overwriting an existing element.
     * @param value The value to write to the path.
     */
    public WriteVisitor(final Object scopes, Object root, boolean insert, Object value) {
        this.scopes = scopes;
        this.root = root;
        this.insert = insert;
        this.value = value;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getScopes() {
        return scopes;
    }

    @Override
    public void handleMissingField(final PathWalker walker, final Map<String, Object> parent,
                                   final String field, final TraversalToken nextToken) throws InvalidPathException {
        Object container = getContainer(walker, nextToken);
        parent.put(field, container);
    }

    @Override
    public void handleMissingElement(final PathWalker walker, final List<Object> parent,
                                     final int index, final TraversalToken nextToken) throws InvalidPathException {
        Object container = getContainer(walker, nextToken);
        while (index >= parent.size()) {
            parent.add(null);
        }
        parent.set(index, container);
    }

    @Override
    public void handleExceptionOnBranch(final PathWalker walker, final int tokenIndex,
                                        final Object obj, final InvalidPathException exc) throws InvalidPathException {
    }

    @Override
    public void visitField(final PathWalker walker, final Map<String, Object> parent,
                           final String field) throws InvalidPathException {
        parent.put(field, value);
    }

    @Override
    public void visitElement(final PathWalker walker, final List<Object> parent,
                             final int[] indexes) throws InvalidPathException {
        walker.computeListIndexForLastToken(parent, indexes, this);
        Arrays.sort(indexes);
        for (int i = indexes.length - 1; i >= 0; i--) {
            if (insert) {
                parent.add(indexes[i], value);
            } else {
                while (indexes[i] >= parent.size()) {
                    parent.add(null);
                }
                parent.set(indexes[i], value);
            }
        }
    }

    /**
     * Get a container object that can hold values that are referenced by the given token.
     *
     * @param walker The current PathWalker.
     * @param nextToken The next token in the path.
     * @return A container object (map or list) that can be used to hold values that are
     *   addressed by the nextToken.
     * @throws InvalidPathException If there was a problem with the path.
     */
    private Object getContainer(final PathWalker walker, final TraversalToken nextToken) throws
            InvalidPathException {
        Object container = null;
        try {
            // Try to traverse a null value, which should trigger the
            // token to throw an UnexpectedTypeException with the type
            // it expects.
            nextToken.traverse(walker, 0, null, this);
        } catch (UnexpectedTypeException e) {
            if (e.getExpectedType() == Map.class) {
                container = new LinkedHashMap<String, Object>();
            } else if (e.getExpectedType() == List.class) {
                List<Object> list = new ArrayList<>();
                // XXX We're currently relying on this code to populate a new list with an object.
                // I've tried to narrow it down to just when a wildcard or other branching token
                // is used as the next token.
                if (!insert && nextToken.isBranchingToken()) {
                    list.add(new LinkedHashMap<String, Object>());
                }
                container = list;
            }
        }
        return container;
    }
}
