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
import com.snaplogic.jsonpath.tokens.DescentToken;
import com.snaplogic.jsonpath.tokens.TraversalToken;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.Messages.*;

/**
 * A path visitor that deletes the elements it visits.
 *
 * @author tstack
 */
public class DeleteVisitor implements PathVisitor {
    private final Object scopes;
    private final Object root;

    public DeleteVisitor(final Object scopes, final Object root) {
        this.scopes = scopes;
        this.root = root;
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

    }

    @Override
    public void handleMissingElement(final PathWalker walker, final List<Object> parent,
                                     final int index, final TraversalToken nextToken) throws InvalidPathException {
    }

    @Override
    public void handleExceptionOnBranch(final PathWalker walker, final int tokenIndex,
                                        final Object obj, final InvalidPathException exc) throws InvalidPathException {
        if (!(walker.getTokens().get(tokenIndex) instanceof DescentToken)) {
            throw exc;
        }
    }

    @Override
    public void visitField(final PathWalker walker, final Map<String, Object> parent,
                           final String field) throws InvalidPathException {
        if (!parent.containsKey(field)) {
            throw new FieldNotFoundException(field, String.format(FIELD_TO_DELETE_DOES_NOT_EXIST,
                    field));
        }
        parent.remove(field);
    }

    @Override
    public void visitElement(final PathWalker walker, final List<Object> parent,
                             final int[] indexes) throws InvalidPathException {
        int lastIndex = -1;

        walker.computeListIndexForLastToken(parent, indexes, this);
        // Sort the array indexes and delete in reverse so that the indexes
        // stay sane.
        Arrays.sort(indexes);
        for (int i = indexes.length - 1; i >= 0; i--) {
            if (indexes[i] == lastIndex) {
                // Ignore duplicate indexes.
                continue;
            }
            try {
                parent.remove(indexes[i]);
            } catch (IndexOutOfBoundsException e) {
                throw new IndexNotFoundException(indexes[i],
                        String.format(JSON_PATH_ARRAY_INDEX_IS_TOO_LARGE, indexes[i],
                                parent.size()))
                        .withParent(parent)
                        .withResolution(ENSURE_THE_INDEX_IS_WITHIN_THE_ARRAY_BOUNDS);
            }
            lastIndex = indexes[i];
        }
    }
}
