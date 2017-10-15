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
import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript array reduce method.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ReduceTest extends ExpressionTest {
    @Test
    public void basic() {
        assertEquals(BigDecimal.valueOf(6), eval(
                "[1, 2, 3].reduce((prev, curr) => prev + curr, 0)"));
    }

    @Test
    public void flatten() {
        assertEquals(Arrays.asList(BigInteger.valueOf(0), BigInteger.valueOf(1),
                BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4),
                BigInteger.valueOf(5)), eval(
                "[[0, 1], [2, 3], [4, 5]].reduce((a, b) => a.concat(b), [])"
        ));
    }

    @Test(expected = SnapDataException.class)
    public void emptyWithoutInitial() {
        eval("[].reduce(x => x)");
    }

    @Test
    public void emptyWithInitial() {
        assertEquals(BigInteger.valueOf(1), eval("[].reduce(x => x, 1)"));
    }

    @Test
    public void oneValue() {
        assertEquals(BigInteger.valueOf(1), eval("[1].reduce((p, c, i) => p / i)"));
    }

    @Test
    public void oneValueWithInitial() {
        assertEquals(BigDecimal.valueOf(0), eval("[1].reduce((x, y, i) => (x + y) * i, 2)"));
    }
}