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
 * Implementation of javascript array indexOf method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/indexOf
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2FindexOf
 *
 * @author jinloes
 */
public enum IndexOf implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        List<Object> list = (List<Object>) member;
        // Javascript returns -1 if nothing can be found or on error
        int index = -1;
        if (CollectionUtils.isNotEmpty(args)) {
            int start = 0;
            int listSize = list.size();
            // Get the start index if one is provided
            if (args.size() > 1) {
                start = ((Number) args.get(1)).intValue();
            }
            if (start < 0) {
                start = 0;
            }
            if (start < listSize) {
                // IndexOf supports start index, since java does not support an index for indexOf
                // we need to take a sub list from an index until the end
                // args[0] is the value to look for
                index = list.subList(start, listSize).indexOf(args.get(0));
                // Add the start back to the index (if found),
                // since the index comes from the sub list
                if (index >= 0) {
                    index += start;
                }
            }
        }
        return BigInteger.valueOf(index);
    }
}
