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
 * Exception thrown when an index in a JSON array could not be found.
 *
 * @author tstack
 */
public class IndexNotFoundException extends PathNotFoundException {
    private final int index;

    public IndexNotFoundException(final int index, final String message) {
        this(index, message, null);
    }

    public IndexNotFoundException(final int index, final String message, final Throwable cause) {
        super(message, cause);
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
