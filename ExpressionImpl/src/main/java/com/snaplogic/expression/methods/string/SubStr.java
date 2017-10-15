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
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Implementation of javascript string substr method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/substr
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FString%2Fsubstr
 *
 * @author jinloes
 */
public enum SubStr implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        int strLen = str.length();
        int startIdx = MethodUtils.getIndex(args.get(0));
        int size = strLen;
        if (startIdx < 0 && Math.abs(startIdx) > strLen) {
            startIdx = 0;
        }
        if (args.size() > 1) {
            size = MethodUtils.getIndex(args.get(1), Integer.MAX_VALUE);
            if (size <= 0) {
                return StringUtils.EMPTY;
            }
        }
        return StringUtils.substring(str, startIdx, startIdx + size);
    }
}
