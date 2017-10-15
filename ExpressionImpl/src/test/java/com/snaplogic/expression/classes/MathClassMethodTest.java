/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.expression.classes;

import com.snaplogic.expression.ExpressionTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * MathClassMethodTest
 *
 * @author mklumpp
 */
public class MathClassMethodTest extends ExpressionTest {
    private static final List<Pair<Object, String>> BASIC_TESTS = Arrays.asList(
            Pair.<Object, String>of(new BigDecimal("3.141592653589793"), "Math.PI"),

            Pair.<Object, String>of(Double.NaN, "Math.abs('foo')"),
            Pair.<Object, String>of(new BigDecimal("2"), "Math.abs(2)"),
            Pair.<Object, String>of(new BigDecimal("2"), "Math.abs(-2)"),
            Pair.<Object, String>of(new BigDecimal("0"), "Math.abs(0)"),

            Pair.<Object, String>of(Double.NaN, "Math.ceil('foo')"),
            Pair.<Object, String>of(Double.NaN, "Math.ceil()"),
            Pair.<Object, String>of(new BigInteger("2"), "Math.ceil(1.2)"),
            Pair.<Object, String>of(new BigInteger("-1"), "Math.ceil(-1.2)"),

            Pair.<Object, String>of(Double.NaN, "Math.floor('foo')"),
            Pair.<Object, String>of(Double.NaN, "Math.floor()"),
            Pair.<Object, String>of(new BigInteger("1"), "Math.floor(1.2)"),
            Pair.<Object, String>of(new BigInteger("-2"), "Math.floor(-1.2)"),

            Pair.<Object, String>of(Double.NEGATIVE_INFINITY, "Math.max()"),
            Pair.<Object, String>of(new BigDecimal("1"), "Math.max(1)"),
            Pair.<Object, String>of(Double.NaN, "Math.max(1, 'foo')"),
            Pair.<Object, String>of(new BigDecimal("2"), "Math.max(2, 1)"),
            Pair.<Object, String>of(new BigDecimal("2"), "Math.max(1, 2)"),

            Pair.<Object, String>of(Double.POSITIVE_INFINITY, "Math.min()"),
            Pair.<Object, String>of(new BigDecimal("1"), "Math.min(1)"),
            Pair.<Object, String>of(Double.NaN, "Math.min(1, 'foo')"),
            Pair.<Object, String>of(new BigDecimal("1"), "Math.min(2, 1)"),
            Pair.<Object, String>of(new BigDecimal("1"), "Math.min(1, 2)"),
            Pair.<Object, String>of(new BigDecimal("1"), "Math.min(...[1, 2])"),

            Pair.<Object, String>of(Double.NaN, "Math.pow('foo')"),
            Pair.<Object, String>of(Double.NaN, "Math.pow()"),
            Pair.<Object, String>of(new BigDecimal("8"), "Math.pow(2, 3)"),

            Pair.<Object, String>of(Double.NaN, "Math.sign('foo')"),
            Pair.<Object, String>of(Double.NaN, "Math.sign()"),
            Pair.<Object, String>of(new BigInteger("1"), "Math.sign(55)"),
            Pair.<Object, String>of(new BigInteger("0"), "Math.sign(0)"),
            Pair.<Object, String>of(new BigInteger("-1"), "Math.sign(-55)"),

            Pair.<Object, String>of(Double.NaN, "Math.sqrt()"),
            Pair.<Object, String>of(Double.NaN, "Math.sqrt('abc')"),
            Pair.<Object, String>of(new BigDecimal("3"), "Math.sqrt(9)"),
            Pair.<Object, String>of(new BigDecimal("1.4142135623730950488"), "Math.sqrt(2)"),
            Pair.<Object, String>of(new BigDecimal("1"), "Math.sqrt(1)"),
            Pair.<Object, String>of(new BigDecimal("0"), "Math.sqrt(0)"),
            Pair.<Object, String>of(Double.NaN, "Math.sqrt(-1)"),

            Pair.<Object, String>of(Double.NaN, "Math.trunc('foo')"),
            Pair.<Object, String>of(Double.NaN, "Math.trunc()"),
            Pair.<Object, String>of(new BigInteger("1"), "Math.trunc(1.2)"),
            Pair.<Object, String>of(new BigInteger("-1"), "Math.trunc(-1.2)")

    );

    @Test
    public void testBasic() {
        for (Pair<Object, String> pair : BASIC_TESTS) {
            assertEquals(pair.toString(), pair.getLeft(), eval(pair.getRight()));
        }
    }

    @Test
    public void testFloor1() {
        Number number = eval("Math.floor((Math.random()*100)+1)");
        assertTrue(number.floatValue() > 0);
    }

    @Test
    public void testFloorWithBigNumber() {
        Number number = eval("Math.floor(12345678901234567890123456789012345678901234567890.1)");
        assertEquals(new BigInteger("12345678901234567890123456789012345678901234567890"), number);
    }

    @Test
    public void testFloorAndConcat() {
        assertEquals("FRED_45.json", eval("'FRED_' + Math.floor(45.6537) + '.json'"));
    }

    @Test
    public void testRandom() {
        Number number = eval("Math.random()");
        assertTrue(number.floatValue() > 0);
    }

    @Test
    public void testUUID() {
        String uuid = eval("Math.randomUUID()");
        assertNotNull(uuid);
    }
}