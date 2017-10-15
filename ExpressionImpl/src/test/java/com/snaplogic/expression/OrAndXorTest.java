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

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Tests for or, and, and xor.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class OrAndXorTest extends ExpressionTest {

    @Test
    public void testTrueAndTrue() {
        assertTrue((Boolean) eval("true && true"));
    }

    @Test
    public void testTrueAndFalse() {
        assertFalse((Boolean) eval("true && false"));
    }

    @Test
    public void testFalseAndFalse() {
        assertFalse((Boolean) eval("false && false"));
    }

    @Test
    public void testFalseAndTrueAndFalse() {
        assertFalse((Boolean) eval("true && false && true"));
    }

    @Test
    public void testStringAndFalse() {
        assertFalse((Boolean) eval(" \"test\" && false"));
    }

    @Test
    public void testStringAndTrue() {
        assertTrue((Boolean) eval(" \"test\" && true"));
    }

    @Test
    public void testEmptyStringAndTrue() {
        assertEquals("", eval(" \"\" && true"));
    }

    @Test
    public void testTrueAndString() {
        assertEquals("test", eval("true && 'test'"));
    }

    @Test
    public void testTrueAndInteger() {
        assertEquals(new BigInteger("3"), eval("true && 3"));
    }

    @Test
    public void testChainedAnd() {
        assertFalse((Boolean) eval("true && true && true && true && true && false"));
    }

    @Test
    public void testChainedAndStr() {
        assertEquals("test", eval("true && true && true && true && true && \"test\""));
    }

    @Test
    public void testChainedAndStrFalse() {
        assertFalse((Boolean) eval("true && true && true && false && true && \"\""));
    }

    @Test
    public void testTrueOrFalse() {
        assertTrue((Boolean) eval("true||false"));
    }

    @Test
    public void testFalseOrTrueOrFalse() {
        assertTrue((Boolean) eval("false||true||false"));
    }

    @Test
    public void testFalseOrFalse() {
        assertFalse((Boolean) eval("false || false"));
    }

    @Test
    public void testTrueOrTrue() {
        assertTrue((Boolean) eval("true || true"));
    }

    @Test
    public void testOrString() {
        assertEquals("test", eval("\"test\" || true"));
    }

    @Test
    public void testOrInteger() {
        assertEquals(new BigInteger("3"), eval("3 || false"));
    }

    @Test
    public void testOrTrueString() {
        assertTrue((Boolean) eval("true || \"true\""));
    }

    @Test
    public void testOrEmptyString() {
        assertTrue((Boolean) eval("\"\" || true"));
    }

    @Test
    public void testOrEmptyStringFalse() {
        assertFalse((Boolean) eval("\"\" || false"));
    }

    @Test
    public void testOrEmptyStringString() {
        assertEquals("test", eval("\"\"|| \"test\""));
    }

    @Test
    public void testOrStrings() {
        assertEquals("abc123", eval("\"abc123\"|| \"test\""));
    }

    @Test
    public void testChainedOr() {
        assertTrue((Boolean) eval("false || false || false || false || false || true"));
    }

    @Test
    public void testChainedOrStrings() {
        assertEquals("test", eval("false || false || false || false || \"\" || \"test\""));
    }

    @Test
    public void testXorTrueTrue() {
        assertFalse((Boolean) eval("true ^ true"));
    }

    @Test
    public void testXorTrueFalse() {
        assertTrue((Boolean) eval("true ^ false"));
    }

    @Test
    public void testXorFalseTrue() {
        assertTrue((Boolean) eval("false ^ true"));
    }

    @Test
    public void testXorFalseFalse() {
        assertFalse((Boolean) eval("false ^ false"));
    }

    @Test
    public void testLogicalOrShortCircuit() {
        assertEquals(BigInteger.valueOf(42), eval("0 || 42 || $foo"));
    }

    @Test
    public void testLogicalOrShortCircuitWithParens() {
        assertEquals(BigInteger.valueOf(42), eval("0 || 42 || ($foo || $bar)"));
    }

    @Test
    public void testLogicalAndShortCircuitWithNull() {
        assertNull(eval("42 && null && $foo"));
    }

    @Test
    public void testLogicalAndShortCircuitWithZero() {
        assertEquals(BigInteger.ZERO, eval("42 && 0 && $foo"));
    }

    @Test(expected = SnapDataException.class)
    public void testXorIllegalFirstArgument() {
        eval("\"false\" ^ true");
    }

    @Test(expected = SnapDataException.class)
    public void testXorIllegalSecondArgument() {
        eval("true ^ 123");
    }

    @Test
    public void testChainedXor() {
        assertFalse((Boolean) eval("true ^ false ^ true"));
    }
}
