/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.api.SnapException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Tests for function call syntax.
 *
 * @author tstack
 */
public class FunctionCallTest extends ExpressionTest {
    @Test
    public void testSpread() {
        assertEquals(BigDecimal.ONE, eval("Math.min(1, ...[2, 3])"));
    }

    @Test
    public void testSpreadNumber() {
        assertEquals(BigDecimal.ONE, eval("Math.min(3, ...1)"));
    }

    @Test(expected = SnapException.class)
    public void missingValue() {
        assertEquals(BigDecimal.ONE, eval("Math.min(3, ...)"));
    }
}
