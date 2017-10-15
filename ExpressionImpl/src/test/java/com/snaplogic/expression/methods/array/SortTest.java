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
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript array sort method.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class SortTest extends ExpressionTest {
    @Test
    public void basic() {
        assertEquals(Arrays.asList("abc", "def", "ghi"), eval("['ghi', 'abc', 'def'].sort()"));
    }

    @Test
    public void nonString() {
        assertEquals(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(10), BigInteger
                        .valueOf(2)), eval("[1, 2, 10].sort()"));
    }

    @Test
    public void callback() {
        assertEquals(Arrays.asList("ghi", "def", "abc"), eval(
                "['ghi', 'abc', 'def'].sort((x, y) => -x.localeCompare(y))"));
    }

    @Test
    public void compareNumbers() {
        assertEquals(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger
                .valueOf(10)), eval("[1, 2, 10].sort((a, b) => a - b)"));
    }
}
