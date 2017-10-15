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
 * Implementation of javascript array join method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/join
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2Fjoin
 *
 * @author jinloes
 */
public enum Join implements Method {
    INSTANCE;

    private static final String DEFAULT_SEPARATOR = ",";

    @Override
    public Object evaluate(final Object member, final List args) {
        List<Object> list = (List<Object>) member;
        String separator = DEFAULT_SEPARATOR;
        if (args.size() > 0) {
            separator = args.get(0).toString();
        }
        return Joiner.on(separator).useForNull(StringUtils.EMPTY).join(list);
    }
}
