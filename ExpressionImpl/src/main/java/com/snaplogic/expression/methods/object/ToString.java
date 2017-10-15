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

package com.snaplogic.expression.methods.object;

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;

import java.util.List;

/**
 * Implements javascript Object toString method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/
 * Object/toString
 *
 * @author tstack
 */
public enum ToString implements Method {
    INSTANCE;

    public Object evaluate(final Object member, final List args) {
        return ObjectType.toString(member);
    }
}
