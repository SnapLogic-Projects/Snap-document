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
import com.snaplogic.expression.methods.MethodUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * Implements javascript charCodeAt:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/
 * charCodeAt?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects
 * %2FString%2FcharCodeAt
 *
 * @author jinloes
 */
public enum CharCodeAt implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        int index = MethodUtils.getIndex(args.get(0));
        if (index > str.length()) {
            return Double.NaN;
        }
        return BigInteger.valueOf((int) str.charAt(index));
    }
}
