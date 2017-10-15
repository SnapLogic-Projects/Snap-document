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

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for trim.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class TrimTest extends ExpressionTest {
    @Test
    public void testTrim() {
        assertEquals("SnapLogic", eval("'    SnapLogic      '.trim()"));
    }

    @Test
    public void testTrimLeft() {
        assertEquals("SnapLogic    ", eval("'    SnapLogic    '.trimLeft()"));
    }

    @Test
    public void testTrimRight() {
        assertEquals("     SnapLogic", eval("'     SnapLogic       '.trimRight()"));
    }

    @Test(expected = SnapDataException.class)
    public void testTrimTypo() {
        assertEquals("     SnapLogic", eval("'     SnapLogic       '.tram()"));
    }
}
