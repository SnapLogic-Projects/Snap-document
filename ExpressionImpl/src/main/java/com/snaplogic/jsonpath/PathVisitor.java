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

import java.util.List;
import java.util.Map;

/**
 * A visitor interface that will be called for the final element in
 * a path.
 *
 * @author tstack
 */
public interface PathVisitor {
    /**
     * @return The root object that this path is traversing.
     */
    Object getRoot();

    /**
     * @return The variable scopes to be used when evaluating expressions.
     */
    Object getScopes();

    /**
     * Handle a request for a missing field in an intermediate object on
     * the path.  The visitor can choose to throw an exception, ignore
     * the missing data, or populate the parent with an object.
     *
     * @param walker The PathWalker that is traversing the JSON-Path.
     * @param parent The parent of the intermediate object.
     * @param field The field to visit in the parent.
     * @param nextToken The next token in the path.  This token can be used to
     *                  determine the expected type for the object stored in 'field'.
     * @throws InvalidPathException If the missing field is an issue.
     */
    void handleMissingField(PathWalker walker, Map<String, Object> parent,
                            String field, TraversalToken nextToken) throws InvalidPathException;

    /**
     * Handle a request for a missing element in an intermediate list on
     * the path.  The visitor can choose to throw an exception, ignore
     * the missing data, or populate the parent with a value.
     *
     * @param walker The PathWalker that is traversing the JSON-Path.
     * @param parent The parent of the intermediate object.
     * @param index The element to visit in the parent.
     * @param nextToken The next token in the path.  This token can be used to
     *                  determine the expected type for the object stored in 'field'.
     * @throws InvalidPathException If the missing field is an issue.
     */
    void handleMissingElement(PathWalker walker, List<Object> parent,
                              int index, TraversalToken nextToken) throws InvalidPathException;

    /**
     * Handle an exception encountered while traversing a branching token.
     *
     * @param walker The PathWalker that is traversing the JSON-Path.
     * @param tokenIndex The index of the token being processed.
     * @param obj The object the token was traversing.
     * @param exc The exception encountered during the traversal.
     * @throws InvalidPathException If the exception should stop all processing.
     */
    void handleExceptionOnBranch(PathWalker walker, int tokenIndex, Object obj,
                                 InvalidPathException exc) throws InvalidPathException;

    /**
     * Visit a field in a JSON object.
     *
     * @param walker The PathWalker that is traversing the JSON-Path.
     * @param parent The parent of the final element in the path.
     * @param field The field to visit in the parent.
     * @throws InvalidPathException If there was an issue visiting the field.
     */
    void visitField(PathWalker walker, Map<String, Object> parent,
                    String field) throws InvalidPathException;

    /**
     * Visit a list of elements in a JSON array.  This method takes an
     * array of indexes to visit since array indexes are not stable
     * between calls to a visitor (e.g. if the visitor deletes an element).
     *
     * @param walker The PathWalker that is traversing the JSON-Path.
     * @param parent The parent of the final element in the path.
     * @param indexes The indexes to visit in the parent array.
     * @throws InvalidPathException If there was an issue visiting the indexes.
     */
    void visitElement(PathWalker walker, List<Object> parent,
                      int[] indexes) throws InvalidPathException;
}
