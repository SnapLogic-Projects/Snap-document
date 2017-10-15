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

package com.snaplogic.expression.classes;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.JavascriptFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sl.EvaluatorUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the String type in JavaScript
 *
 * @author tstack
 */
public enum JavascriptString implements JavascriptClass, Scope {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(JavascriptString.class);
    private static final Map<String, JavascriptFunction> STRING_TO_METHOD =
            new ImmutableMap.Builder<String, JavascriptFunction>()
                    .put("fromCharCode", FromCharCode.INSTANCE)
                    .build();

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   final List<Object> args) {
        JavascriptFunction method = STRING_TO_METHOD.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("String", STRING_TO_METHOD.keySet(), methodName);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof String;
    }

    @Override
    public Object get(String name) {
        Object retval = STRING_TO_METHOD.get(name);
        if (retval == null) {
            retval = UNDEFINED;
        }
        return retval;
    }

    @Override
    public Set<String> keySet() {
        return STRING_TO_METHOD.keySet();
    }

    /**
     * Mimics javascript String.fromCharCode method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/
     */
    public enum FromCharCode implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            StringBuilder stringBuilder = new StringBuilder();

            for (Object arg: args) {
                Object val = ObjectType.attemptToConvertToBigDecimal(arg);

                if (val instanceof Double || val instanceof Float) {
                    continue;
                }
                if (val instanceof Number) {
                    Number num = (Number) val;

                    try {
                        stringBuilder.append(Character.toChars(num.intValue()));
                    } catch (IllegalArgumentException e) {
                        LOG.warn("Cannot convert invalid codepoint: {}", arg, e);
                    }
                }
            }

            return stringBuilder.toString();
        }
    }
}
