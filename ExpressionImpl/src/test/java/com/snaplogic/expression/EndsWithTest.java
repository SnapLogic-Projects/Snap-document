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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for string ends with method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class EndsWithTest extends ExpressionTest {
    private static final String TEST_STR = "'SnapLogic data integration.'";

    @Test
    public void testEndsWithTrue() {
        assertTrue((Boolean) eval(TEST_STR + ".endsWith('integration.')"));
    }

    @Test
    public void testEndsWithFalse() {
        assertFalse((Boolean) eval(TEST_STR + ".endsWith('integration')"));
    }

    @Test
    public void testEndsWithIndexTrue() {
        assertTrue((Boolean) eval(TEST_STR + ".endsWith('integration', '25')"));
    }

    @Test
    public void testEndsWithIndexFalse() {
        assertFalse((Boolean) eval(TEST_STR + ".endsWith('integration.', '25')"));
    }

    @Test
    public void testEndsWithMaxIndex() {
        assertTrue((Boolean) eval(TEST_STR + ".endsWith('integration.', 26)"));
    }

    @Test
    public void testEndsWithIndexOutOfBounds() {
        assertTrue((Boolean) eval(TEST_STR + ".endsWith('integration.', 100)"));
    }

    @Test
    public void testEndsWithStringIndex() {
        assertFalse((Boolean) eval(TEST_STR + ".endsWith('integration.', 'abc')"));
    }
}
