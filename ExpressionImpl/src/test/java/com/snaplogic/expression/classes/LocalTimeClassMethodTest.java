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
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link LocalTime}.
 *
 * @author jinloes, mklumpp
 */
public class LocalTimeClassMethodTest extends ExpressionTest {
    @Test
    public void testParse() {
        String timeStr = "13:52:00.566";
        assertEquals(ISODateTimeFormat.localTimeParser().parseLocalTime(timeStr),
                LocalTime.Parse.INSTANCE.eval(Lists.<Object>newArrayList(timeStr)));
    }

    @Test
    public void testParseNonMilliseconds() {
        String timeStr = "13:52:00";
        assertEquals(ISODateTimeFormat.localTimeParser().parseLocalTime(timeStr),
                LocalTime.Parse.INSTANCE.eval(Lists.<Object>newArrayList(timeStr)));
    }

    @Test
    public void testParseWithTimeZone() {
        String timeStr = "03:56:44";
        assertEquals(ISODateTimeFormat.localTimeParser().parseLocalTime(timeStr),
                eval("LocalTime.parse(Date.parse(\"2013-08-29T10:56:44\")" +
                        ".toLocaleTimeString('{\"timeZone\":\"US/Pacific\"}'))"));
    }

    @Test
    public void testInvalidLocalTime() {
        assertEquals((Object) Double.NaN, eval("LocalTime.parse('bad')"));
    }
}
