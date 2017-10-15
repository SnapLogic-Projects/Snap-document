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
import org.apache.commons.collections.CollectionUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * Implementation of javascript array method lastIndexOf:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array
 * /lastIndexOf?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray
 * %2FlastIndexOf
 *
 * @author jinloes
 */
public enum LastIndexOf implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        List<Object> list = (List<Object>) member;
        long index = -1;
        if (CollectionUtils.isNotEmpty(list)) {
            int listSize = list.size();
            int start = listSize;
            int numArgs = args.size();
            if (numArgs > 1) {
                start = ((Number) args.get(1)).intValue();
                // Negative indexes search from the end so calculate the end index
                // As per javascript docs the search is done right to left
                if (start < 0) {
                    start = listSize + start;
                }
                // Sublist is exclusive on the end index so add one to catch boundary conditions
                start++;
                // Start sizes greater than the list size default to the size of the list
                if (start > listSize) {
                    start = listSize;
                }
            }
            index = list.subList(0, start).lastIndexOf(args.get(0));
        }
        return BigInteger.valueOf(index);
    }
}
