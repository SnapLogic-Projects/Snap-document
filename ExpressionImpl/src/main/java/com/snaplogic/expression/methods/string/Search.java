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

import com.snaplogic.expression.Regex;
import com.snaplogic.expression.methods.Method;
import org.apache.commons.lang.StringUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * Implementation of javascript string search function:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/search
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FString%2Fsearch
 */
public enum Search implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String input = (String) member;
        Object pattern;
        if (args.isEmpty()) {
            return BigInteger.ZERO;
        } else {
            pattern = args.get(0);
        }
        Regex regex;
        if (pattern instanceof Regex) {
            regex = (Regex) pattern;
        } else {
            regex = new Regex(null, String.valueOf(pattern), StringUtils.EMPTY);
        }

        return BigInteger.valueOf(regex.search(input));
    }
}
