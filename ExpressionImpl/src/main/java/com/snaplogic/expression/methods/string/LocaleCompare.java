/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods.string;

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;

import java.math.BigInteger;
import java.util.List;

/**
 * Implementation of javascripts last index of method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String
 * /lastIndexOf?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects
 * %2FString%2FlocaleCompare
 *
 * @author tstack
 */
public enum LocaleCompare implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String s1 = (String) member;
        String s2 = ObjectType.toString(args.get(0));
        return BigInteger.valueOf(s1.compareTo(s2));
    }
}
