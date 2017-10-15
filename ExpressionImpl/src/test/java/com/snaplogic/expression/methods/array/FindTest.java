/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods.array;

import com.snaplogic.expression.ExpressionTest;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for javascript array find method.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class FindTest extends ExpressionTest {
    @Test
    public void basic() {
        assertEquals(BigInteger.valueOf(2), eval("[1, 2].find(x => x == 2)"));
        assertEquals(BigInteger.valueOf(1), eval("[1, 2].findIndex(x => x == 2)"));
    }

    @Test
    public void missing() {
        assertNull(eval("[].find(x => true)"));
        assertEquals(BigInteger.valueOf(-1), eval("[].findIndex(x => true)"));
    }

    @Test
    public void nonBoolean() {
        assertEquals(BigInteger.ONE, eval("[1, 2].find(x => 'hello')"));
        assertEquals(BigInteger.ZERO, eval("[1, 2].findIndex(x => 'hello')"));
    }

    @Test
    public void outOfOrder() {
        assertEquals(BigInteger.valueOf(2),
                eval("[1, 3, 2].find((e, i, a) => i == 0 ? false : a[i - 1] > e)"));
        assertEquals(BigInteger.valueOf(2),
                eval("[1, 3, 2].findIndex((e, i, a) => i == 0 ? false : a[i - 1] > e)"));
    }
}
