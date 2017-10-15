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

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Tests for CharCodeAt.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class CharCodeAtTest extends ExpressionTest {
    @Test
    public void testCharCodeAt() {
        assertEquals(new BigInteger("65"), eval("\"ABC\".charCodeAt(0)"));
    }

    @Test
    public void testCharCodeAtFloat() {
        assertEquals(new BigInteger("66"), eval("\"ABC\".charCodeAt(1.6)"));
    }

    @Test
    public void testCharCodeAtStringIndex() throws Exception {
        assertEquals(new BigInteger("65"), eval("\"ABC\".charCodeAt('abc')"));
    }

    @Test
    public void testCharCodeAtIndexOutOfBounds() {
        assertEquals((Object) Double.NaN, eval("\"ABC\".charCodeAt(100)"));
    }
}
