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

import com.google.common.base.Joiner;
import com.snaplogic.expression.methods.Method;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Implementation of javascript array toString method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/toString
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2FtoString
 */
public enum ToString implements Method {
    INSTANCE;

    private static final String COMMA = ",";

    @Override
    public Object evaluate(final Object member, final List args) {
        List<Object> list = (List<Object>) member;
        return Joiner.on(COMMA).useForNull(StringUtils.EMPTY).join(list);
    }
}
