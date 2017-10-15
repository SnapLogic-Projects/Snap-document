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

package com.snaplogic.expression.methods.array;

import com.snaplogic.expression.ExpressionTest;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript array lastIndexOf method.
 *
 * @author jinloes
 */
public class LastIndexOfTest extends ExpressionTest {
    @Test
    public void testLastIndexOf() {
        assertEquals(new BigInteger("2"),
                eval("['test1', 'test2', 'test3', 'test4'].lastIndexOf('test3')"));
    }

    @Test
    public void testLastIndexOfNotFound() {
        assertEquals(new BigInteger("-1"),
                eval("['test1', 'test2', 'test3', 'test4'].lastIndexOf('test6')"));
    }

    @Test
    public void testLastIndexOfRange() {
        assertEquals(new BigInteger("1"),
                eval("['test1', 'test2', 'test3', 'test4', 'test2'].lastIndexOf('test2', 3)"));
    }

    @Test
    public void testLastIndexOfRangeBounds() {
        assertEquals(new BigInteger("3"),
                eval("['test1', 'test2', 'test3', 'test4', 'test2'].lastIndexOf('test4', 3)"));
    }

    @Test
    public void testLastIndexOfRangeNotFound() {
        assertEquals(new BigInteger("-1"),
                eval("['test1', 'test2', 'test3', 'test4'].lastIndexOf('test4', 2)"));
    }

    @Test
    public void testLastIndexOfNegativeRange() {
        assertEquals(new BigInteger("4"),
                eval("['test1', 'test2', 'test3', 'test4', 'test2'].lastIndexOf('test2', -1)"));
    }
}
