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

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.Regex;
import com.snaplogic.expression.methods.Method;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implements javascript match string method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/match
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FString%2Fmatch
 *
 * @author jinloes
 */
public enum Match implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        String input = (String) member;

        if (args.isEmpty()) {
            return new ArrayList<>(Arrays.asList(""));
        }

        Object pattern = args.get(0);
        Regex regex;
        if (!(pattern instanceof Regex)) {
            regex = new Regex(null, ObjectType.toString(pattern), StringUtils.EMPTY);
        } else {
            regex = (Regex) pattern;
        }
        return regex.match(input);
    }
}
