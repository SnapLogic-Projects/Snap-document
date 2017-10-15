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

import com.snaplogic.api.ExecutionException;
import com.snaplogic.expression.ExpressionTest;
import com.snaplogic.expression.classes.Date;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.*;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * Tests Date class methods in expression language.
 *
 * @author jinloes, mklumpp
 */
@SuppressWarnings("HardCodedStringLiteral")
public class DateClassMethodTest extends ExpressionTest {

    private static final List<Triple<String, String, DateTime>> PARSE_INPUTS = Arrays.asList(
            Triple.of("2009/07/12", "yyyy/MM/dd",
                    new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
            Triple.of("2009/07/12 12:34", "yyyy/MM/dd HH:mm",
                    new DateTime(2009, 7, 12, 12, 34, 0, DateTimeZone.UTC)),
            Triple.of("2009/07/12 12:34:56", "yyyy/MM/dd HH:mm:ss",
                    new DateTime(2009, 7, 12, 12, 34, 56, DateTimeZone.UTC)),
            Triple.of("2009/07/12 12:34:56.789", "yyyy/MM/dd HH:mm:ss.SSS",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),
            // Cannot parse when too many millis are used, so blank out pattern to skip
            Triple.of("2009/07/12 12:34:56.789012", "",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),

            Triple.of("07/12/2009", "MM/dd/yyyy",
                    new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
            Triple.of("07/12/2009 12:34", "MM/dd/yyyy HH:mm",
                    new DateTime(2009, 7, 12, 12, 34, 0, DateTimeZone.UTC)),
            Triple.of("07/12/2009 12:34:56", "MM/dd/yyyy HH:mm:ss",
                    new DateTime(2009, 7, 12, 12, 34, 56, DateTimeZone.UTC)),
            Triple.of("07/12/2009 12:34:56.789", "MM/dd/yyyy HH:mm:ss.SSS",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),
            // Cannot parse when too many millis are used, so blank out pattern to skip
            Triple.of("07/12/2009 12:34:56.789012", "",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),

            Triple.of("07-12-2009", "MM-dd-yyyy",
                    new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
            Triple.of("07-12-2009 12:34", "MM-dd-yyyy HH:mm",
                    new DateTime(2009, 7, 12, 12, 34, 0, DateTimeZone.UTC)),
            Triple.of("07-12-2009 12:34:56", "MM-dd-yyyy HH:mm:ss",
                    new DateTime(2009, 7, 12, 12, 34, 56, DateTimeZone.UTC)),
            Triple.of("07-12-2009 12:34:56.789", "MM-dd-yyyy HH:mm:ss.SSS",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),
            Triple.of("07-12-2009 12:34:56.789012", "",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),

            Triple.of("2009-07-12", "yyyy-MM-dd",
                    new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
            Triple.of("2009-07-12 12:34", "yyyy-MM-dd HH:mm",
                    new DateTime(2009, 7, 12, 12, 34, 0, DateTimeZone.UTC)),
            Triple.of("2009-07-12 12:34:56", "yyyy-MM-dd HH:mm:ss",
                    new DateTime(2009, 7, 12, 12, 34, 56, DateTimeZone.UTC)),
            Triple.of("2009-07-12 12:34:56.789", "yyyy-MM-dd HH:mm:ss.SSS",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),
            // Cannot parse when too many millis are used, so blank out pattern to skip
            Triple.of("2009-07-12 12:34:56.789123", "",
                    new DateTime(2009, 7, 12, 12, 34, 56, 789, DateTimeZone.UTC)),

            Triple.of("2009-07-12 12:34:56-07:00", "yyyy-MM-dd HH:mm:ssXXX",
                    new DateTime(2009, 7, 12, 19, 34, 56, DateTimeZone.UTC)),
            Triple.of("2009-07-12 12:34:56-0700", "yyyy-MM-dd HH:mm:ssZ",
                    new DateTime(2009, 7, 12, 19, 34, 56, DateTimeZone.UTC))
    );

    // These inputs only work when a pattern is specified, but not with the default parsers
    private static final List<Triple<String, String, DateTime>>
            PARSE_INPUTS_PATTERN = Arrays.asList(
                Triple.of("2009-07-12 12:34:56GMT-07:00", "yyyy-MM-dd HH:mm:sszzz",
                        new DateTime(2009, 7, 12, 19, 34, 56, DateTimeZone.UTC)),
                // Week year is not returning a correct date
//                Triple.of("2009-07-12", "YYYY-MM-dd",
//                        new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009AD", "yyyyGG",
                        new DateTime(2009, 1, 1, 0, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-26", "yyyy-ww",
                        new DateTime(2009, 6, 21, 0, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-07-2", "yyyy-MM-W",
                        new DateTime(2009, 7, 5, 0, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-193", "yyyy-DDD",
                        new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-07-12 2 Sun 7", "yyyy-MM-dd F E u",
                        new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-07-12 12 PM", "yyyy-MM-dd HH aa",
                        new DateTime(2009, 7, 12, 12, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-07-12 24", "yyyy-MM-dd kk",
                        new DateTime(2009, 7, 12, 0, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-07-12 1 PM", "yyyy-MM-dd K a",
                        new DateTime(2009, 7, 12, 13, 0, 0, DateTimeZone.UTC)),
                Triple.of("2009-07-12 4 PM", "yyyy-MM-dd h a",
                        new DateTime(2009, 7, 12, 16, 0, 0, DateTimeZone.UTC))
            );

    private static final List<Pair<Long, String>> DATE_GET_METHODS = Arrays.asList(
            Pair.of(2011L, "getFullYear"),
            Pair.of(10L, "getMonth"),
            Pair.of(6L, "getDate"),
            Pair.of(15L, "getHours"),
            Pair.of(1L, "getMinutes"),
            Pair.of(12L, "getSeconds"),
            Pair.of(123L, "getMilliseconds"),

            Pair.of(2011L, "getUTCFullYear"),
            Pair.of(10L, "getUTCMonth"),
            Pair.of(6L, "getUTCDate"),
            Pair.of(15L, "getUTCHours"),
            Pair.of(1L, "getUTCMinutes"),
            Pair.of(12L, "getUTCSeconds"),
            Pair.of(123L, "getUTCMilliseconds"),

            Pair.of(0L, "getTimezoneOffset"),
            Pair.of(1317913272123L, "getTime")
    );
    private static final List<Pair<String, String>> DATE_MATH_METHODS = Arrays.asList(
            Pair.of("2011-10-06T15:01:17.123Z", "plus(5 * 1000)"),
            Pair.of("2011-10-07T15:01:12.123Z", "plusDays(1)"),
            Pair.of("2011-10-06T16:01:12.123Z", "plusHours(1)"),
            Pair.of("2011-10-06T15:01:12.124Z", "plusMillis(1)"),
            Pair.of("2011-10-06T15:02:12.123Z", "plusMinutes(1)"),
            Pair.of("2011-11-06T15:01:12.123Z", "plusMonths(1)"),
            Pair.of("2011-10-06T15:01:13.123Z", "plusSeconds(1)"),
            Pair.of("2011-10-13T15:01:12.123Z", "plusWeeks(1)"),
            Pair.of("2012-10-06T15:01:12.123Z", "plusYears(1)"),

            Pair.of("2011-10-06T15:01:07.123Z", "minus(5 * 1000)"),
            Pair.of("2011-10-05T15:01:12.123Z", "minusDays(1)"),
            Pair.of("2011-10-06T14:01:12.123Z", "minusHours(1)"),
            Pair.of("2011-10-06T15:01:12.122Z", "minusMillis(1)"),
            Pair.of("2011-10-06T15:00:12.123Z", "minusMinutes(1)"),
            Pair.of("2011-09-06T15:01:12.123Z", "minusMonths(1)"),
            Pair.of("2011-10-06T15:01:11.123Z", "minusSeconds(1)"),
            Pair.of("2011-09-29T15:01:12.123Z", "minusWeeks(1)"),
            Pair.of("2010-10-06T15:01:12.123Z", "minusYears(1)"),

            Pair.of("2011-10-01T15:01:12.123Z", "withDayOfMonth(1)"),
            Pair.of("2011-10-03T15:01:12.123Z", "withDayOfWeek(1)"),
            Pair.of("2011-01-01T15:01:12.123Z", "withDayOfYear(1)"),
            Pair.of("2011-10-06T01:01:12.123Z", "withHourOfDay(1)"),
            Pair.of("2011-10-06T15:01:12.001Z", "withMillisOfSecond(1)"),
            Pair.of("2011-10-06T15:05:12.123Z", "withMinuteOfHour(5)"),
            Pair.of("2011-01-06T15:01:12.123Z", "withMonthOfYear(1)"),
            Pair.of("2011-10-06T15:01:05.123Z", "withSecondOfMinute(5)"),
            Pair.of("2005-10-06T15:01:12.123Z", "withYear(2005)")
    );

    private void compareDates(DateTime expected, DateTime actual) {
        if (!expected.isEqual(actual)) {
            fail(String.format("Dates are not equal.\n" +
                    "Expected date: %s\n" +
                    "Actual date: %s", expected, actual));
        }
    }

    @Test
    public void testParseRfc2822ShortDate() throws ParseException {
        DateTime date = new DateTime(new SimpleDateFormat(
                Date.Parse.RFC_2822_SHORT_DATE_FMT).parse("Aug 9, 1995")).withZoneRetainFields
                (DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"Aug 9, 1995\")"));
    }

    @Test
    public void testParseRfc2822DateTimeWithTz() {
        DateTime date = new DateTime(807926400000L, DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"Wed, 09 Aug 1995 00:00:00 GMT\")"));
    }

    @Test
    public void testParse0001Date() {
        Assert.assertEquals((Object) Double.NaN, eval(
                "Date.parse(\"0001-01-01 00:00:00.0000000\")"));
    }

    @Test
    public void testParseRfc2822DateTime() {
        DateTime date = ISODateTimeFormat.dateHourMinuteSecondMillis().withZoneUTC()
                .parseDateTime("1995-08-09T00:00:00.000");
        compareDates(date, (DateTime) eval("Date.parse(\"Wed, 09 Aug 1995 00:00:00\")"));
    }

    @Test
    public void testParseRfc2822DateTimeEpoch() {
        DateTime date = new DateTime(0L, DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"Thu, 01 Jan 1970 00:00:00 GMT\")"));
    }

    @Test
    public void testParseCustomDate() {
        DateTime date = new DateTime(1318286880123L, DateTimeZone.forTimeZone(TimeZone
                .getTimeZone("America/Los_Angeles")));
        Object dt = eval("Date.parse(\"2011-10-10T15:48:00.123 America/Los_Angeles\")");
        compareDates(date, (DateTime) dt);
    }

    @Test
    public void testParseCustomDatetime() {
        DateTime date = new DateTime(1318286880123L, DateTimeZone.forTimeZone(TimeZone
                .getTimeZone("America/Los_Angeles")));
        Object dt = eval("DateTime.parse(\"2011-10-10T15:48:00.123 America/Los_Angeles\")");
        compareDates(date, (DateTime) dt);
    }

    @Test
    public void testParseRfc2822DateTimeWithTimeZones() {
        DateTime date = new DateTime(14400000L, DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"Thu, 01 Jan 1970 00:00:00 GMT-0400\")"));
        compareDates(date, (DateTime) eval("Date.parse(\"Thu, 01 Jan 1970 00:00:00 GMT-04:00\")"));
    }

    @Test
    public void testParse8601Date() throws ParseException {
        DateTime date = ISODateTimeFormat.dateParser().withZoneUTC().parseDateTime("2011-10-10");
        assertThat((DateTime) eval("Date.parse(\"2011-10-10\")"), equalTo(date));
    }

    @Test
    public void testParse8601DateTime() {
        DateTime date = ISODateTimeFormat.dateHourMinuteSecond()
                .withZoneUTC().parseDateTime("1995-08-09T00:00:00");
        compareDates(date, (DateTime) eval("Date.parse(\"1995-08-09T00:00:00\")"));
    }

    @Test
    public void testParse8601DateTimeWithMillis() {
        DateTime date = ISODateTimeFormat.dateHourMinuteSecondMillis().withZoneUTC()
                .parseDateTime("2011-10-10T14:48:00.123");
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10T14:48:00.123\")"));
    }

    @Test
    public void testParse8601DateTimeWithTimeZone() throws ParseException {
        DateTime date = ISODateTimeFormat.dateTimeParser()
                .parseDateTime("2011-10-10T14:48:00-08:00");
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10T14:48:00-08:00\")"));
    }

    @Test
    public void testParse8601DateTimeWithTimeZoneAndMillis() {
        DateTime date = new DateTime(1318286880123L, DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10T14:48:00.123-08:00\")"));
    }

    @Test
    public void testParse8601DateTimeWithTimeZone1AndMicros() {
        DateTime date = new DateTime(1318286880123L, DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10 14:48:00.123456-08\")"));
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10T14:48:00.123456-08\")"));
    }

    @Test
    public void testParse8601DateTimeWithTimeZone2AndMicros() {
        DateTime date = new DateTime(1318286880123L, DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10 14:48:00.123456-0800\")"));
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10T14:48:00.123456-0800\")"));
    }

    @Test
    public void testParse8601DateTimeWithTimeZone3AndMicros() {
        DateTime date = new DateTime(1318286880123L, DateTimeZone.UTC);
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10 14:48:00.123456-08:00\")"));
        compareDates(date, (DateTime) eval("Date.parse(\"2011-10-10T14:48:00.123456-08:00\")"));
    }

    @Test
    public void testOptimizedDateMethodCalls() {
        DateTime result = eval("Date.parse(\"2011-10-10 14:48:00.123456-08:00\") " +
                "? Date.parse(\"2011-10-10 14:48:00.123456-08:00\") : \"bad\"" );
        assertNotNull(result);
        assertEquals(result.getZone(), DateTimeZone.UTC);
    }


    @Test
    public void testNow() {
        DateTime result = eval("Date.now()");
        assertNotNull(result);
        assertEquals(result.getZone(), DateTimeZone.UTC);
    }

    @Test
    public void testLocalTime() {
        String result = eval("Date.now().toLocaleTimeString" +
                "('{\"timeZone\":\"PST\"}')");
        assertNotNull(result);
        LocalTime resultTime = LocalTime.parse(result);
        LocalTime expected = new LocalTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("PST")));
        int compare = expected.compareTo(resultTime);
        assertTrue(compare >= 0);
    }

    @Test
    public void testLocalTimeFormatted() {
        String result = eval("Date.now().toLocaleTimeString" +
                "('{\"timeZone\":\"PST\", \"format\":\"HH:mm\"}')");
        assertNotNull(result);
        LocalTime resultTime = LocalTime.parse(result);
        LocalTime expected = new LocalTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("PST")));
        int compare = expected.compareTo(resultTime);
        assertTrue(compare >= 0);
    }

    @Test
    public void testLocalDateTime() {
        String result = eval("Date.now().toLocaleDateTimeString" +
                "('{\"timeZone\":\"PST\"}')");
        assertNotNull(result);
        LocalDateTime resultDateTime = LocalDateTime.parse(result);
        LocalDateTime expected = new LocalDateTime(DateTimeZone.forTimeZone(
                TimeZone.getTimeZone("PST")));
        int compare = expected.compareTo(resultDateTime);
        assertTrue(compare >= 0);
    }

    @Test
    public void testUTCLocalDateTime() {
        String result = eval("Date.now().toLocaleDateTimeString()");
        assertNotNull(result);
        LocalDateTime resultDateTime = LocalDateTime.parse(result);
        LocalDateTime expected = new LocalDateTime(DateTimeZone.UTC);
        int compare = expected.compareTo(resultDateTime);
        assertTrue(compare >= 0);
    }

    @Test
    public void testUTCLocalDateTimeFormatted() {
        String result = eval("Date.now().toLocaleDateTimeString" +
                "('{\"format\":\"yyyy-MM-dd\"}')");
        assertNotNull(result);
        LocalDateTime resultDateTime = LocalDateTime.parse(result);
        LocalDateTime expected = new LocalDateTime(DateTimeZone.UTC);
        int compare = expected.compareTo(resultDateTime);
        assertTrue(compare >= 0);
    }

    @Test
    public void testLocalDate() {
        String result = eval("Date.now().toLocaleDateString('{\"timeZone\":\"PST\"}')");
        assertNotNull(result);
        LocalDate expected = new LocalDate(DateTimeZone.forTimeZone(TimeZone.getTimeZone("PST")));
        int compare = expected.compareTo(LocalDate.parse(result));
        assertTrue(compare >= 0);
    }

    @Test
    public void testLocalDateFormatting() {
        String result = eval("Date.now().toLocaleDateString('{\"timeZone\":\"PST\", " +
                "\"format\":\"yyyy-MM-dd\"}')");
        assertNotNull(result);
        LocalDate expected = new LocalDate(DateTimeZone.forTimeZone(TimeZone.getTimeZone("PST")));
        int compare = expected.compareTo(LocalDate.parse(result));
        assertTrue(compare >= 0);
    }

    @Test
    public void testUTCTime() {
        String result = eval("Date.now().toLocaleTimeString()");
        assertNotNull(result);
        LocalTime resultTime = LocalTime.parse(result);
        LocalTime localTime = new LocalTime(DateTimeZone.UTC);
        int compare = localTime.compareTo(resultTime);
        assertTrue(compare >= 0);
    }

    @Test
    public void testUTCDate() {
        String result = eval("Date.now().toLocaleDateString()");
        assertNotNull(result);
        LocalDate localDate = new LocalDate(DateTimeZone.UTC);
        int compare = localDate.compareTo(LocalDate.parse(result));
        assertTrue(compare >= 0);
    }

    @Test
    public void testUtcNoArgs() {
        DateTime expected = new DateTime(-2208988800000L, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC()"));
    }

    @Test
    public void testUtcYear() {
        DateTime expected = new DateTime(1995, 1, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95)"));
    }

    @Test
    public void testUtcYearString() {
        DateTime expected = new DateTime(1995, 1, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(\"95\")"));
    }

    @Test(expected = ExecutionException.class)
    public void testUtcYearInvalidString() {
        DateTime expected = new DateTime(1995, 1, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(\"abc\")"));
    }

    @Test
    public void testUtcYearFloat() {
        DateTime expected = new DateTime(1995, 1, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95.6)"));
    }

    @Test
    public void testUtcYearLarge() {
        DateTime expected = new DateTime(1995, 1, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(1995)"));
    }

    @Test
    public void testUtcMonth() {
        DateTime expected = new DateTime(1995, 9, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8)"));
    }

    @Test
    public void testUtcMonthString() {
        DateTime expected = new DateTime(1995, 9, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, \"8\")"));
    }

    @Test(expected = ExecutionException.class)
    public void testUtcMonthInvalidString() {
        DateTime expected = new DateTime(1995, 9, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, \"abc\")"));
    }

    @Test
    public void testUtcMonthFloat() {
        DateTime expected = new DateTime(1995, 9, 1, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8.8)"));
    }

    @Test
    public void testUtcMonthLarge() {
        DateTime expected = new DateTime(1995, 1, 1, 0, 0, DateTimeZone.UTC).plusMonths(50);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 50)"));
    }

    @Test
    public void testUtcDay() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24)"));
    }

    @Test
    public void testUtcDayString() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, \"24\")"));
    }

    @Test(expected = ExecutionException.class)
    public void testUtcDayInvalidString() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, \"abc\")"));
    }

    @Test
    public void testUtcDayFloat() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24.4)"));
    }

    @Test
    public void testUtcDayLarge() {
        DateTime expected = new DateTime(1995, 9, 1, 0, 0, DateTimeZone.UTC).plusDays(39);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 40)"));
    }

    @Test
    public void testUtcHour() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13)"));
    }

    @Test
    public void testUtcHourString() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, \"13\")"));
    }

    @Test(expected = ExecutionException.class)
    public void testUtcHourInvalidString() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, \"abc\")"));
    }

    @Test
    public void testUtcHourFloat() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13.8)"));
    }

    @Test
    public void testUtcHourLarge() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC).plusHours(36);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 36)"));
    }

    @Test
    public void testUtcMinute() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59)"));
    }

    @Test
    public void testUtcMinuteString() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, \"59\")"));
    }

    @Test(expected = ExecutionException.class)
    public void testUtcMinuteInvalidString() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, \"abc\")"));
    }

    @Test
    public void testUtcMinuteFloat() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59.9)"));
    }

    @Test
    public void testUtcMinuteLarge() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 0, DateTimeZone.UTC).plusMinutes(128);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 128)"));
    }

    @Test
    public void testUtcSecond() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 50, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, 50)"));
    }

    @Test
    public void testUtcSecondString() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 50, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, \"50\")"));
    }

    @Test(expected = ExecutionException.class)
    public void testUtcSecondInvalidString() {
        DateTime expected = new DateTime(1995, 9, 24, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, \"abc\")"));
    }

    @Test
    public void testUtcSecondFloat() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 50, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, 50.9999)"));
    }

    @Test
    public void testUtcSecondLarge() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 0, DateTimeZone.UTC)
                .plusSeconds(5000);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, 5000)"));
    }

    @Test
    public void testUtcMillis() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 59, 50, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, 59, 50)"));
    }

    @Test
    public void testUtcMillisString() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 59, 50, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, 59, \"50\")"));
    }

    @Test(expected = ExecutionException.class)
    public void testUtcMillisInvalidString() {
        eval("Date.UTC(95, 8, 24, 13, 59, 59, \"abc\")");
    }

    @Test
    public void testUtcMillisFloat() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 59, 50, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, 59, 50.9999)"));
    }

    // Test formats taken from -- http://dygraphs.com/date-formats.html

    @Test
    public void testUtcMillisLarge() {
        DateTime expected = new DateTime(1995, 9, 24, 13, 59, 59, DateTimeZone.UTC)
                .plusMillis(5000);
        compareDates(expected, (DateTime) eval("Date.UTC(95, 8, 24, 13, 59, 59, 5000)"));
    }

    @Test
    public void testBadDate() {
        assertEquals((Object) Double.NaN, eval("Date.parse('bad')"));
    }

    @Test
    public void testBadDateCond() {
        assertTrue((Boolean) eval("isNaN(Date.parse('bad'))"));
    }

    @Test
    public void testParse() {
        for (Triple<String, String, DateTime> triple : PARSE_INPUTS) {
            String toEval;
            if (triple.getMiddle().isEmpty() == false) {
                toEval = String.format(
                        "Date.parse('%s', '%s')", triple.getLeft(), triple.getMiddle());
                assertEquals(triple.toString(), triple.getRight(), eval(toEval));
            }

            toEval = String.format(
                    "Date.parse('%s')", triple.getLeft());
            assertEquals(triple.toString(), triple.getRight(), eval(toEval));
        }

        for (Triple<String, String, DateTime> triple : PARSE_INPUTS_PATTERN) {
            String toEval = String.format(
                    "Date.parse('%s', '%s')", triple.getLeft(), triple.getMiddle());
            assertEquals(triple.toString(), triple.getRight(), eval(toEval));
        }

        String toEval = "Date.parse('2009-13', 'yyyy-MM')";
        assertEquals("Testing unparsable string", (Object) Double.NaN, eval(toEval));
    }

    @Test
    public void testCanUseJoda() {
        assertFalse(Date.Parse.canUseJoda("yyyy-MM-dd HH:mm:ssXXX"));
        assertFalse(Date.Parse.canUseJoda("YYYY-MM-dd"));
        assertFalse(Date.Parse.canUseJoda("yyyy-MM-W"));
        assertFalse(Date.Parse.canUseJoda("yyyy-MM-dd F E u"));
        assertFalse(Date.Parse.canUseJoda("yyyy-MM-dd HH:mm:sszzz"));
        assertFalse(Date.Parse.canUseJoda("yyyy-MM-dd HH:mm:ssXXX"));

        assertTrue(Date.Parse.canUseJoda("yyyy/MM/dd HH:mm:ss.SSS"));
    }

    @Test
    public void testEpochTimestamp() {
        final long millis = 1394043899L * 1000;
        DateTime date = new DateTime(millis);
        compareDates(date, (DateTime) eval(String.format("Date.parse(%s)", millis)));
    }

    @Test
    public void testParseWithFormat() {
        DateTime expected = new DateTime(1995, 2, 1, 0, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.parse('1995_02_01', 'yyyy_MM_dd')"));
    }

    @Test
    public void testParseWithFormatAndZone() {
        DateTime expected = new DateTime(1995, 2, 1, 8, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.parse('1995_02_01PST', 'yyyy_MM_ddz')"));
    }

    @Test
    public void testParseWithFormatAndQuotedZ() {
        DateTime expected = new DateTime(1995, 2, 1, 0, 0, 0, DateTimeZone.UTC);
        compareDates(expected, (DateTime) eval("Date.parse('1995_02_01z', 'yyyy_MM_dd\\'z\\'')"));
    }

    @Test(expected = SnapDataException.class)
    public void testParseWithBadFormat() {
        eval("Date.parse('1995_02_01', 123)");
    }

    @Test(expected = SnapDataException.class)
    public void testParseWithBadFormat2() {
        eval("Date.parse('1995_02_01', 'qqqqqq32')");
    }

    @Test
    public void testToString() {
        assertEquals("1995-08-09T00:00:00.000Z",
                eval("Date.parse(\"Wed, 09 Aug 1995 00:00:00\").toString()"));
    }

    @Test
    public void testGetterMethods() {
        for (Pair<Long, String> pair : DATE_GET_METHODS) {
            Number actual = eval(String.format("Date.parse('2011-10-06T15:01:12.123').%s()",
                    pair.getRight()));

            assertEquals(pair.toString(), pair.getLeft().longValue(), actual.longValue());
        }
    }

    @Test
    public void testGetDay() {
        assertEquals(BigInteger.valueOf(0), eval("Date.parse('2016-10-16').getDay()"));
    }

    @Test
    public void testArithmeticMethods() {
        for (Pair<String, String> pair : DATE_MATH_METHODS) {
            String actual = eval(String.format(
                    "Date.parse('2011-10-06T15:01:12.123').%s.toString()", pair.getRight()));

            assertEquals(pair.toString(), pair.getLeft(), actual);
        }
    }

    @Test(expected = SnapDataException.class)
    public void testPlusInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plus(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusDaysInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusDays(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusHoursInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusHours(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusMillisInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusMillis(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusMinutesInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusMinutes(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusMonthsInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusMonths(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusSecondsInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusSeconds(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusWeeksInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusWeeks(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testPlusYearsInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').plusYears(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minus(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusDaysInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusDays(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusHoursInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusHours(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusMillisInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusMillis(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusMinutesInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusMinutes(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusMonthsInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusMonths(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusSecondsInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusSeconds(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusWeeksInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusWeeks(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testMinusYearsInvalid() {
        eval("Date.parse('2013-08-29T10:56:44.161').minusYears(invalid)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadDayOfWeek() {
        eval("Date.parse('2011-10-06T15:01:12.123').withDayOfWeek(25)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadMillis() {
        eval("Date.parse('2011-10-06T15:01:12.123').withMillisOfSecond(-25)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadSeconds() {
        eval("Date.parse('2011-10-06T15:01:12.123').withSecondOfMinute(-25)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadMinute() {
        eval("Date.parse('2011-10-06T15:01:12.123').withMinuteOfHour(-25)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadHour() {
        eval("Date.parse('2011-10-06T15:01:12.123').withHourOfDay(-25)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadDay() {
        eval("Date.parse('2011-10-06T15:01:12.123').withDayOfMonth(-25)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadMonth() {
        eval("Date.parse('2011-10-06T15:01:12.123').withMonthOfYear(-25)");
    }

    @Test(expected = SnapDataException.class)
    public void testBadDayOfYear() {
        eval("Date.parse('2011-10-06T15:01:12.123').withDayOfYear(-25)");
    }
}