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
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Implementation of javascript string trimRight method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String
 * /TrimRight?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FString
 * %2FTrimRight
 */
public enum TrimRight implements Method {
    INSTANCE;

    @Override public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        return StringUtils.stripEnd(str, null);
    }
}
