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

import com.snaplogic.api.ExecutionException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Tests for negating a number and boolean.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class MinusNotTest extends ExpressionTest {

    @Test
    public void testUnaryNotTrue() {
        assertFalse((Boolean) eval("!true"));
    }

    @Test
    public void testUnaryNotFalse() {
        assertTrue((Boolean) eval("!false"));
    }

    @Test
    public void testNotNumber() {
        assertFalse((Boolean) eval("!5"));
    }

    @Test
    public void testNotZero() {
        assertTrue((Boolean) eval("!0"));
    }

    @Test
    public void testNotNull() {
        assertTrue((Boolean) eval("!null"));
    }

    @Test
    public void testNotString() {
        assertFalse((Boolean) eval("!\"test\""));
    }

    @Test
    public void testNotEmptyString() {
        assertTrue((Boolean) eval("!\"\""));
    }

    @Test(expected = ExecutionException.class)
    public void testNotFalseSyntaxError() {
        eval("true!!false");
    }

    @Test
    public void testNegatePositive() {
        assertEquals(new BigDecimal("-10"), eval("-10"));
    }

    @Test
    public void testNegateNegative() {
        assertEquals(new BigDecimal("10"), eval("-( -10 )"));
    }

    @Test
    public void testNegateIllegalArgument() throws Exception {
        assertEquals((Object) Double.NaN, eval("-{}"));
    }

    @Test
    public void testNegateNaN() throws Exception {
        eval("-NaN");
    }

    @Test
    public void testNegateInfinity() throws Exception {
        eval("-Infinity");
    }

    @Test
    public void testPositiveNumber() throws Exception {
        assertEquals(new BigDecimal("9"), eval("+9"));
    }

    @Test
    public void testPositiveString() throws Exception {
        assertEquals((Object) Double.NaN, eval("+'foo'"));
    }

    @Test
    public void testPositiveNumberString() throws Exception {
        assertEquals(new BigDecimal(5), eval("+'5'"));
    }
}
