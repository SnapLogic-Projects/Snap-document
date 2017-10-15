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

import static org.junit.Assert.assertEquals;

/**
 * Tests for SubStr method.
 *
 * @author jinloes
 */
public class SubStrTest extends ExpressionTest {
    private static final String TEST_STR = "\"abcdefghij\"";

    @Test
    public void testSubStr() {
        assertEquals("bcdefghij", eval(TEST_STR + ".substr(1)"));
    }

    @Test
    public void testSubStrWithEnd() {
        assertEquals("bc", eval(TEST_STR + ".substr(1, 2)"));
    }

    @Test
    public void testSubStrStringIndex() {
        assertEquals("abcdefghij", eval(TEST_STR + ".substr('abc')"));
    }

    @Test
    public void testSubStrStringEndIndex() {
        assertEquals("", eval(TEST_STR + ".substr(2, 'abc')"));
    }

    @Test
    public void testSubStrNegativeIdxOutOfBounds() {
        assertEquals("ab", eval(TEST_STR + ".substr(-20, 2)"));
    }

    @Test
    public void testSubStrIndexOutOfBounds() {
        assertEquals("", eval(TEST_STR + ".substr(20, 2)"));
    }

    @Test
    public void testSubStrNegativeIndex() {
        assertEquals("hi", eval(TEST_STR + ".substr(-3, 2)"));
    }
}
