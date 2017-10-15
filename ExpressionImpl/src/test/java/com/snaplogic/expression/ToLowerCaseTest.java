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
 * Tests for string toLowerCase method.
 *
 * @author jinloes
 */
public class ToLowerCaseTest extends ExpressionTest {
    private static final String TEST_STR = "'SNAPLOGIC'";

    @Test
    public void testToLower() {
        assertEquals("snaplogic", eval(TEST_STR + ".toLowerCase()"));
    }
}
