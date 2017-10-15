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
 * Implementation of javascript string slice method.
 *
 * @author jinloes
 */
public enum Slice implements Method {
    INSTANCE;

    @Override public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        int startIdx = MethodUtils.getIndex(args.get(0));
        int endIdx = str.length();
        if (args.size() > 1) {
            endIdx = MethodUtils.getIndex(args.get(1));
        }
        return StringUtils.substring(str, startIdx, endIdx);

    }
}
