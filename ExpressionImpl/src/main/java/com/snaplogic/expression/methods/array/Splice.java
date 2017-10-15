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
import sl.EvaluatorUtils;

import java.util.List;

/**
 * Implementation of javascript array splice method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/splice
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2Fsplice
 *
 * @author jinloes
 */
public enum Splice implements Method {
    INSTANCE;

    @Override public Object evaluate(final Object member, final List args) {
        EvaluatorUtils.CONTEXT_THREAD_LOCAL.get().impure = true;
        List<Object> list = (List<Object>) member;
        int numArgs = args.size();
        int size = list.size();
        int startIndex = 0;
        int endIndex = size;
        if (numArgs > 0) {
            startIndex = ((Number) args.get(0)).intValue();
        }
        if (startIndex < 0) {
            startIndex = size + startIndex;
        }
        if (numArgs > 1) {
            endIndex = startIndex + ((Number) args.get(1)).intValue();
        }
        if (endIndex > size) {
            endIndex = size;
        }
        List<Object> subList = list.subList(startIndex, endIndex);
        List<Object> removed = Lists.newArrayList(subList);
        subList.clear();
        if (numArgs > 2) {
            list.addAll(startIndex, args.subList(2, numArgs));
        }
        return removed;
    }
}
