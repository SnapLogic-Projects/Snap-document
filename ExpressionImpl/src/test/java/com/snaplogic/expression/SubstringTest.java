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
 * Tests for substring method.
 *
 * @author jinloes
 */
public class SubstringTest extends ExpressionTest {
    private static final String TEST_STR = "'SnapLogic'";

    @Test
    public void testSubstring() {
        assertEquals("Sna", eval(TEST_STR + ".substring(0, 3)"));
    }

    @Test
    public void testSubstringInverted() {
        assertEquals("Sna", eval(TEST_STR + ".substring(3, 0)"));
    }

    @Test
    public void testSubstringIndexOutOfBounds() {
        assertEquals("SnapLogic", eval(TEST_STR + ".substring(0, 10)"));
    }

    @Test
    public void testSubstringStringIdx() {
        assertEquals("SnapLogic", eval(TEST_STR + ".substring('abc')"));
    }

    @Test
    public void testSubstringStringEndIdx() {
        assertEquals("Sn", eval(TEST_STR + ".substring(2, 'abc')"));
    }
}
