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

package com.snaplogic.expression;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for greater than, less than, less than equals, and greater than equals.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class GreaterLessTest extends ExpressionTest {
    private static final String GT_FMT = "%s>%s";
    private static final String LT_FMT = "%s<%s";
    private static final String GTE_FMT = "%s>=%s";
    private static final String LTE_FMT = "%s<=%s";
    private static final BigDecimal OP1 = new BigDecimal("7.123111234132413");
    private static final BigDecimal OP2 = new BigDecimal("7");

    @Test
    public void testGreaterThanTrue() {
        assertTrue((Boolean) eval(String.format(GT_FMT, OP1, OP2)));
    }

    @Test
    public void testGreaterThanFalse() {
        assertFalse((Boolean) eval(String.format(GT_FMT, OP2, OP1)));
    }

    @Test
    public void testGreaterThanTrueBoolean() {
        assertTrue((Boolean) eval(String.format(GT_FMT, "true", 0)));
    }

    @Test
    public void testGreaterThanFalseBoolean() {
        assertFalse((Boolean) eval(String.format(GT_FMT, 0, "true")));
    }

    @Test
    public void testGreaterThanTrueStringNumNum() {
        assertTrue((Boolean) eval(String.format(GT_FMT, "\"300\"", 7)));
    }

    @Test
    public void testGreaterThanFalseStringNumNum() {
        assertFalse((Boolean) eval(String.format(GT_FMT, "\"5\"", 7)));
    }

    @Test
    public void testGreaterThanTrueStrNumNum() {
        assertTrue((Boolean) eval(String.format(GT_FMT, 300, "\"7\"")));
    }

    @Test
    public void testGreaterThanFalseStrNumNum() {
        assertTrue((Boolean) eval(String.format(GT_FMT, 300, "\"7\"")));
    }

    @Test
    public void testGreaterThanStrNum() {
        assertFalse((Boolean) eval(String.format(GT_FMT, "\"abc\"", 300)));
    }

    @Test
    public void testGreaterThanNumStr() {
        assertFalse((Boolean) eval(String.format(GT_FMT, 300, "\"abc\"")));
    }

    @Test
    public void testGreaterWithFunction() {
        Object data = ImmutableMap.<String, Object>of("name", "SnapLogic");
        assertTrue((Boolean) eval(String.format(GT_FMT, "$name.indexOf(\"Logic\")", 2), data));
    }

    @Test
    public void testGreaterDates() {
        assertTrue((Boolean) eval(String.format(GT_FMT,
                "Date.parse(\"2011-10-10T14:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")")));
    }

    @Test
    public void testGreaterDatesFalse() {
        assertFalse((Boolean) eval(String.format(GT_FMT,
                "Date.parse(\"2011-10-10T11:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")")));
    }

    @Test
    public void testGreaterDateNum() {
        assertTrue((Boolean) eval(String.format(GT_FMT,
                "Date.parse(\"2011-10-10T15:48:00.123-08:00\")", "1318286880123")));
    }

    @Test
    public void testGreaterDateStr() {
        assertTrue((Boolean) eval(String.format(GT_FMT,
                "Date.parse(\"2011-10-10T14:48:00.123-08:00\")", "\"500\"")));
    }

    @Test
    public void testGreaterDateNumFalse() {
        assertFalse((Boolean) eval(String.format(GT_FMT, "1318286880123",
                "Date.parse(\"2011-10-10T15:48:00.123-08:00\")")));
    }

    @Test
    public void testGreaterLocalDate() {
        assertTrue((Boolean) eval(String.format(GT_FMT, "LocalDate.parse(\"2013-08-29\")",
                "LocalDate.parse(\"2012-08-29\")")));
    }

    @Test
    public void testGreaterLocalDateFalse() {
        assertFalse((Boolean) eval(String.format(GT_FMT, "LocalDate.parse(\"2013-08-29\")",
                "LocalDate.parse(\"2014-08-29\")")));
    }

    @Test
    public void testGreaterLocalDateBoolean() {
        assertFalse((Boolean) eval(String.format(GT_FMT, "LocalDate.parse(\"2013-08-29\")",
                "true")));
    }

    @Test
    public void testGreaterLocalTime() {
        assertTrue((Boolean) eval(String.format(GT_FMT, "LocalTime.parse(\"14:52:00.566\")",
                "LocalTime.parse(\"13:52:00.566\")")));
    }

    @Test
    public void testGreaterLocalTimeFalse() {
        assertFalse((Boolean) eval(String.format(GT_FMT, "LocalTime.parse(\"13:52:00.566\")",
                "LocalTime.parse(\"14:52:00.566\")")));
    }

    @Test
    public void testGreaterLocalTimeBoolean() {
        assertFalse((Boolean) eval(String.format(GT_FMT, "LocalTime.parse(\"13:52:00.566\")",
                "true")));
    }

    @Test
    public void testGreaterLocalDateTime() {
        assertTrue((Boolean) eval(String.format(GT_FMT,
                "LocalDateTime.parse(\"2013-08-29T10:56:44.161\")",
                "LocalDateTime.parse(\"2013-07-29T10:56:44.161\")")));
    }

    @Test
    public void testGreaterLocalDateTimeFalse() {
        assertFalse((Boolean) eval(String.format(GT_FMT,
                "LocalDateTime.parse(\"2013-08-29T10:56:44.161\")",
                "LocalDateTime.parse(\"2013-09-29T10:56:44.161\")")));
    }

    @Test
    public void testGreaterLocalDateTimeBoolean() {
        assertFalse((Boolean) eval(String.format(GT_FMT,
                "LocalDateTime.parse(\"2013-08-29T10:56:44.161\")",
                "true")));
    }

    @Test
    public void testLessThanTrue() {
        assertTrue((Boolean) eval(String.format(LT_FMT, OP2, OP1)));
    }

    @Test
    public void testLessThanFalse() {
        assertFalse((Boolean) eval(String.format(LT_FMT, OP1, OP2)));
    }

    @Test
    public void testLessThanTrueStrNumNum() {
        assertTrue((Boolean) eval(String.format(LT_FMT, "\"4\"", 5)));
    }

    @Test
    public void testLessThanFalseStrNumNum() {
        assertFalse((Boolean) eval(String.format(LT_FMT, "\"123\"", 5)));
    }

    @Test
    public void testLessThanTrueNumStrNum() {
        assertTrue((Boolean) eval(String.format(LT_FMT, 5, "\"123\"")));
    }

    @Test
    public void testLessThanFalseNumStrNum() {
        assertTrue((Boolean) eval(String.format(LT_FMT, 5, "\"123\"")));
    }

    @Test
    public void testLessThanNumStr() {
        assertFalse((Boolean) eval(String.format(LT_FMT, 5, "\"abc\"")));
    }

    @Test
    public void testLessThanStrNum() {
        assertFalse((Boolean) eval(String.format(LT_FMT, "\"abc\"", 5)));
    }

    @Test
    public void testLessThanDates() {
        assertTrue((Boolean) eval(String.format(LT_FMT,
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T13:48:00.123-08:00\")")));
    }

    @Test
    public void testLessThanDatesFalse() {
        assertFalse((Boolean) eval(String.format(LT_FMT,
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T11:48:00.123-08:00\")")));
    }

    @Test
    public void testLessThanDateNum() {
        assertTrue((Boolean) eval(String.format(LT_FMT,
                "Date.parse(\"2011-10-10T10:48:00.123-08:00\")", "1318286880123")));
    }

    @Test
    public void testLessThanDateNumFalse() {
        assertFalse((Boolean) eval(String.format(LT_FMT, "1318286880123",
                "Date.parse(\"2011-10-10T10:48:00.123-08:00\")")));
    }

    @Test
    public void testGreaterThanEqualTrue() {
        assertTrue((Boolean) eval(String.format(GTE_FMT, OP1, OP1)));
    }

    @Test
    public void testGreaterThanEqualFalse() {
        assertFalse((Boolean) eval(String.format(GTE_FMT, OP2, OP1)));
    }

    @Test
    public void testGreaterThanEqualTrueStrNumNum() {
        assertTrue((Boolean) eval(String.format(GTE_FMT, "\"7\"", 5)));
    }

    @Test
    public void testGreaterThanEqualFalseStrNumNum() {
        assertFalse((Boolean) eval(String.format(GTE_FMT, "\"4\"", 5)));
    }

    @Test
    public void testGreaterThanEqualTrueNumStrNum() {
        assertTrue((Boolean) eval(String.format(GTE_FMT, 7, "\"5\"")));
    }

    @Test
    public void testGreaterThanEqualFalseNumStrNum() {
        assertFalse((Boolean) eval(String.format(GTE_FMT, 4, "\"5\"")));
    }

    @Test
    public void testGreaterThanEqualStrNumNum() {
        assertFalse((Boolean) eval(String.format(GTE_FMT, "\"abc\"", 7)));
    }

    @Test
    public void testGreaterThanEqualNumStrNum() {
        assertFalse((Boolean) eval(String.format(GTE_FMT, 7, "\"abc\"")));
    }

    @Test
    public void testGreaterThanEqualDates() {
        assertTrue((Boolean) eval(String.format(GTE_FMT,
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")")));
    }

    @Test
    public void testGreaterThanEqualDatesFalse() {
        assertFalse((Boolean) eval(String.format(GTE_FMT,
                "Date.parse(\"2011-10-10T11:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")")));
    }

    @Test
    public void testGreaterThanEqualDateNum() {
        assertTrue((Boolean) eval(String.format(GTE_FMT,
                "Date.parse(\"2011-10-10T15:48:00.123-08:00\")", "1318286880123")));
    }

    @Test
    public void testGreaterThanEqualDateNumFalse() {
        assertFalse((Boolean) eval(String.format(GTE_FMT, "1318286880123",
                "Date.parse(\"2011-10-10T15:48:00.123-08:00\")")));
    }

    @Test
    public void testLessThanEqualTrue() {
        assertTrue((Boolean) eval(String.format(LTE_FMT, OP2, OP1)));
    }

    @Test
    public void testLessThanEqualFalse() {
        assertFalse((Boolean) eval(String.format(LTE_FMT, OP1, OP2)));
    }

    @Test
    public void testLessThanEqualTrueStrNumNum() {
        assertTrue((Boolean) eval(String.format(LTE_FMT, "\"5\"", 10)));
    }

    @Test
    public void testLessThanEqualTrueNumStrNum() {
        assertTrue((Boolean) eval(String.format(LTE_FMT, 5, "\"10\"")));
    }

    @Test
    public void testLessThanEqualDates() {
        assertTrue((Boolean) eval(String.format(LTE_FMT,
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")")));
    }

    @Test
    public void testLessThanEqualDatesFalse() {
        assertFalse((Boolean) eval(String.format(LTE_FMT,
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\")",
                "Date.parse(\"2011-10-10T11:48:00.123-08:00\")")));
    }

    @Test
    public void testLessThanEqualDateNum() {
        assertTrue((Boolean) eval(String.format(LTE_FMT,
                "Date.parse(\"2011-10-10T10:48:00.123-08:00\")", "1318286880123")));
    }

    @Test
    public void testLessThanEqualDateNumFalse() {
        assertFalse((Boolean) eval(String.format(LTE_FMT, "1318286880123",
                "Date.parse(\"2011-10-10T10:48:00.123-08:00\")")));
    }

    @Test
    public void testLessThanEqualDateTime() {
        assertTrue((Boolean) eval(String.format(LTE_FMT,
                "DateTime.parse('2013-08-29T10:56:44.161-06:00')",
                "DateTime.parse('2013-08-29T10:56:44.161-07:00')")));
    }

    @Test
    public void testLessThanEqualDateTimeFalse() {
        assertFalse((Boolean) eval(String.format(LTE_FMT,
                "DateTime.parse('2013-08-29T10:56:44.161-07:00')",
                "DateTime.parse('2013-08-29T10:56:44.161-06:00')")));
    }
}