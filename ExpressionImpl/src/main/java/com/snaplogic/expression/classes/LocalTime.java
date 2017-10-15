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

package com.snaplogic.expression.classes;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.methods.JavascriptFunction;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.snaplogic.expression.classes.Messages.LOCALTIME_DOES_NOT_HAVE_METHOD;
import static com.snaplogic.expression.classes.Messages.PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX;

/**
 * Adds {@link org.joda.time.LocalTime} support into expression language.
 *
 * @author jinloes
 */
public enum LocalTime implements JavascriptClass, Scope {
    INSTANCE;

    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, JavascriptFunction> STRING_TO_METHOD =
            ImmutableMap.<String, JavascriptFunction>of(
                    "parse", Parse.INSTANCE
            );

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   final List<Object> args) {
        JavascriptFunction method = STRING_TO_METHOD.get(methodName);
        if (method == null) {
            throw new ExecutionException(LOCALTIME_DOES_NOT_HAVE_METHOD)
                    .formatWith(methodName)
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof org.joda.time.LocalTime;
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
     * Parses an ISO8601 time using {@link ISODateTimeFormat#localTimeParser()}.
     *
     * Specific format: HH:mm:ss.SSS
     */
    public enum Parse implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object result = null;
            Object arg = args.get(0);
            if (arg instanceof String) {
                try {
                    result = ISODateTimeFormat.localTimeParser().parseLocalTime((String) arg);
                } catch (IllegalArgumentException e) {
                    result = Double.NaN;
                }
            }
            return result;
        }
    }
}
