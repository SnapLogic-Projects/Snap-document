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

package com.snaplogic.expression.methods.array;

import com.snaplogic.expression.methods.Method;
import sl.EvaluatorUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * Implementation of javascript array unshift method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/unshift
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2Funshift
 *
 * @author jinloes
 */
public enum Unshift implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        EvaluatorUtils.CONTEXT_THREAD_LOCAL.get().impure = true;
        List<Object> list = (List<Object>) member;
        list.addAll(0, args);
        return BigInteger.valueOf(list.size());
    }
}
