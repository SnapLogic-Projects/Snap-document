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
 * Tests for string toUpperCase method.
 *
 * @author jinloes
 */
public class ToUpperCaseTest extends ExpressionTest {
    private static final String TEST_STR = "'snaplogic'";

    @Test
    public void testToLower() {
        assertEquals("SNAPLOGIC", eval(TEST_STR + ".toUpperCase()"));
    }
}
