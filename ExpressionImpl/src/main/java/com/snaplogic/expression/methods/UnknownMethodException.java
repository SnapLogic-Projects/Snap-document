/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2017, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods;

import java.util.Set;

/**
 * Exception thrown by a expression language method handler when a requested method is not found.
 *
 * @author tstack
 */
public class UnknownMethodException extends RuntimeException {
    private final Set<String> methodNames;

    public UnknownMethodException(final Set<String> methodNames) {
        this.methodNames = methodNames;
    }

    public Set<String> getMethodNames() {
        return methodNames;
    }
}
