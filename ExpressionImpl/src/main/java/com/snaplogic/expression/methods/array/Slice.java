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

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of javascript array slice method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/slice
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2Fslice
 *
 * @author jinloes
 */
public enum Slice implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        List<Object> list = (List<Object>) member;
        int start = 0;
        int end = list.size();
        int numArgs = args.size();
        if (numArgs > 0) {
            start = ((Number) args.get(0)).intValue();
        }
        if (start < 0) {
            start = end + start;
        }
        if (numArgs > 1) {
            end = ((Number) args.get(1)).intValue();
        }
        if (end < 0) {
            end = list.size() + end;
        }

        return new ArrayList<>(list.subList(start, end));
    }
}
