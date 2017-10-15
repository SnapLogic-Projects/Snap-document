/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
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
 * Test the isNaN() global function.
 *
 * @author tstack
 */
public class IsNaNTest extends ExpressionTest {
    private static final String[] EVALS_TO_TRUE = {
            "isNaN(NaN)",
            "isNaN({})",
            "isNaN('foo')",
    };
    private static final String[] EVALS_TO_FALSE = {
            "isNaN(true)",
            "isNaN(null)",
            "isNaN(37)",
            "isNaN('37')",
            "isNaN('37.37')",
    };

    @Test
    public void testIsNaN() {
        for (String expr : EVALS_TO_TRUE) {
            assertTrue(expr, (boolean) eval(expr));
        }
        for (String expr : EVALS_TO_FALSE) {
            assertFalse(expr, (boolean) eval(expr));
        }
    }
}
