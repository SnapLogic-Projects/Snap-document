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
 * Tests for {@link com.snaplogic.expression.classes.LocalDate}.
 *
 * @author jinloes
 */
public class LocalDateClassMethodTest extends ExpressionTest {

    @Test
    public void testParse() {
        String dateStr = "2013-08-29";
        assertEquals(ISODateTimeFormat.localDateParser().parseLocalDate(dateStr),
                LocalDate.Parse.INSTANCE.eval(Lists.<Object>newArrayList(dateStr)));
    }

    @Test
    public void testParseNonStandard() throws Exception {
        String dateStr = "2013/08/29";
        org.joda.time.LocalDate expected = new org.joda.time.DateTime(DateUtils.parseDateStrictly
                (dateStr, LocalDate.Parse.NO_TZ_FORMATS)).withZoneRetainFields
                (DateTimeZone.UTC).toLocalDate();
        assertEquals(expected,
                LocalDate.Parse.INSTANCE.eval(Lists.<Object>newArrayList(dateStr)));
    }

    @Test
    public void testToString() {
        assertEquals("2013-08-29", eval("LocalDate.parse('2013-08-29').toString()"));
    }

    @Test
    public void testTimeZone() throws Exception {
        String dateStr = "2013/08/29";
        org.joda.time.LocalDate expected = new org.joda.time.DateTime(DateUtils.parseDateStrictly
                (dateStr, LocalDate.Parse.NO_TZ_FORMATS)).withZoneRetainFields
                (DateTimeZone.UTC).toLocalDate();
        assertEquals(expected, eval("LocalDate.parse(Date.parse(\"2013-08-29T10:56:44.161\")" +
                ".toLocaleDateString('{\"timeZone\":\"UTC\"}'))"));
    }

    @Test
    public void testInvalidLocalDate() {
        assertEquals((Object) Double.NaN, eval("LocalDate.parse('bad')"));
    }
}
