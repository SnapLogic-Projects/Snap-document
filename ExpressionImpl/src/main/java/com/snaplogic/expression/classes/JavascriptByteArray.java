/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
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
 * Represents the byte array type in JavaScript
 *
 * @author tstack
 */
public enum JavascriptByteArray implements JavascriptClass, Scope {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(JavascriptByteArray.class);
    private static final Map<String, JavascriptFunction> STRING_TO_METHOD =
            new ImmutableMap.Builder<String, JavascriptFunction>()
                    .put("of", Of.INSTANCE)
                    .build();

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   final List<Object> args) {
        JavascriptFunction method = STRING_TO_METHOD.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("Uint8Array", STRING_TO_METHOD.keySet(),
                    methodName);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof byte[];
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
     * Implements the Uint8Array.of() function
     */
    public enum Of implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            byte[] retval = new byte[args.size()];
            int index = 0;

            for (Object obj : args) {
                Number num = ObjectType.toNumber(obj, 0);

                retval[index] = num.byteValue();
                index += 1;
            }
            return retval;
        }
    }
}
