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

package com.snaplogic.expression;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Test for the conditional operator (exp ? expTrue : expFalse).
 *
 * @author tstack
 */
public class ConditionalTest extends ExpressionTest {
    @Test
    public void testBasicConditionalTrue() {
        assertEquals(BigInteger.ONE, eval("true ? 1 : 'bad'"));
    }

    @Test
    public void testBasicConditionalFalse() {
        assertEquals(BigInteger.ONE, eval("false ? 'bad' : 1"));
    }

    @Test
    public void testBasicConditionalEmptyString() {
        assertEquals(BigInteger.ONE, eval("'' ? 'bad' : 1"));
    }

    @Test
    public void testBasicConditionalEmptyArray() {
        assertEquals(BigInteger.ONE, eval("[] ? 1 : 'bad'"));
    }

    @Test
    public void testBasicConditionalZero() {
        assertEquals(BigInteger.ONE, eval("0 ? 'bad' : 1"));
    }

    @Test
    public void testBasicConditionalNonZero() {
        assertEquals(BigInteger.ONE, eval("1 ? 1 : 'bad'"));
    }

    @Test
    public void testBasicConditionalZeroFloat() {
        assertEquals(BigInteger.ONE, eval("0.0 ? 'bad' : 1"));
    }

    @Test
    public void testBasicConditionalNonZeroFloat() {
        assertEquals(BigInteger.ONE, eval("1.0 ? 1 : 'bad'"));
    }

    @Test
    public void testBasicConditionalNull() {
        assertEquals(BigInteger.ONE, eval("null ? 'bad' : 1"));
    }

    @Test
    public void testBasicConditionalNaN() {
        assertEquals(BigInteger.ONE, eval("NaN ? 'bad' : 1"));
    }

    @Test
    public void testBasicConditionalInfinity() {
        assertEquals(BigInteger.ONE, eval("Infinity ? 1 : 'bad'"));
    }

    @Test
    public void testBasicConditionalWithExpr() {
        assertEquals(BigInteger.ONE, eval("1 > 2 ? 'bad' : 1"));
    }

    @Test
    public void testConditionalEvaluation() {
        assertEquals(BigInteger.ONE, eval("true ? 1 : $foo"));
    }
}
