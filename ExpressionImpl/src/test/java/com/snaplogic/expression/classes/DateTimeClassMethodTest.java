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
 * Tests for {@link com.snaplogic.expression.classes.DateTime}
 *
 * @author jinloes, mklumpp
 */
@SuppressWarnings("HardCodedStringLiteral")
public class DateTimeClassMethodTest extends ExpressionTest {
    @Test
    public void testParse() {
        String dateStr = "2013-08-29T10:56:44.161-07:00";
        assertEquals(ISODateTimeFormat.dateTime().parseDateTime(dateStr),
                com.snaplogic.expression.classes.DateTime.Parse.INSTANCE.eval(
                        Lists.<Object>newArrayList(dateStr)));

    }

    @Test
    public void testParseNonStandard() throws Exception {
        String dateStr = "2013-08-29 10:56:44.161";
        org.joda.time.DateTime expected = new org.joda.time.DateTime(DateUtils.parseDateStrictly(
                dateStr, Date.Parse.NO_TZ_FORMATS))
                .withZoneRetainFields(DateTimeZone.UTC);
        assertEquals(expected,
                com.snaplogic.expression.classes.DateTime.Parse.INSTANCE.eval(
                        Lists.<Object>newArrayList(dateStr)));

    }

    @Test
    public void testInvalidDateTime() {
        assertEquals((Object) Double.NaN, eval("DateTime.parse('bad')"));
    }
}
