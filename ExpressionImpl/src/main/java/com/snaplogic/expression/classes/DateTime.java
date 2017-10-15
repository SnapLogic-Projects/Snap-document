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
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.methods.JavascriptFunction;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import sl.EvaluatorUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Adds {@link org.joda.time.DateTime} into expression language
 *
 * @author jinloes, mklumpp
 */
public enum DateTime implements JavascriptClass, Scope {
    INSTANCE;

    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, JavascriptFunction> STRING_TO_METHOD =
            ImmutableMap.<String, JavascriptFunction>of(
                    "parse", Parse.INSTANCE
            );

    @Override
    public Object evalStaticMethod(ScopeStack scopes, final String methodName,
                                   final List<Object> args) {
        JavascriptFunction method = STRING_TO_METHOD.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("DateTime", STRING_TO_METHOD.keySet(),
                    methodName);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof org.joda.time.DateTime;
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
     * Parses an ISO8601 string according to {@link ISODateTimeFormat#dateTime()}.
     *
     * Specific format: yyyy-MM-dd'T'HH:mm:ss.SSSZZ. Will fall back to Date.Parse.NO_TZ_FORMATS
     * if non standard
     */
    public enum Parse implements JavascriptFunction {
        INSTANCE;
        private DateTimeFormatter ISO_FORMATTER = ISODateTimeFormat.dateTime();

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
                    return ISO_FORMATTER.parseDateTime(dateString);
                } catch (IllegalArgumentException e) {
                    try {
                        return new org.joda.time.DateTime(DateUtils.parseDateStrictly(dateString,
                                Date.Parse.NO_TZ_FORMATS))
                                .withZoneRetainFields(DateTimeZone.UTC);
                    } catch (ParseException e2) {
                        try {
                            // Try parse it with the custom formatter,
                            // the length of the string is not known due to the zone ids;
                            return Date.Parse.CUSTOM_DATE_TIME_FORMATTER.parseDateTime
                                    (dateString);
                        } catch (IllegalArgumentException e3) {
                            // ignore
                        }
                        return Double.NaN;
                    }
                }
            }
            return Double.NaN;
        }
    }
}
