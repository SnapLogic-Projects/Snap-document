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
import com.snaplogic.api.ExecutionException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for equals and not equals.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class EqualsNotEqualsTest extends ExpressionTest {

    @Test
    public void testBooleanEqualsTrue() {
        assertTrue((Boolean) eval("true == true"));
    }

    @Test
    public void testNumberTypesEqualsTrue() {
        Object data = ImmutableMap.<String, Object>of("a", BigDecimal.ONE, "b", 1);
        assertTrue((Boolean) eval("$a == $b", data));
    }

    @Test
    public void testBooleanEqualsFalse() {
        assertFalse((Boolean) eval("true == false"));
    }

    @Test
    public void testBooleanNotEqualsTrue() {
        assertTrue((Boolean) eval("true != false"));
    }

    @Test
    public void testMixedBooleanEqualityTrue() {
        assertTrue((Boolean) eval("true && 10 == 10"));
    }

    @Test
    public void testMixedBooleanEqualityFalse() {
        assertFalse((Boolean) eval("true && 10 == 7"));
    }

    @Test
    public void testBooleanNotEqualsFalse() {
        assertFalse((Boolean) eval("true != true"));
    }

    @Test(expected = ExecutionException.class)
    public void testNoEqualsSyntaxError() {
        eval("$false!!====true");
    }

    @Test
    public void testStringEqualsTrue() {
        assertTrue((Boolean) eval("\"snapLogic\" == 'snapLogic'"));
    }

    @Test
    public void testStringEqualsFalse() {
        assertFalse((Boolean) eval("\"snapLogic\" == \"snap\""));
    }

    @Test
    public void testStringNonEqualsTrue() {
        assertTrue((Boolean) eval("\"\\\"snapLogic\\\"\" != 'snap'"));
    }

    @Test
    public void testStringNonEqualsFalse() {
        assertFalse((Boolean) eval("\"snapLogic\" != \"snapLogic\""));
    }

    @Test
    public void testNumberEqualsTrue() {
        assertTrue((Boolean) eval("5 == 5"));
    }

    @Test
    public void testOctNumberEqualsTrue() {
        assertTrue((Boolean) eval("63 == 077"));
    }

    @Test
    public void testHexNumberEqualsTrue() {
        assertTrue((Boolean) eval("255 == 0xff"));
    }

    @Test
    public void testNumberStrNumEqualsTrue() {
        assertTrue((Boolean) eval("5 == '5'"));
    }

    @Test
    public void testStrNumNumberEqualsTrue() {
        assertTrue((Boolean) eval("5 == '5'"));
    }

    @Test
    public void testNumberStrEqualsFalse() {
        assertFalse((Boolean) eval("5 == 'abc'"));
    }

    @Test
    public void testStrNumNumberEqualsFalse() {
        assertFalse((Boolean) eval("'5.5' == '5'"));
    }

    @Test
    public void testNumberEqualsFalse() {
        assertFalse((Boolean) eval("5 == 6"));
    }

    @Test
    public void testNaNEqualsNaN() {
        assertFalse((Boolean) eval("NaN == NaN"));
    }

    @Test
    public void testInfinityEqualsInfinity() {
        assertTrue((Boolean) eval("Infinity == Infinity"));
    }

    @Test
    public void testDateEquals() {
        assertTrue((Boolean) eval(
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\") == " +
                        "Date.parse(\"2011-10-10T12:48:00.123-08:00\")"));
    }

    @Test
    public void testDateEqualsFalse() {
        assertFalse((Boolean) eval("Date.parse(\"2011-10-10T12:48:00.123-08:00\") == " +
                "Date.parse(\"2011-10-10T11:48:00.123-08:00\")"));
    }

    @Test
    public void testDateNumEquals() {
        assertTrue((Boolean) eval("1318290480123 " +
                "== Date.parse(\"2011-10-10T15:48:00.123-08:00\")"));
    }

    @Test
    public void testEqualsLocalDate() {
        assertTrue((Boolean) eval("LocalDate.parse(\"2013-08-29\") == " +
                "LocalDate.parse(\"2013-08-29\")"));
    }

    @Test
    public void testEqualsLocalDateFalse() {
        assertFalse((Boolean) eval("LocalDate.parse(\"2013-08-29\") == " +
                "LocalDate.parse(\"2012-08-29\")"));
    }

    @Test
    public void testNumberNotEqualsTrue() {
        assertTrue((Boolean) eval("5 != 6"));
    }

    @Test
    public void testNumberNotEqualsFalse() {
        assertFalse((Boolean) eval("5 != 5"));
    }

    @Test
    public void testDateNotEquals() {
        assertTrue((Boolean) eval(
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\") !=" +
                        "Date.parse(\"2011-10-10T11:48:00.123-08:00\")"));
    }

    @Test
    public void testDateNotEqualsFalse() {
        assertFalse((Boolean) eval(
                "Date.parse(\"2011-10-10T12:48:00.123-08:00\") != " +
                        "Date.parse(\"2011-10-10T12:48:00.123-08:00\")"));
    }

    @Test
    public void testNotEqualsNumStrNum() {
        assertFalse((Boolean) eval("5 != \"5\""));
    }

    @Test
    public void testNotEqualsStrNumNum() {
        assertFalse((Boolean) eval("\"5\" != 5"));
    }

    @Test
    public void testNotEqualsStrNum() {
        assertTrue((Boolean) eval("\"abc\" != 5"));
    }

    @Test
    public void testNotEqualsLocalDate() {
        assertTrue((Boolean) eval("LocalDate.parse(\"2013-08-29\") != " +
                "LocalDate.parse(\"2011-08-29\")"));
    }

    @Test
    public void testNotEqualsLocalDateFalse() {
        assertFalse((Boolean) eval("LocalDate.parse(\"2013-08-29\") != " +
                "LocalDate.parse(\"2013-08-29\")"));
    }

    @Test
    public void testNotEqualsNumStr() {
        assertTrue((Boolean) eval("5 != \"abc\""));
    }

    @Test
    public void testChainedEquals() {
        assertFalse((Boolean) eval("5 == 5 == false"));
    }

    @Test
    public void testChainedNotEquals() {
        assertFalse((Boolean) eval("5 != 6 != true"));
    }

    @Test
    public void testNotNull() {
        Object data = ImmutableMap.<String, Object>of("a", "abc");
        assertTrue((Boolean) eval("$a != null", data));
    }

    @Test
    public void testNotNullFalse() {
        Object data = new HashMap<String, Object>() {{
            put("a", null);
        }};
        assertFalse((Boolean) eval("$a != null", data));
    }

    @Test
    public void testDecimalEquals() {
        assertTrue((Boolean) eval("0.0 == 0.00"));
    }

    @Test
    public void testDecimalNotEquals() {
        assertFalse((Boolean) eval("0.0 != 0.00"));
    }

    @Test
    public void testNumberStringEquals() {
        assertFalse((boolean) eval("'0' == '00'"));
    }

    @Test
    public void testFalseEqualsZero() {
        assertTrue((boolean) eval("false == 0"));
    }

    @Test
    public void testTrueEqualsOne() {
        assertTrue((boolean) eval("true == 1"));
    }
}
