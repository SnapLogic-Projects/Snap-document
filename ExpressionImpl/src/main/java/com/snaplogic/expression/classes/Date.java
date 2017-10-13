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

import com.snaplogic.api.common.expressions.Scope;
import com.snaplogic.api.FeatureFlag;
import com.snaplogic.api.common.expressions.ScopeStack;
import com.snaplogic.expression.methods.JavascriptFunction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.snaplogic.expression.classes.Messages.UNABLE_TO_PARSE_DATE;

/**
 * Mimics javascript date class.
 *
 * @author jinloes
 */
public enum Date implements JavascriptClass, Scope {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(Date.class);
    @FeatureFlag(description = "Use the Joda parser for dates")
    private static volatile boolean USE_JODA = true;

    private static class SLDateFormatter {

        private final SimpleDateFormat javaDateFormatter;
        private final DateTimeFormatter jodaDateFormatter;
        private final boolean hasTZ;

        public SLDateFormatter(String pattern, boolean hasTZ) {
            if (Date.Parse.canUseJoda(pattern)) {
                jodaDateFormatter = DateTimeFormat.forPattern(pattern);
            } else {
                jodaDateFormatter = null;
            }
            javaDateFormatter = new SimpleDateFormat(pattern);
            javaDateFormatter.setLenient(false);
            this.hasTZ = hasTZ;
        }

        public DateTime parse(String dateString, ParsePosition pos) {
            DateTime retval;
            if (jodaDateFormatter != null && USE_JODA) {
                retval = parseWithJoda(dateString);
            } else {
                retval = parseWithJava(dateString, pos);
            }

            if (retval != null && hasTZ) {
                retval = retval.withZone(DateTimeZone.UTC);
            } else if (retval != null) {
                retval = retval.withZoneRetainFields(DateTimeZone.UTC);
            }

            return retval;
        }

        private DateTime parseWithJoda(String dateString) {
            try {
                return jodaDateFormatter.parseDateTime(dateString);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        private DateTime parseWithJava(String dateString, ParsePosition pos) {
            java.util.Date date = javaDateFormatter.parse(dateString, pos);
            if (date != null && pos.getIndex() == dateString.length()) {
                return new DateTime(date);
            } else {
                return null;
            }
        }
    }

    /**
     * A thread-local that is used to store compiled SimpleDateFormat objects and the order that
     * they should be checked when trying to parse a date string.
     */
    private static class PreferredDateOrdering extends ThreadLocal<List<SLDateFormatter>> {
        private static void compileFormats(List<SLDateFormatter> accum, String[]
                parsePatterns, boolean hasTZ) {
            for (String pattern : parsePatterns) {
                accum.add(new SLDateFormatter(pattern, hasTZ));
            }
        }

        @Override
        protected List<SLDateFormatter> initialValue() {
            List<SLDateFormatter> retval = new ArrayList<>();
            compileFormats(retval, Parse.WITH_TZ_FORMATS, true);
            compileFormats(retval, Parse.NO_TZ_FORMATS, false);
            return retval;
        }

        /**
         * Attempt to parse the given string as a date.
         *
         * @param str The date string to parse
         * @return A joda DateTime object or a Double.NaN if the date parsed, but was invalid.
         * @throws ParseException If the date could not be parsed.
         */
        public Object parse(String str) throws ParseException {
            ParsePosition pos = new ParsePosition(0);
            List<SLDateFormatter> parsers = get();

            for (int i = parsers.size() - 1; i >= 0; i--) {
                SLDateFormatter parser = parsers.get(i);
                pos.setIndex(0);
                DateTime retval = parser.parse(str, pos);
                if (retval != null) {
                    if (i < (parsers.size() - 1)) {
                        // If this format is not the first one we checked, move it to the end of
                        // the list.
                        parsers.remove(i);
                        parsers.add(parser);
                    }
                    return verifyValidDateTime(retval);
                }
            }
            throw new ParseException(UNABLE_TO_PARSE_DATE, 0);
        }
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, JavascriptFunction> STRING_TO_METHOD =
            new ImmutableMap.Builder<String, JavascriptFunction>()
                    .put("parse", Parse.INSTANCE)
                    .put("now", Now.INSTANCE)
                    .put("UTC", Utc.INSTANCE)
                    .build();


    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   List<Object> args) {
        JavascriptFunction method = STRING_TO_METHOD.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("Date", STRING_TO_METHOD.keySet(), methodName);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof DateTime;
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
     * Mimics javascript Date.parse method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/parse
     * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FDate%2Fparse
     */
    public enum Parse implements JavascriptFunction {
        INSTANCE;

        private static final PreferredDateOrdering PREFERRED_DATE_ORDERING = new
                PreferredDateOrdering();

        // Some of the supported formats are based on what is seen here --
        //   http://dygraphs.com/date-formats.html

        public static final String SLASH_DATE_FMT = "yyyy/MM/dd";
        public static final String SLASH_DATE_TIME_FMT = "yyyy/MM/dd HH:mm";
        public static final String SLASH_DATE_TIME_SECS_FMT = "yyyy/MM/dd HH:mm:ss";
        public static final String SLASH_DATE_TIME_SECS_MS_FMT = "yyyy/MM/dd HH:mm:ss.SSS";

        // It looks like most JavaScript implementations prefer the American ordering of when
        // the year is not first.  So, we'll do the same.
        public static final String MERICA_DATE_FMT = "MM/dd/yyyy";
        public static final String MERICA_DATE_TIME_FMT = "MM/dd/yyyy HH:mm";
        public static final String MERICA_DATE_TIME_WITH_SECS_FMT = "MM/dd/yyyy HH:mm:ss";
        public static final String MERICA_DATE_TIME_WITH_MILLIS_FMT = "MM/dd/yyyy HH:mm:ss.SSS";

        public static final String MERICA_DASH_DATE_FMT = "MM-dd-yyyy";
        public static final String MERICA_DASH_DATE_TIME_FMT = "MM-dd-yyyy HH:mm";
        public static final String MERICA_DASH_DATE_TIME_WITH_SECS_FMT = "MM-dd-yyyy HH:mm:ss";
        public static final String MERICA_DASH_DATE_TIME_WITH_MILLIS_FMT =
                "MM-dd-yyyy HH:mm:ss.SSS";

        public static final String RFC_2822_SHORT_DATE_FMT = "MMM dd, yyyy";
        public static final String RFC_2822_DATE_TIME_FMT = "EEE, dd MMM yyyy HH:mm:ss";
        public static final String RFC_2822_DATE_TIME_WITH_TZ_FMT = "EEE, dd MMM yyyy HH:mm:ss z";
        public static final String RFC_2822_DATE_TIME_WITH_TZS_FMT =
                "EEE, dd MMM yyyy HH:mm:ss 'GMT'Z";
        public static final String ISO8601_DATE_FMT = "yyyy-MM-dd";
        public static final String ISO8601T_DATE_TIME_FMT = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String ISO8601T_DATE_TIME_WITH_TZ1_FMT = "yyyy-MM-dd'T'HH:mm:ssX";
        public static final String ISO8601T_DATE_TIME_WITH_TZ2_FMT = "yyyy-MM-dd'T'HH:mm:ssXX";
        public static final String ISO8601T_DATE_TIME_WITH_TZ3_FMT = "yyyy-MM-dd'T'HH:mm:ssXXX";
        public static final String ISO8601T_DATE_TIME_WITH_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        public static final String ISO8601T_DATE_TIME_WITH_MILLIS_TZ1_FMT =
                "yyyy-MM-dd'T'HH:mm:ss.SSSX";
        public static final String ISO8601T_DATE_TIME_WITH_MILLIS_TZ2_FMT =
                "yyyy-MM-dd'T'HH:mm:ss.SSSXX";
        public static final String ISO8601T_DATE_TIME_WITH_MILLIS_TZ3_FMT =
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        public static final String ISO8601_DATE_TIME_FMT = "yyyy-MM-dd HH:mm";
        public static final String ISO8601_DATE_TIME_WITH_SECS_FMT = "yyyy-MM-dd HH:mm:ss";
        public static final String ISO8601_DATE_TIME_WITH_TZ1_FMT = "yyyy-MM-dd HH:mm:ssX";
        public static final String ISO8601_DATE_TIME_WITH_TZ2_FMT = "yyyy-MM-dd HH:mm:ssXX";
        public static final String ISO8601_DATE_TIME_WITH_TZ3_FMT = "yyyy-MM-dd HH:mm:ssXXX";
        public static final String ISO8601_DATE_TIME_WITH_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
        public static final String ISO8601_DATE_TIME_WITH_MILLIS_TZ1_FMT =
                "yyyy-MM-dd HH:mm:ss.SSSX";
        public static final String ISO8601_DATE_TIME_WITH_MILLIS_TZ2_FMT =
                "yyyy-MM-dd HH:mm:ss.SSSXX";
        public static final String ISO8601_DATE_TIME_WITH_MILLIS_TZ3_FMT =
                "yyyy-MM-dd HH:mm:ss.SSSXXX";

        /**
         * Custom format: such as 2013-10-01T02:39:16.78 America/Los_Angeles
         * We need the timezone as represented as full ID's so that we can parse it back into
         * Datetime object with the correct Chronology setting.
         * A format such as 2013-10-01T02:39:16.78 -08:00 will result in a DateTime object using a
         * -08 chronology which then is not equals to the DateTime object that e.g. gets created by
         * oracle datetime string representations.
         */
        private static final String CUSTOM_FORMATTED_DATE_TIME_W_ZONE = "YYYY-MM-dd'T'HH:mm:ss" +
                ".SSS ZZZ";
        public static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER =
                DateTimeFormat.forPattern(CUSTOM_FORMATTED_DATE_TIME_W_ZONE);

        public static final String[] WITH_TZ_FORMATS = {
                RFC_2822_DATE_TIME_WITH_TZ_FMT,
                RFC_2822_DATE_TIME_WITH_TZS_FMT,
                ISO8601_DATE_TIME_WITH_TZ1_FMT,
                ISO8601_DATE_TIME_WITH_TZ2_FMT,
                ISO8601_DATE_TIME_WITH_TZ3_FMT,
                ISO8601_DATE_TIME_WITH_MILLIS_TZ1_FMT,
                ISO8601_DATE_TIME_WITH_MILLIS_TZ2_FMT,
                ISO8601_DATE_TIME_WITH_MILLIS_TZ3_FMT,
                ISO8601T_DATE_TIME_WITH_TZ1_FMT,
                ISO8601T_DATE_TIME_WITH_TZ2_FMT,
                ISO8601T_DATE_TIME_WITH_TZ3_FMT,
                ISO8601T_DATE_TIME_WITH_MILLIS_TZ1_FMT,
                ISO8601T_DATE_TIME_WITH_MILLIS_TZ2_FMT,
                ISO8601T_DATE_TIME_WITH_MILLIS_TZ3_FMT,
        };

        public static final String[] NO_TZ_FORMATS = {
                RFC_2822_SHORT_DATE_FMT,
                RFC_2822_DATE_TIME_FMT,
                ISO8601_DATE_TIME_FMT,
                ISO8601_DATE_FMT,
                ISO8601_DATE_TIME_WITH_SECS_FMT,
                ISO8601_DATE_TIME_WITH_MILLIS,
                ISO8601T_DATE_TIME_FMT,
                ISO8601T_DATE_TIME_WITH_MILLIS,
                SLASH_DATE_FMT,
                SLASH_DATE_TIME_FMT,
                SLASH_DATE_TIME_SECS_FMT,
                SLASH_DATE_TIME_SECS_MS_FMT,
                MERICA_DATE_FMT,
                MERICA_DATE_TIME_FMT,
                MERICA_DATE_TIME_WITH_SECS_FMT,
                MERICA_DATE_TIME_WITH_MILLIS_FMT,
                MERICA_DASH_DATE_FMT,
                MERICA_DASH_DATE_TIME_FMT,
                MERICA_DASH_DATE_TIME_WITH_SECS_FMT,
                MERICA_DASH_DATE_TIME_WITH_MILLIS_FMT,
        };

        // Pattern used to detect date strings that have microsecond-level precision.
        // The DateTime object doesn't support that much precision, but we should still
        // be able to parse the string.  Unfortunately, the SimpleDateFormat doesn't
        // support microseconds, so we need to scrub out the microsecond digits.
        private static final Pattern MICROSECONDS_TO_MILLISECONDS = Pattern.compile(
                "(.*\\.\\d{3})\\d{3}(.*)");

        private static final Pattern CONTAINS_INCOMPATIBLE_PATTERN = Pattern.compile(
                "[YWFuzXZw]");

        /**
         * Check if a format string includes a timezone pattern.  We're
         * interested in whether a zone is specified since we assume
         * formats without a zone are UTC.
         *
         * XXX There's probably an existing function to figure this out,
         * but I couldn't find it.
         *
         * @param format A SimpleDateFormat format string.
         *
         * @return True if a timezone pattern was found, false otherwise.
         */
        public boolean hasTimeZoneFormat(String format) {
            boolean inQuote = false;

            for (int i = 0; i < format.length(); i++) {
                if (inQuote) {
                    if (format.charAt(i) == '\'') {
                        inQuote = false;
                    }
                } else {
                    switch (format.charAt(i)) {
                        case '\'':
                            inQuote = true;
                            break;
                        case 'z':
                        case 'Z':
                        case 'X':
                            return true;
                    }
                }
            }

            return false;
        }

        @Override
        public Object eval(final List<Object> args) {
            Object date = args.get(0);
            if (date == null) {
                // We want be able to deal with nulls downstream, so that we don't get a
                // Double.NaN value
                return null;
            } else if (date instanceof String) {
                String dateString = (String) date;

                if (args.size() == 2) {
                    if (!(args.get(1) instanceof String)) {
                        throw new SnapDataException(EXPECTING_A_STRING_FOR_DATE_FORMAT)
                                .withResolution(BAD_FORMAT_TYPE_RESOLUTION);
                    }

                    try {
                        String format = (String) args.get(1);
                        boolean usingJoda = canUseJoda(format);
                        DateTime dateTime;

                        if (usingJoda && USE_JODA) {
                             dateTime = DateTimeFormat.forPattern(format).parseDateTime(
                                     dateString);
                        } else {
                            dateTime = new DateTime(
                                    DateUtils.parseDateStrictly(dateString, format));
                        }

                        if (hasTimeZoneFormat(format)) {
                            return dateTime.withZone(DateTimeZone.UTC);
                        } else {
                            return dateTime.withZoneRetainFields(DateTimeZone.UTC);
                        }
                    } catch (ParseException | IllegalFieldValueException e) {
                        return Double.NaN;
                    } catch (IllegalArgumentException e) {
                        throw new SnapDataException(e, THE_DATE_FORMAT_STRING_IS_INVALID)
                                .withReason(e.getMessage())
                                .withResolution(BAD_FORMAT_RESOLUTION);
                    }
                } else {
                    Matcher matcher = MICROSECONDS_TO_MILLISECONDS.matcher(dateString);

                    if (matcher.matches()) {
                        // Found microseconds, make a new string without those digits.
                        dateString = matcher.group(1) + matcher.group(2);
                    }
                    try {
                        return PREFERRED_DATE_ORDERING.parse(dateString);
                    } catch (ParseException e) {
                        try {
                            // Try parse it with the custom formatter,
                            // the length of the string is not known due to the zone ids;
                            return CUSTOM_DATE_TIME_FORMATTER.parseDateTime(dateString);
                        } catch (IllegalArgumentException e3) {
                            // ignore
                        }
                        return Double.NaN;
                    }
                }
            } else if (date instanceof Number) {
                Number millis = (Number) date;

                return new DateTime(millis.longValue());
            }
            return Double.NaN;
        }

        /**
         * Whether the pattern is compatible with both Joda-Time and SimpleDateFormat. The
         * pattern may be incompatible because Joda-Time does not implement that pattern
         * element or uses a different way of designating it.
         *
         * @param pattern the pattern used to parse the datetime string
         * @return
         */
        public static boolean canUseJoda(String pattern) {
            return CONTAINS_INCOMPATIBLE_PATTERN.matcher(pattern).find() == false;
        }
    }

    /**
     * Verifies the corner case, where we get a Date such as 0001-01-01 00:00:00.0000000 and then
     * apply the time zone on top of it, which can lead to a date of 0000-12-13-....,
     * making it an invalid date representation.
     *
     * @param dateTime the datetime
     *
     * @return the incoming datetime if its valid, otherwise NAN
     */
    private static Object verifyValidDateTime(DateTime dateTime) {
        if (dateTime.getCenturyOfEra() == 0 && dateTime.getYearOfCentury() == 0) {
            return Double.NaN;
        }
        return dateTime;
    }

    /**
     * Mimics javascript Date.now method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/now
     * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FDate%2Fnow
     */
    public enum Now implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            return new DateTime(DateTimeZone.UTC);
        }
    }

    /**
     * Mimics javascript Date.UTC method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/UTC
     * ?redirectlocale=en-US&redirectslug=JavaScript%2FReference%2FGlobal_Objects%2FDate%2FUTC
     */
    public enum Utc implements JavascriptFunction {
        INSTANCE;
        private static final int MONTH_OFFSET = 1;
        private static final int DAY_OFFSET = 1;
        private static final String MILLISECONDS = "Milliseconds";
        private static final String SECONDS = "Seconds";
        private static final String MINUTES = "Minutes";
        private static final String HOURS = "Hours";
        private static final String DAYS = "Days";
        private static final String MONTHS = "Months";
        private static final String YEARS = "Years";

        @Override
        public Object eval(final List<Object> args) {
            DateTime defaultDate = new DateTime(1900, MONTH_OFFSET, DAY_OFFSET, 0, 0,
                    DateTimeZone.UTC);
            for (int i = 0; i < args.size(); i++) {
                Object arg = args.get(i);
                ObjectType type = ObjectType.objectToType(arg);
                switch (i) {
                    case 6:
                        defaultDate = defaultDate.plusMillis(getField(arg, type, MILLISECONDS));
                        break;
                    case 5:
                        defaultDate = defaultDate.plusSeconds(getField(arg, type, SECONDS));
                        break;
                    case 4:
                        defaultDate = defaultDate.plusMinutes(getField(arg, type, MINUTES));
                        break;
                    case 3:
                        defaultDate = defaultDate.plusHours(getField(arg, type, HOURS));
                        break;
                    case 2:
                        defaultDate = defaultDate.plusDays(getField(arg, type, DAYS) - DAY_OFFSET);
                        break;
                    case 1:
                        defaultDate = defaultDate.plusMonths(getField(arg, type, MONTHS));
                        break;
                    case 0:
                        defaultDate = defaultDate.withYear(getYear(arg, type));
                        break;
                }
            }
            return defaultDate;
        }

        private int getField(final Object arg, final ObjectType type, final String fieldName) {
            int fieldVal;
            switch (type) {
                case BIG_INTEGER:
                case BIG_DECIMAL:
                case INTEGER:
                case DOUBLE:
                case SHORT:
                case FLOAT:
                case LONG:
                    fieldVal = ((Number) arg).intValue();
                    break;
                case STRING:
                    try {
                        fieldVal = Integer.valueOf(arg.toString());
                        break;
                    } catch (NumberFormatException e) {
                        throw new ExecutionException(e, FIELD_PARAMETER_INVALID)
                                .formatWith(fieldName, arg)
                                .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
                    }
                default:
                    throw new ExecutionException(FIELD_PARAMETER_INVALID)
                            .formatWith(fieldName, arg)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            return fieldVal;
        }

        private int getYear(final Object arg, final ObjectType type) {
            int year;
            switch (type) {
                case BIG_INTEGER:
                case BIG_DECIMAL:
                case INTEGER:
                case DOUBLE:
                case SHORT:
                case FLOAT:
                case LONG:
                    year = ((Number) arg).intValue();
                    break;
                case STRING:
                    try {
                        year = Integer.valueOf(arg.toString());
                        break;
                    } catch (NumberFormatException e) {
                        throw new ExecutionException(e, FIELD_PARAMETER_INVALID)
                                .formatWith(YEARS, arg)
                                .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
                    }
                default:
                    throw new ExecutionException(FIELD_PARAMETER_INVALID)
                            .formatWith(arg)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            if (year >= 0 && year <= 99) {
                year += 1900;
            }
            return year;
        }
    }
}