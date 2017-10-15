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

import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript number forms.
 *
 * @author tstack
 */
public class NumberTest extends ExpressionTest {
    @Test
    public void testZero() {
        assertEquals(BigInteger.valueOf(0), eval("0"));
    }

    @Test
    public void testBaseTen() {
        assertEquals(BigInteger.valueOf(109), eval("109"));
    }

    @Test
    public void testHexNumber() {
        assertEquals(BigInteger.valueOf(33), eval("0x21"));
    }

    @Test
    public void testOctNumber() {
        assertEquals(BigInteger.valueOf(15), eval("017"));
    }

    @Test
    public void testFloatNumber() {
        assertEquals(new BigDecimal("101.22349"), eval("101.22349"));
    }

    @Test
    public void testFloatZero() {
        assertEquals(new BigDecimal("0.0"), eval("0.0"));
    }

    @Test
    public void testIntWithExponent() {
        assertEquals(new BigDecimal("1E+2"), eval("1e+2"));
    }

    @Test
    public void testIntWithNegExponent() {
        assertEquals(new BigDecimal("0.01"), eval("1e-2"));
    }

    @Test
    public void testFloatWithExponent() {
        assertEquals(new BigDecimal("1.2E+2"), eval("1.2e+2"));
    }

    @Test
    public void testFloatWithNegExponent() {
        assertEquals(new BigDecimal("0.012"), eval("1.2e-2"));
    }
}
