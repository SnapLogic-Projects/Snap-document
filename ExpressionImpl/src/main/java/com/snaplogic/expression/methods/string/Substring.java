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
 * Implements javascript string substring method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String
 * /substring?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects
 * %2FString%2Fsubstring
 *
 * @author jinloes
 */
public enum Substring implements Method {
    INSTANCE;

    @Override public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        int strLen = str.length();
        int startIdx = MethodUtils.getIndex(args.get(0));
        if (startIdx > strLen) {
            startIdx = strLen;
        } else if (startIdx < 0) {
            startIdx = 0;
        }
        int endIdx = strLen;
        if (args.size() > 1) {
            endIdx = MethodUtils.getIndex(args.get(1));
            if (endIdx > strLen) {
                endIdx = strLen;
            } else if (endIdx < 0) {
                endIdx = 0;
            }
        }
        if (startIdx > endIdx) {
            startIdx = startIdx ^ endIdx;
            endIdx = startIdx ^ endIdx;
            startIdx = startIdx ^ endIdx;
        }
        return StringUtils.substring(str, startIdx, endIdx);
    }
}
