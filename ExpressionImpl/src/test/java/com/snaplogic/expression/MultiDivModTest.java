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
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.Assert.assertEquals;

/**
 * Tests for multiplication, division, and modulus in expression language.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class MultiDivModTest extends ExpressionTest {
    private static final String MULTI_FMT = "%s*%s";
    private static final String DIVIDE_FMT = "%s/%s";
    private static final String MOD_FMT = "%s%%%s";
    private static final BigInteger INT1 = new BigInteger("5");
    private static final BigInteger INT2 = new BigInteger("12");
    private static final BigDecimal OP1 = new BigDecimal("5.98327439287432");
    private static final BigDecimal OP2 = new BigDecimal("1283897821037.213123123122131");
    private static final BigDecimal OP3 = new BigDecimal("0000000000.000000");
    private static final BigDecimal OP4 = new BigDecimal("100000.00001");

    @Test
    public void testMultiply() {
        assertEquals(new BigDecimal(INT1).multiply(new BigDecimal(INT2)),
                eval(String.format(MULTI_FMT, 5, 12)));
    }

    @Test
    public void testMultipleMultiply() {
        assertEquals(new BigDecimal(64), eval("2 * 2 * 2 * 2 * 2 * 2"));
    }

    @Test
    public void testMultiplyIntFloat() {
        Object data = ImmutableMap.<String, Object>of("num1", BigInteger.valueOf(2));
        assertEquals(new BigDecimal(2).multiply(new BigDecimal("4.4")),
                eval(String.format(MULTI_FMT, "$num1", "4.4"), data));
    }

    @Test
    public void testMultiplyFloatInt() {
        Object data = ImmutableMap.<String, Object>of("num1", BigDecimal.valueOf(4.4)
                , "num2", BigInteger.valueOf(2));
        assertEquals(new BigDecimal("4.4").multiply(new BigDecimal(2)),
                eval(String.format(MULTI_FMT, "$num1", "$num2"), data));
    }

    @Test
    public void testMultiplyDecimal() {
        assertEquals(OP1.multiply(OP2, MathContext.DECIMAL128),
                eval(String.format(MULTI_FMT, OP1, OP2)));
    }

    @Test
    public void testMultiplyStrings() {
        assertEquals(OP1.multiply(OP2, MathContext.DECIMAL128),
                eval(String.format(MULTI_FMT, "'" + OP1 + "'", "'" + OP2 + "'")));
    }

    @Test
    public void testMultiplyStringInt() {
        assertEquals((Object) Double.NaN, eval("'abc' * 6"));
    }

    @Test
    public void testMultiplyIntString() {
        assertEquals((Object) Double.NaN, eval("5 * 'abc'"));
    }

    @Test
    public void testMultiplyStringFloat() {
        assertEquals((Object) Double.NaN, eval("'abc' * 6.6"));
    }

    @Test
    public void testMultiplyFloatString() {
        assertEquals((Object) Double.NaN, eval("5.5 * 'abc'"));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigDecimal(INT2).divide(new BigDecimal(INT1)),
                eval(String.format(DIVIDE_FMT, 12, 5)));
    }

    @Test
    public void testDivideSmall() {
        assertEquals(new BigDecimal(1).divide(new BigDecimal(2)),
                eval(String.format(DIVIDE_FMT, 1, 2)));
    }

    @Test
    public void testDivideFloatInt() {
        Object data = ImmutableMap.<String, Object>of("num1", BigDecimal.valueOf(4.4),
                "num2", BigInteger.valueOf(2));
        assertEquals(BigDecimal.valueOf(4.4).divide(new BigDecimal(2)),
                eval(String.format(DIVIDE_FMT, "$num1", "$num2"), data));
    }

    @Test
    public void testDivideDecimal() {
        assertEquals(OP2.divide(OP1, MathContext.DECIMAL128),
                eval(String.format(DIVIDE_FMT, OP2, OP1)));
    }

    @Test
    public void testDivideStrings() {
        assertEquals(OP2.divide(OP1, MathContext.DECIMAL128),
                eval(String.format(DIVIDE_FMT, "'" + OP2 + "'", "'" + OP1 + "'")));
    }

    @Test
    public void testDivideStringInt() {
        assertEquals((Object) Double.NaN, eval(String.format(DIVIDE_FMT, "\"abc\"", 5)));
    }

    @Test
    public void testDivideIntString() {
        assertEquals((Object) Double.NaN, eval(String.format(DIVIDE_FMT, 12, "'abc'")));
    }

    @Test
    public void testDivideStringFloat() {
        assertEquals((Object) Double.NaN, eval(String.format(DIVIDE_FMT, "\"abc\"", 5.5)));
    }

    @Test
    public void testDivideFloatString() {
        assertEquals((Object) Double.NaN, eval(String.format(DIVIDE_FMT, 12.4, "'abc'")));
    }

    @Test
    public void testMod() {
        assertEquals(new BigDecimal(INT2.remainder(INT1)),
                eval(String.format(MOD_FMT, INT2, INT1)));
    }

    @Test
    public void testModDecimal() {
        assertEquals(OP2.remainder(OP1, MathContext.DECIMAL128),
                eval(String.format(MOD_FMT, OP2, OP1)));
    }

    @Test
    public void testModStrings() {
        assertEquals(OP2.remainder(OP1, MathContext.DECIMAL128),
                eval(String.format(MOD_FMT, "'" + OP2 + "'", "'" + OP1 + "'")));
    }

    @Test
    public void testModStringInt() {
        assertEquals((Object) Double.NaN, eval("'abc' % 5"));
    }

    @Test
    public void testModIntString() {
        assertEquals((Object) Double.NaN, eval("12 % 'abc'"));
    }

    @Test
    public void testDivideByZero() {
        assertEquals((Object) Double.POSITIVE_INFINITY, eval("1 / 0"));
    }

    @Test
    public void testDivideAmbiguity() {
        assertEquals(new BigDecimal("2"), eval("1 / 1 + 1 / 1"));
    }
}