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
 * Tests for starts with method.
 *
 * @author jinloes
 */
public class StartsWithTest extends ExpressionTest {
    private static final String TEST_STR = "'SnapLogic data integration.'";

    @Test
    public void testStartsWithTrue() {
        assertTrue((Boolean) eval(TEST_STR + ".startsWith('Snap')"));
    }

    @Test
    public void testStartsWithFalse() {
        assertFalse((Boolean) eval(TEST_STR + ".startsWith('Logic')"));
    }

    @Test
    public void testStartsWithIndexTrue() {
        assertTrue((Boolean) eval(TEST_STR + ".startsWith('Logic', '4')"));
    }

    @Test
    public void testStartsWithIndexFalse() {
        assertFalse((Boolean) eval(TEST_STR + ".startsWith('Snap', '4')"));
    }

    @Test
    public void testEndsWithIndexOutOfBounds() {
        assertTrue((Boolean) eval(TEST_STR + ".startsWith('SnapLogic', 100)"));
    }

    @Test
    public void testStartsWithStringIndex() {
        assertTrue((Boolean) eval(TEST_STR + ".startsWith('Snap', 'abc')"));
    }
}
