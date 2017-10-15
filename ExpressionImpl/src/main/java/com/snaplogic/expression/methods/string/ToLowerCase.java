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

package com.snaplogic.expression.methods.string;

import com.snaplogic.expression.methods.Method;

import java.util.List;

/**
 * Implements the javascript string toLowerCase method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String
 * /toLowerCase?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects
 * %2FString%2FtoLowerCase
 *
 * @author jinloes
 */
public enum ToLowerCase implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        return str.toLowerCase();
    }
}
