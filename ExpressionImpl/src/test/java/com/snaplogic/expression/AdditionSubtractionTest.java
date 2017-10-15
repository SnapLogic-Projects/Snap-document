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

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.Assert.assertEquals;

/**
 * Tests for addition operator in the expression language.
 *
 * @author jinloes
 */
public class AdditionSubtractionTest extends ExpressionTest {
    private static final String ADD_FMT = "%s+%s";
    private static final String SUB_FMT = "%s-%s";
    private static final BigDecimal OP1 = new BigDecimal("5.98327439287432");
    private static final BigDecimal OP2 = new BigDecimal("1283897821037.213123123122131");

    @Test
    public void testAddition() {
        assertEquals(new BigDecimal("15"), eval("5 + 10"));
    }

    @Test
    public void testAdditionDecimal() {
        assertEquals(OP1.add(OP2, MathContext.DECIMAL128),
                eval(String.format(ADD_FMT, OP1, OP2)));
    }

    @Test
    public void testAdditionFloatInt() {
        assertEquals(OP1.add(new BigDecimal(BigInteger.ONE), MathContext.DECIMAL128),
                eval(String.format(ADD_FMT, OP1, BigDecimal.ONE)));
    }

    @Test
    public void testAdditionIntFloat() {
        assertEquals(OP1.add(new BigDecimal(BigInteger.ONE), MathContext.DECIMAL128),
                eval(String.format(ADD_FMT, BigDecimal.ONE, OP1)));
    }

    @Test
    public void testConcat() {
        assertEquals("510", eval("'5' + 10"));
    }

    @Test
    public void testConcatMulti() {
        assertEquals("5101099", eval("'5' + 10 + 10 + 99"));
    }

    @Test
    public void testMixedOperators() {
        assertEquals(new BigDecimal("223"), eval("5 + 10 + 10 + 99 * 2"));
    }


    @Test
    public void testConcatMultiComplex() {
        assertEquals("5119", eval("'5' + (10 + (10 + 99))"));
    }

    @Test
    public void testAddIllegalFirstArgument() {
        assertEquals(BigDecimal.valueOf(7), eval("true + 6"));
    }

    @Test
    public void testAddDecimalIllegalSecondArgument() {
        assertEquals(BigDecimal.valueOf(6), eval("5 + true"));
    }

    @Test
    public void testSubtraction() {
        assertEquals(new BigDecimal("-5"), eval("5 - 10"));
    }

    @Test
    public void testSubtractionDecimal() throws Exception {
        assertEquals(OP1.subtract(OP2, MathContext.DECIMAL128),
                eval(String.format(SUB_FMT, OP1, OP2)));
    }

    @Test
    public void testSubtractionStrings() throws Exception {
        assertEquals(OP1.subtract(OP2, MathContext.DECIMAL128),
                eval(String.format(SUB_FMT, "'" + OP1 + "'", "'" + OP2 + "'")));
    }

    @Test
    public void testSubtractionNonNumericFirstArgument() {
        assertEquals(BigDecimal.valueOf(-9), eval("true - 10"));
    }

    @Test
    public void testSubtractionWithString() {
        assertEquals(BigDecimal.valueOf(-9), eval("'1' - 10"));
    }

    @Test
    public void testAdditionWithString() {
        assertEquals("110", eval("'1' + 10"));
    }

    @Test
    public void testSubtractionIntString() {
        assertEquals((Object) Double.NaN, eval("5 - \"abc\""));
    }

    @Test
    public void testSubtractionFloatString() {
        assertEquals((Object) Double.NaN, eval("5.5 - \"abc\""));
    }

    @Test
    public void testComplexAdd() {
        assertEquals(new BigDecimal("30"), eval("( 5 + 3 ) * ( 2 + 1 ) + 2 * 3"));
    }

    @Test
    public void testStringAdd() {
        assertEquals("onetwo", eval("'one' + 'two'"));
    }

    @Test
    public void testStringAddNull() {
        assertEquals("strnull", eval("'str' + null"));
    }

    @Test
    public void testIntPlusString() {
        assertEquals("1two", eval("1 + 'two'"));
    }
}
