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
 * Tests for javascript array filter method.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class FilterTest extends ExpressionTest {
    @Test
    public void basic() {
        assertEquals(Arrays.asList(BigInteger.valueOf(3)), eval(
                "[1, 2, 3].filter(x => x > 2)"));
    }

    @Test
    public void indexArg() {
        assertEquals(Arrays.asList(BigInteger.valueOf(2)), eval(
                "[1, 2, 3].filter((elem, index, array) => index == 1)"
        ));
    }

    @Test
    public void outOfOrder() {
        assertEquals(Arrays.asList(BigInteger.valueOf(2)), eval(
                "[1, 3, 2].filter((elem, index, array) => index == 0 ? false : array[index" +
                        " - 1] > elem)"
        ));
    }

    @Test
    public void nonBool() {
        assertEquals(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2)), eval(
                "[1, 2].filter(x => 'true')"));
    }
}
