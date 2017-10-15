/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods.number;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.object.HasOwnProperty;
import com.snaplogic.expression.methods.object.ObjectMethod;

import java.util.Map;

/**
 * Wrapper class for number functions in the expression language.
 *
 * @author tstack
 */
public class NumberMethod extends ObjectMethod {
    private static final String METHOD_SEPARATOR = ",";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, Method> NUMBER_METHODS = ImmutableMap.<String, Method>builder()
            .put("hasOwnProperty", HasOwnProperty.INSTANCE)
            .put("toExponential", ToExponential.INSTANCE)
            .put("toFixed", ToFixed.INSTANCE)
            .put("toPrecision", ToPrecision.INSTANCE)
            .put("toString", ToString.INSTANCE)
                    // TODO tstack - add toLocaleString (?)
            .build();

    public static Method getMethod(String methodName) {
        Method method = NUMBER_METHODS.get(methodName);
        if (method == null) {
            throw new UnknownMethodException(NUMBER_METHODS.keySet());
        }
        return method;
    }
}
