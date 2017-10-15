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
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.snaplogic.expression.classes.Messages.LOCALDATE_DOES_NOT_HAVE_METHOD;
import static com.snaplogic.expression.classes.Messages.PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX;

/**
 * Add {@link org.joda.time.LocalDate} support to expression language.
 *
 * @author jinloes, mklumpp
 */
public enum LocalDate implements JavascriptClass, Scope {
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
            throw new ExecutionException(LOCALDATE_DOES_NOT_HAVE_METHOD)
                    .formatWith(methodName)
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof org.joda.time.LocalDate;
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
     * Parses an ISO8601 date {@link ISODateTimeFormat#localDateParser()}.
     * Specific format: yyyy-MM-dd, will fallback to NO_TZ_FORMATS if non standard
     */
    public enum Parse implements JavascriptFunction {
        INSTANCE;

        public static final String[] NO_TZ_FORMATS = {
                Date.Parse.RFC_2822_SHORT_DATE_FMT,
                Date.Parse.ISO8601_DATE_FMT,
                Date.Parse.SLASH_DATE_FMT,
                Date.Parse.MERICA_DATE_FMT
        };

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
                    return ISODateTimeFormat.localDateParser().parseLocalDate(dateString);
                } catch (IllegalArgumentException e) {
                    try {
                        return new org.joda.time.DateTime(DateUtils.parseDateStrictly(dateString,
                                NO_TZ_FORMATS)).withZoneRetainFields(DateTimeZone.UTC)
                                .toLocalDate();
                    } catch (ParseException e2) {
                        return Double.NaN;
                    }
                }
            }
            return Double.NaN;
        }
    }
}
