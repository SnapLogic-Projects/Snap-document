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

import java.util.List;

/**
 * Implementation of javascript string startsWith method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String
 * /startsWith?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects
 * %2FString%2FstartsWith
 *
 * @author jinloes
 */
public enum StartsWith implements Method {
    INSTANCE;

    @Override public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        String searchStr = (String) args.get(0);
        int startIndex = 0;
        if (args.size() > 1) {
            startIndex = MethodUtils.getIndex(args.get(1));
            if (startIndex > str.length()) {
                startIndex = 0;
            }
        }
        return str.startsWith(searchStr, startIndex);
    }
}
