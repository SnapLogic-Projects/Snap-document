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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.Messages.ENSURE_THE_INDEX_IS_WITHIN_THE_ARRAY_BOUNDS;
import static com.snaplogic.jsonpath.Messages.JSON_PATH_ARRAY_INDEX_IS_TOO_LARGE;

/**
 * A PathVisitor that reads values from the elements that it visits.
 *
 * @author tstack
 */
public class ReadVisitor implements PathVisitor {
    private final Object scopes;
    private final Object root;
    private final List<Object> resultSet = new ArrayList<>();

    public ReadVisitor(final Object scopes, final Object root) {
        this.scopes = scopes;
        this.root = root;
    }

    /**
     * @return The values found on the JSON-Path.
     */
    public List<Object> getResultSet() {
        return this.resultSet;
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
        throw new FieldNotFoundException(field, StringUtils.EMPTY)
                .withParent(parent);
    }

    @Override
    public void handleMissingElement(final PathWalker walker, final List<Object> parent,
            final int index, final TraversalToken nextToken) throws InvalidPathException {
        if (index >= parent.size()) {
            throw new IndexNotFoundException(index,
                    String.format(JSON_PATH_ARRAY_INDEX_IS_TOO_LARGE, index, parent.size()))
                    .withParent(parent)
                    .withResolution(ENSURE_THE_INDEX_IS_WITHIN_THE_ARRAY_BOUNDS);
        }
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
        Object value = parent.get(field);

        if (value == null && !parent.containsKey(field)) {
            throw new FieldNotFoundException(field, StringUtils.EMPTY)
                    .withParent(parent);
        }
        this.resultSet.add(value);
    }

    @Override
    public void visitElement(final PathWalker walker, final List<Object> parent,
            final int[] indexes) throws InvalidPathException {
        walker.computeListIndexForLastToken(parent, indexes, this);
        for (int index : indexes) {
            try {
                this.resultSet.add(parent.get(index));
            } catch (IndexOutOfBoundsException e) {
                throw new IndexNotFoundException(index,
                        String.format(JSON_PATH_ARRAY_INDEX_IS_TOO_LARGE, index, parent.size()))
                        .withParent(parent)
                        .withResolution(ENSURE_THE_INDEX_IS_WITHIN_THE_ARRAY_BOUNDS);
            }
        }
    }
}
