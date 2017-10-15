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
 * Boolean tests for expression language.
 *
 * @author jinloes
 */
public class BooleanTest extends ExpressionTest {

    @Test
    public void testTrue() {
        assertTrue((Boolean) eval("true"));
    }

    @Test
    public void testFalse() {
        assertFalse((Boolean) eval("false"));
    }

    @Test
    public void complexBoolean() {
        assertTrue((Boolean) eval("true && ( false || true ) && 2 > 1 && !false || " +
                "10 != 10 && ( 10 == 10 ) && 5 + 6 > 10"));
    }
}
