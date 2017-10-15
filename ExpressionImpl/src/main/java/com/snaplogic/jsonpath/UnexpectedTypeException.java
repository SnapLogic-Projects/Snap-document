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

/**
 * Exception thrown if a JSON-Path field or array reference is used on
 * an incompatible value in the object tree.
 *
 * @author tstack
 */
public class UnexpectedTypeException extends PathNotFoundException {
    private final Class expectedType;

    public UnexpectedTypeException(Class cl, final String message) {
        this(cl, message, null);
    }

    public UnexpectedTypeException(Class cl, final String message, final Throwable cause) {
        super(message, cause);
        expectedType = cl;
    }

    public Class getExpectedType() {
        return expectedType;
    }
}
