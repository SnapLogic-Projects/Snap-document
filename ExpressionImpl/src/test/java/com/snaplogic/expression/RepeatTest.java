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

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for repeat string method.
 *
 * @author tstack
 */
public class RepeatTest extends ExpressionTest {
    @Test
    public void testRepeatBasics() {
        assertEquals("", eval("'abc'.repeat(0)"));
        assertEquals("abc", eval("'abc'.repeat(1)"));
        assertEquals("abcabc", eval("'abc'.repeat(2)"));
        assertEquals("abcabcabc", eval("'abc'.repeat(3.5)"));
    }

    @Test(expected = SnapDataException.class)
    public void testRepeatRangeError() {
        eval("'abc'.repeat(-1)");
    }

    @Test(expected = SnapDataException.class)
    public void testRepeatNaN() {
        eval("'abc'.repeat(1/0)");
    }
}
