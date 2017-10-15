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
 * Tests for lastIndexOf method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class LastIndexOfTest extends ExpressionTest {
    private static final String TEST_STR = "'SnapLogic data integration.'";

    @Test
    public void testLastIndexOf() {
        assertEquals(new BigInteger("23"), eval(TEST_STR + ".lastIndexOf('i')"));
    }

    @Test
    public void testLastIndexOfNotFound() {
        assertEquals(new BigInteger("-1"), eval(TEST_STR + ".lastIndexOf('x')"));
    }

    @Test
    public void testLastIndexOfIndex() {
        assertEquals(new BigInteger("15"), eval(TEST_STR + ".lastIndexOf('i', '22')"));
    }

    @Test
    public void testLastIndexOfIndexNotFound() {
        assertEquals(new BigInteger("-1"), eval(TEST_STR + ".lastIndexOf('i', 0)"));
    }

    @Test
    public void testLastIndexOfStringArg() {
        assertEquals(new BigInteger("23"), eval(TEST_STR + ".lastIndexOf('i', 'abc')"));
    }
}
