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

import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.ScopeStack;

import java.util.List;
import java.util.Map;

import static com.snaplogic.expression.classes.Messages.NO_SUPPORTED_METHODS_YET;
import static com.snaplogic.expression.classes.Messages.OBJECT_DOES_NOT_HAVE_METHOD;

/**
 *.Represents the Object type in JavaScript
 *
 * @author tstack
 */
public enum JavascriptObject implements JavascriptClass {
    INSTANCE;

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   final List<Object> args) {
        throw new ExecutionException(OBJECT_DOES_NOT_HAVE_METHOD).formatWith(methodName)
                .withReason(String.format(OBJECT_DOES_NOT_HAVE_METHOD, methodName))
                .withResolution(String.format(NO_SUPPORTED_METHODS_YET));
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof Map;
    }
}
