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

/**
 * Parent class for components in a JSON-Path that will be
 * traversing the object tree.
 *
 * @author tstack
 */
public abstract class TraversalToken extends PathToken {
    /**
     * Traverse the given object using the behavior implemented
     * by this type of token.
     *
     * @param walker The object that is managing the traversal of the object tree.
     * @param index The current index in the JSON-Path in the walker.
     * @param obj The current object being traversed.
     * @param visitor The object that expects to handle the last element in a path.
     * @throws InvalidPathException If there was a problem traversing the tree/path.
     */
    public abstract void traverse(PathWalker walker, int index, Object obj,
                                  PathVisitor visitor) throws InvalidPathException;

    /**
     * @return True if this token will follow multiple branches in the object tree.
     */
    public boolean isBranchingToken() {
        return false;
    }
}
