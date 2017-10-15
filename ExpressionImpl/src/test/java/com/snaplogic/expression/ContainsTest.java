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
 * Tests for contains method.
 *
 * @author jinloes
 */
public class ContainsTest extends ExpressionTest {
    private static final String TEST_STR = "'SnapLogic data integration.'";

    @Test
    public void testContainsTrue() {
        assertTrue((Boolean) eval(TEST_STR + ".contains(\"data\")"));
    }

    @Test
    public void testContainsFalse() {
        assertFalse((Boolean) eval(TEST_STR + ".contains(\"server\")"));
    }

    @Test
    public void testContainsIndexTrue() {
        assertTrue((Boolean) eval(TEST_STR + ".contains(\"Logic\", 1)"));
    }

    @Test
    public void testContainsIndexFalse() {
        assertFalse((Boolean) eval(TEST_STR + ".contains(\"Snap\", 1)"));
    }
}
