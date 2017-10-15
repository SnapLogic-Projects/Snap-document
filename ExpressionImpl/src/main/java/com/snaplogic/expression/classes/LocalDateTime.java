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
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.snaplogic.expression.classes.Messages.LOCAL_DATETIME_DOES_NOT_HAVE_METHOD;
import static com.snaplogic.expression.classes.Messages.PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX;

/**
 * Adds {@link org.joda.time.LocalDateTime} support to expression language.
 *
 * @author jinloes, mklumpp
 */
public enum LocalDateTime implements JavascriptClass, Scope {
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
            throw new ExecutionException(LOCAL_DATETIME_DOES_NOT_HAVE_METHOD)
                    .formatWith(methodName)
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof org.joda.time.LocalDateTime;
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
     * Parses a {@link org.joda.time.LocalDateTime}.
     *
     * Expected format is: yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public enum Parse implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object date = args.get(0);
            if (date == null) {
                // We want be able to deal with nulls downstream, so that we don't get a
                // Double.NaN value
                return null;
            } else if (date instanceof String) {
                String dateString = (String) date;
                try {
                    return ISODateTimeFormat.dateHourMinuteSecondMillis().parseLocalDateTime(
                            dateString);
                } catch (IllegalArgumentException e) {
                    try {
                        return new DateTime(DateUtils.parseDateStrictly(dateString,
                                Date.Parse.NO_TZ_FORMATS))
                                .withZoneRetainFields(DateTimeZone.UTC)
                                .toLocalDateTime();
                    } catch (ParseException e2) {
                        return Double.NaN;
                    }
                }
            }
            return Double.NaN;
        }
    }
}
