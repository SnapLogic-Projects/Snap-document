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

/**
 * Base exception for cases when a path could not be found.
 *
 * @author tstack
 */
public class PathNotFoundException extends InvalidPathException {
    public PathNotFoundException(final String message) {
        super(message);
    }

    public PathNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        // XXX Filling in the stack trace is expensive, especially since these
        // types of exceptions are often ignored (e.g. read with a default value).
        return this;
    }

    /**
     * Fill in the stack trace if the exception is actually going to leave the
     * JSON-Path code and be consumed by client code.
     *
     * @return this
     */
    public PathNotFoundException fillInStackTraceForCaller() {
        super.fillInStackTrace();
        return this;
    }
}
