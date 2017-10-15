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

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.MethodUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * Implementation of javascripts last index of method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String
 * /lastIndexOf?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects
 * %2FString%2FlastIndexOf
 *
 * @author jinloes
 */
public enum LastIndexOf implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        String searchValue = ObjectType.toString(args.get(0));
        int index = str.length();
        if (args.size() > 1) {
            index = MethodUtils.getIndex(args.get(1), index);
        }
        return BigInteger.valueOf(str.lastIndexOf(searchValue, index));
    }
}
