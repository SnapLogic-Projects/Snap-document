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

import com.google.common.collect.Lists;
import com.snaplogic.expression.methods.Method;

import java.util.List;

/**
 * Implements javascript array reverse method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/reverse
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2Freverse
 *
 * @author jinloes
 */
public enum Reverse implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        List<Object> list = (List<Object>) member;
        return Lists.reverse(list);
    }
}
