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
package com.snaplogic.util;

import com.snaplogic.api.ExecutionException;
import com.snaplogic.expression.classes.Date;
import com.snaplogic.snap.api.DocumentException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;

import static com.snaplogic.util.Messages.INVALID_DATE_TIME_FORMAT;
import static com.snaplogic.util.Messages.INVALID_DATE_TIME_ZONE_FORMAT;

/**
 * Helper class for deserializing our custom data types.
 *
 * @author tstack
 */
public class JsonSnapTypes {
    private static final DateTimeFormatter parser = ISODateTimeFormat.localDateParser();
    private static final DateTimeFormatter timeParser = ISODateTimeFormat.localTimeParser();
    private static final DateTimeFormatter dateTimeParser = ISODateTimeFormat.dateTimeParser();
    /**
     * ISO date time format with zone info, we expect '+|-HH:mm' as offset from UTC as in:
     * yyyy-dd-MM'T'HH:mm:ss.SSS+|-HH:mm
     */
    private static final int ISO_FORMATED_DATE_TIME_LENGTH = 29;
    private static final int ORACLE_FORMATED_DATE_TIME_LENGTH = 30;
    /**
     * ISO date time format with no zone info, we expect 'Z' for UTC as in:
     * yyyy-dd-MM'T'HH:mm:ss.SSS'Z'
     */
    private static final int ISO_FORMATED_DATE_TIME_UTC_LENGTH = 24;
    private static final int ISO_FORMATED_DATE_TIME_LENGTH_NO_ZONE = 23;
    private static final String COLON = ":";

    /**
     * Custom format: such as 2013-10-01T02:39:16.78 America/Los_Angeles
     * We need the timezone as represented as full ID's so that we can parse it back into
     * Datetime object with the correct Chronology setting.
     * A format such as 2013-10-01T02:39:16.78 -08:00 will result in a DateTime object using a
     * -08 chronology which then is not equals to the DateTime object that e.g. gets created by
     * oracle datetime string representations.
     */
    private static final String CUSTOM_FORMATTED_DATE_TIME_W_ZONE = "YYYY-MM-dd'T'HH:mm:ss.SSS " +
            "ZZZ";
    private static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER =
            DateTimeFormat.forPattern(CUSTOM_FORMATTED_DATE_TIME_W_ZONE);
    /**
     * This is equivalent to UTC
     */
    private static final String ISO_NO_ZONE = "Z";

    public static final String SNAP_TYPE_LOCAL_DATE = "_snaptype_localdate";
    public static final String SNAP_TYPE_DATE_TIME = "_snaptype_datetime";
    public static final String SNAP_TYPE_LOCAL_TIME = "_snaptype_localtime";
    public static final String SNAP_TYPE_LOCAL_DATE_TIME = "_snaptype_localdatetime";
    public static final String SNAP_TYPE_BINARY = "_snaptype_binary_base64";

    /**
     * Special Number types
     */
    public static final String SNAP_TYPE_DOUBLE_NUMBER_NAN = "_snaptype_double_nan";
    public static final String SNAP_TYPE_DOUBLE_POS_INFINITY = "_snaptype_double_pos_infinity";
    public static final String SNAP_TYPE_DOUBLE_NEG_INFINITY = "_snaptype_double_neg_infinity";

    public static final String SNAP_TYPE_FLOAT_NUMBER_NAN = "_snaptype_float_nan";
    public static final String SNAP_TYPE_FLOAT_POS_INFINITY = "_snaptype_float_pos_infinity";
    public static final String SNAP_TYPE_FLOAT_NEG_INFINITY = "_snaptype_float_neg_infinity";

    private JsonSnapTypes() {

    }

    public static Object castToSnapType(String key, Object value) {
        String keyStr = key.toLowerCase();
        switch (keyStr) {
            case SNAP_TYPE_LOCAL_DATE:
                return parser.parseLocalDate(String.valueOf(value));
            case SNAP_TYPE_DATE_TIME:
                return parseDateTime(String.valueOf(value));
            case SNAP_TYPE_LOCAL_DATE_TIME:
                return dateTimeParser.parseLocalDateTime(String.valueOf(value));
            case SNAP_TYPE_LOCAL_TIME:
                return timeParser.parseLocalTime(String.valueOf(value));
            case SNAP_TYPE_BINARY:
                return Base64.decodeBase64(String.valueOf(value));
            case SNAP_TYPE_DOUBLE_NUMBER_NAN:
                return Double.NaN;
            case SNAP_TYPE_DOUBLE_POS_INFINITY:
                return Double.POSITIVE_INFINITY;
            case SNAP_TYPE_DOUBLE_NEG_INFINITY:
                return Double.NEGATIVE_INFINITY;
            case SNAP_TYPE_FLOAT_NUMBER_NAN:
                return Float.NaN;
            case SNAP_TYPE_FLOAT_POS_INFINITY:
                return Float.POSITIVE_INFINITY;
            case SNAP_TYPE_FLOAT_NEG_INFINITY:
                return Float.NEGATIVE_INFINITY;
            default:
                return null;
        }
    }

    /**
     * Parses the ISO datetime string representation into a {@link DateTime}.
     *
     * @param dateTime the string representation of the ISO formatted date
     *
     * @return the parsed {@link DateTime}
     */
    private static DateTime parseDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        final DateTimeFormatter formatter;
        final String zone;
        String[] zoneParts;
        Integer hourOffset;
        Integer minuteOffset;
        switch (dateTime.length()) {
            case ISO_FORMATED_DATE_TIME_UTC_LENGTH:
                zone = dateTime.substring(ISO_FORMATED_DATE_TIME_LENGTH_NO_ZONE,
                        dateTime.length());
                if (!zone.equalsIgnoreCase(ISO_NO_ZONE)) {
                    throw new DocumentException(INVALID_DATE_TIME_ZONE_FORMAT, zone);
                }
                formatter = dateTimeParser.withZoneUTC();
                break;
            case ISO_FORMATED_DATE_TIME_LENGTH:
                zone = dateTime.substring(ISO_FORMATED_DATE_TIME_LENGTH_NO_ZONE,
                        dateTime.length());
                StringUtils.trim(zone);
                zoneParts = zone.split(COLON);
                if (zoneParts.length != 2) {
                    throw new DocumentException(INVALID_DATE_TIME_ZONE_FORMAT, zone);
                }
                hourOffset = Integer.parseInt(zoneParts[0]);
                minuteOffset = Integer.parseInt(zoneParts[1]);
                formatter = dateTimeParser.withZone(DateTimeZone
                        .forOffsetHoursMinutes(hourOffset, minuteOffset));
                break;
            case ORACLE_FORMATED_DATE_TIME_LENGTH:
                String fullZone = dateTime.substring(ISO_FORMATED_DATE_TIME_LENGTH_NO_ZONE,
                        dateTime.length());
                zone = StringUtils.trim(fullZone);
                zoneParts = zone.split(COLON);
                if (zoneParts.length != 2) {
                    throw new DocumentException(INVALID_DATE_TIME_ZONE_FORMAT, zone);
                }
                hourOffset = Integer.parseInt(zoneParts[0]);
                minuteOffset = Integer.parseInt(zoneParts[1]);
                try {
                    return new DateTime(DateUtils.parseDateStrictly(dateTime,
                            Date.Parse.WITH_TZ_FORMATS)).withZone(
                            DateTimeZone.forOffsetHoursMinutes(hourOffset, minuteOffset));
                } catch (ParseException ex) {
                    String msg = String.format(INVALID_DATE_TIME_FORMAT, dateTime);
                    throw new ExecutionException(ex, msg)
                            .withReason(msg)
                            .withResolutionAsDefect();
                }
            default:
                // Try parse it with the custom formatter, the length of the string is not know
                // due to the zone ids;
                return CUSTOM_DATE_TIME_FORMATTER.parseDateTime(dateTime);
        }
        return formatter.parseDateTime(dateTime);
    }
}
