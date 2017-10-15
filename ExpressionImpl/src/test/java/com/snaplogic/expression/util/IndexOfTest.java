/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.util;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.ExpressionTest;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for indexOf on the given string.
 *
 * @author hpatel
 */
@SuppressWarnings("HardCodedStringLiteral")
public class IndexOfTest extends ExpressionTest {

    BigDecimal minusOne = BigDecimal.ONE.negate();

    @Test
    public void testIndexOf() {
        Object data = ImmutableMap.<String, Object>of("companyInfo",
                ImmutableMap.of("name", "SnapLogic"));
        assertEquals(BigDecimal.valueOf(4), eval("$companyInfo.name.indexOf('Logic')", data));
    }

    @Test
    public void testIndexOfLiteral() {
        assertEquals(BigDecimal.valueOf(3), eval("'abc123'.indexOf('123')"));
    }

    @Test
    public void testIndexOfEquality() {
        Object data = ImmutableMap.<String, Object>of("companyInfo",
                ImmutableMap.of("name", "SnapLogic"));
        assertTrue((Boolean) eval("$companyInfo.name.indexOf('Logic') == " +
                "$companyInfo.name.indexOf('Logic')", data));
    }

    @Test
    public void testIndexOfNotFound() {
        String TEST_STR = "'SnapLogic data integration.'";
        assertEquals(minusOne, eval(TEST_STR + ".indexOf('x')"));
    }

    @Test
    public void testIndexOfStringArg() {
        String TEST_STR = "'SnapLogic data integration.'";
        assertEquals(BigDecimal.valueOf(7), eval(TEST_STR + ".indexOf('i')"));
    }

    @Test
    public void testIndexOfDifferentType() {
        assertEquals(BigDecimal.ZERO, eval("'true'.indexOf(true)"));
    }

    @Test
    public void testIndexOfEmpty() {
        assertEquals(BigDecimal.valueOf(-1), eval("'value'.indexOf()"));
    }

    @Test
    public void testBooleanFalseIndexOf() {
        assertEquals(new BigDecimal("4"), eval("\"truefalse\".indexOf(false)"));
    }

    @Test
    public void testNullIndexOf() {
        assertEquals(new BigDecimal("5"), eval("\"true null false\".indexOf(null)"));
    }

    @Test
    public void testNumericIndexOf(){
        assertEquals(new BigDecimal("5"), eval("\"abc1234\".indexOf(3)"));
        assertEquals(minusOne, eval("\"abc1234\".indexOf(500)"));
        assertEquals(minusOne, eval("\"abc1234\".indexOf(-3)"));
    }

    @Test
    public void testStartPosIndexOf() {
        String TEST_STR = "'SnapLogic data integration.'";
        assertEquals(BigDecimal.valueOf(1), eval(TEST_STR + ".indexOf('n', 1)"));
        assertEquals(BigDecimal.valueOf(16), eval(TEST_STR + ".indexOf('n', 10)"));
        assertEquals(BigDecimal.valueOf(1), eval(TEST_STR + ".indexOf('n', -1)"));
        assertEquals(minusOne, eval(TEST_STR + ".indexOf('n', 2000)"));
    }
}