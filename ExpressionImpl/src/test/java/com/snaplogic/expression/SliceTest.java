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
 * Tests for slice method.
 *
 * @author jinloes
 */
public class SliceTest extends ExpressionTest {
    private static final String TEST_STR = "'The morning is upon us.'";

    @Test
    public void testSlice() {
        assertEquals("morning is upon u", eval(TEST_STR + ".slice(4, -2)"));
    }

    @Test
    public void testSliceStrNumIndex() {
        assertEquals("us", eval(TEST_STR + ".slice('-3', '-1')"));
    }

    @Test
    public void testSliceStrIndex() {
        assertEquals("", eval(TEST_STR + ".slice('-3', 'abc')"));
    }

    @Test
    public void testSliceStrIndex2() {
        assertEquals("The morning is upon us.", eval(TEST_STR + ".slice('abc')"));
    }
}
