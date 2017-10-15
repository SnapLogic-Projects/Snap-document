/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.classes;

import com.snaplogic.common.expressions.ScopeStack;

import java.util.List;

/**
 * Represents the Number type in JavaScript
 *
 * @author tstack
 */
public enum JavascriptNumber implements JavascriptClass {
    INSTANCE;

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   final List<Object> args) {
        return null;
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof Number;
    }
}
