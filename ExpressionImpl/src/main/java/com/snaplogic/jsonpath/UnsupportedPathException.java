/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
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
public class UnsupportedPathException extends PathNotFoundException {
    public UnsupportedPathException(final String message) {
        super(message);
    }
}
