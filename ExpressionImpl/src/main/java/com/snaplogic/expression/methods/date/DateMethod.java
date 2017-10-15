/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013 - 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.expression.methods.date;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.api.Lint;
import com.snaplogic.api.Notification;
import com.snaplogic.api.Notifications;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.object.ObjectMethod;
import com.snaplogic.expression.methods.object.ToString;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.snaplogic.expression.methods.date.Messages.*;

/**
 * Wrapper class for date functions in the expression language.
 *
 * @author mklumpp
 */
public class DateMethod extends ObjectMethod {
    private static final String TIME_ZONE = "timeZone";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map> TYPE_REFERENCE = new TypeReference<Map>() {
    };

    protected static final String DATE_FORMAT = "format";
    // TODO - MK: implement the rest of the functions once we need them.
    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, Method> DATE_METHODS = ImmutableMap.<String, Method>builder()
            .put("toString", ToString.INSTANCE)
            .put("toLocaleDateString", ToLocaleDateString.INSTANCE)
            .put("toLocaleDateTimeString", ToLocaleDateTimeString.INSTANCE)
            .put("toLocaleTimeString", ToLocaleTimeString.INSTANCE)
            .put("getDate", GetDate.INSTANCE)
            .put("getDay", GetDay.INSTANCE)
            .put("getFullYear", GetFullYear.INSTANCE)
            .put("getHours", GetHours.INSTANCE)
            .put("getMilliseconds", GetMilliseconds.INSTANCE)
            .put("getMinutes", GetMinutes.INSTANCE)
            .put("getMonth", GetMonth.INSTANCE)
            .put("getSeconds", GetSeconds.INSTANCE)
            .put("getTime", GetTime.INSTANCE)

            // Since Date objects are in the UTC timezone, I'm just reusing the existing methods.
            .put("getUTCDate", GetDate.INSTANCE)
            .put("getUTCDay", GetDay.INSTANCE)
            .put("getUTCFullYear", GetFullYear.INSTANCE)
            .put("getUTCHours", GetHours.INSTANCE)
            .put("getUTCMilliseconds", GetMilliseconds.INSTANCE)
            .put("getUTCMinutes", GetMinutes.INSTANCE)
            .put("getUTCMonth", GetMonth.INSTANCE)
            .put("getUTCSeconds", GetSeconds.INSTANCE)

            .put("getTimezoneOffset", GetTimezoneOffset.INSTANCE)

            // The Date objects are immutable, so we're not going to be adding any setters.  We
            // are exposing the joda methods for doing arithmetic and replacement, though.
            .put("plus", Plus.INSTANCE)
            .put("plusDays", PlusDays.INSTANCE)
            .put("plusHours", PlusHours.INSTANCE)
            .put("plusMillis", PlusMillis.INSTANCE)
            .put("plusMinutes", PlusMinutes.INSTANCE)
            .put("plusMonths", PlusMonths.INSTANCE)
            .put("plusSeconds", PlusSeconds.INSTANCE)
            .put("plusWeeks", PlusWeeks.INSTANCE)
            .put("plusYears", PlusYears.INSTANCE)

            .put("minus", Minus.INSTANCE)
            .put("minusDays", MinusDays.INSTANCE)
            .put("minusHours", MinusHours.INSTANCE)
            .put("minusMillis", MinusMillis.INSTANCE)
            .put("minusMinutes", MinusMinutes.INSTANCE)
            .put("minusMonths", MinusMonths.INSTANCE)
            .put("minusSeconds", MinusSeconds.INSTANCE)
            .put("minusWeeks", MinusWeeks.INSTANCE)
            .put("minusYears", MinusYears.INSTANCE)

            .put("withDayOfMonth", WithDayOfMonth.INSTANCE)
            .put("withDayOfWeek", WithDayOfWeek.INSTANCE)
            .put("withDayOfYear", WithDayOfYear.INSTANCE)
            .put("withHourOfDay", WithHourOfDay.INSTANCE)
            .put("withMillisOfSecond", WithMillisOfSecond.INSTANCE)
            .put("withMinuteOfHour", WithMinuteOfHour.INSTANCE)
            .put("withMonthOfYear", WithMonthOfYear.INSTANCE)
            .put("withSecondOfMinute", WithSecondOfMinute.INSTANCE)
            .put("withYear", WithYear.INSTANCE)

            .build();

    public static Method getMethod(String methodName) {
        Method method = DATE_METHODS.get(methodName);
        if (method == null) {
            throw new UnknownMethodException(DATE_METHODS.keySet());
        }
        return method;
    }

    /**
     * Creates a date time object using a timezone if provided in the params
     *
     * @param member the object
     * @param params the params
     *
     * @return the datetime
     */
    protected static DateTime createBaseDateTime(final Object member,
            @Nullable final Map<String, Object> params) {
        if (params != null) {
            String timezone;
            timezone = (String) params.get(TIME_ZONE);
            if (!StringUtils.isBlank(timezone)) {
                TimeZone tz = TimeZone.getTimeZone(timezone);
                return ((DateTime) member).withZone(DateTimeZone
                        .forTimeZone(tz));
            }
        }
        return ((DateTime) member);
    }

    @Notification(
            message = "Date.toLocaleDateTimeString() arguments",
            reason = "Passing a string containing a JSON-object to toLocaleDateTimeString() " +
                    "can affect performance",
            resolution = "Use an object-literal to pass extra arguments to toLocaleDateTimeString()"
    )
    private static final Lint JSON_FORMAT_ARG = new Lint();

    static {
        Notifications.register(DateMethod.class);
    }

    /**
     * Parses the arguments if any exist
     *
     * @param args the arguments
     *
     * @return the arguments represented as a map
     */
    protected static Map<String, Object> parseArguments(final List<Object> args) {
        if (args != null && !args.isEmpty()) {
            Object arg0 = args.get(0);

            if (arg0 instanceof Map) {
                return (Map<String, Object>) arg0;
            }

            JSON_FORMAT_ARG.report();

            try {
                return OBJECT_MAPPER.readValue(arg0.toString(), DateMethod.TYPE_REFERENCE);
            } catch (IOException e) {
                throw new ExecutionException(ARGUMENT_PARSING_EXCEPTION)
                        .withReason(ARGUMENT_PARSING_EXCEPTION)
                        .withResolution(ARGUMENT_PARSING_RESOLUTION);
            }
        }
        return null;
    }

    /**
     * Helper method for converting the "member" object passed into the
     * method implementations into a DateTime object.
     *
     * @param member The DateTime object to cast.
     * @return The casted object.
     * @throws ExecutionException If member is not a DateTime object.
     */
    protected static DateTime convertMember(Object member) {
        if (member instanceof DateTime) {
            return (DateTime) member;
        }
        throw new ExecutionException(INVALID_MEMBER_TYPE_EXCEPTION)
                .withReason(INVALID_MEMBER_TYPE_EXCEPTION)
                .withResolution(INVALID_MEMBER_TYPE_RESOLUTION);
    }
}