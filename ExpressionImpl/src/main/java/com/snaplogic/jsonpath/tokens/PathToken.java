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

import com.snaplogic.jsonpath.UnsupportedPathException;

/**
 * Base class for tokens that show up in a JSON-Path.
 *
 * @author tstack
 */
public class PathToken {
    /**
     * @return The static value of this path.
     * @throws UnsupportedPathException If this path token is branching or cannot be statically
     *   determined.
     */
    public Object resolve() throws UnsupportedPathException {
        throw new UnsupportedPathException("Path component does not support resolving");
    }
}
