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

import com.google.common.collect.Lists;
import com.snaplogic.expression.ExpressionTest;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link LocalDateTime}.
 *
 * @author jinloes, mklumpp
 */
public class LocalDateTimeClassMethodTest extends ExpressionTest {
    @Test
    public void testParse() {
        String dateStr = "2013-08-29T10:56:44.161";
        assertEquals(ISODateTimeFormat.dateHourMinuteSecondMillis().parseLocalDateTime(dateStr),
                LocalDateTime.Parse.INSTANCE.eval(Lists.<Object>newArrayList(dateStr)));
    }

    @Test
    public void testParseNonStandard() throws Exception {
        String dateStr = "2013-08-29 10:56:44";
        org.joda.time.LocalDateTime expected = new org.joda.time.DateTime(
                DateUtils.parseDateStrictly(dateStr, Date.Parse.ISO8601_DATE_TIME_WITH_SECS_FMT))
                .withZoneRetainFields(DateTimeZone.UTC)
                .toLocalDateTime();
        assertEquals(expected, LocalDateTime.Parse.INSTANCE.eval(Lists.<Object>newArrayList
                (dateStr)));
    }

    @Test
    public void testParseWithTimeZone() throws Exception {
        String dateStr = "2013-08-29 10:56:44";
        org.joda.time.LocalDateTime expected = new org.joda.time.DateTime(
                DateUtils.parseDateStrictly(dateStr, Date.Parse.ISO8601_DATE_TIME_WITH_SECS_FMT))
                .withZoneRetainFields(DateTimeZone.UTC)
                .toLocalDateTime();
        assertEquals(expected, eval("LocalDateTime.parse(Date.parse(\"2013-08-29T10:56:44\")" +
                ".toLocaleDateTimeString('{\"timeZone\":\"UTC\"}'))"));
    }

    @Test
    public void testInvalidLocalDateTime() {
        assertEquals((Object) Double.NaN, eval("LocalDateTime.parse('bad')"));
    }


    @Test
    public void testNullLocalDateTime() {
        assertEquals(null, eval("LocalDateTime.parse(null)"));
    }
}
