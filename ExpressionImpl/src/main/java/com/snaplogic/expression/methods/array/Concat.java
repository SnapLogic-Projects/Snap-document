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

import com.google.common.collect.Lists;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Implements javascript array concat method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/concat
 * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FArray%2Fconcat
 *
 * @author jinloes
 */
public enum Concat implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        List<Object> list = (List<Object>) member;
        List<Object> result = Lists.newArrayList(list);
        if (CollectionUtils.isNotEmpty(args)) {
            for (Object arg : args) {
                ObjectType type = ObjectType.objectToType(arg);
                switch (type) {
                    case LIST:
                        result.addAll((List) arg);
                        break;
                    default:
                        result.add(arg);
                }
            }
        }
        return result;
    }
}
