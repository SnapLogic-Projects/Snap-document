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

package com.snaplogic.expression.methods.number;

import com.snaplogic.expression.ExpressionTest;
import com.snaplogic.snap.api.SnapDataException;
import org.antlr.v4.runtime.misc.Triple;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the methods on Number objects.
 *
 * @author tstack
 */
public class NumberMethodTest extends ExpressionTest {
    private static final List<Triple<String, Object, String>> EXP_TESTS = Arrays.asList(
            new Triple<>("3e+0", (Object) 3, ""),
            new Triple<>("3.14e+0", (Object) 3.14, ""),
            new Triple<>("3.14e+2", (Object) 314, ""),
            new Triple<>("3.14e+5", (Object) 314159, "2"),
            new Triple<>("3.14e-3", (Object) 0.00314159, "2")
    );
    private static final List<Triple<String, Object, String>> FIXED_TESTS = Arrays.asList(
            new Triple<>("3.14", (Object) 3.14159, "2"),
            new Triple<>("1234.57", (Object) 1234.5678, "2"),
            new Triple<>("3", (Object) 3.14159, ""),
            new Triple<>("3", (Object) 3.14159, "0"),
            new Triple<>("3.1400", (Object) 3.14, "4"),
            new Triple<>("3.00", (Object) 3, "2"),
            new Triple<>("0.00", (Object) 0.000023, "2"),
            new Triple<>("NaN", (Object) Double.NaN, "2")
    );
    private static final List<Pair<String, String>> LIT_FIXED_TESTS = Arrays.asList(
            Pair.of("123000000000000000000.00", "(1.23e+20).toFixed(2)"),
            Pair.of("0.00", "(1.23e-10).toFixed(2)"),
            Pair.of("2.3", "2.34.toFixed(1)"),
            Pair.of("-2.3", "(-2.34).toFixed(1)")
    );
    private static final List<Triple<String, Object, String>> PRECISION_TESTS = Arrays.asList(
            new Triple<>("1234.5678", (Object) 1234.5678, ""),
            new Triple<>("1e+3", (Object) 1234.5678, "1"),
            new Triple<>("1.2e+3", (Object) 1234.5678, "2"),
            new Triple<>("1.23e+3", (Object) 1234.5678, "3"),
            new Triple<>("1235", (Object) 1234.5678, "4"),
            new Triple<>("1234.6", (Object) 1234.5678, "5"),
            new Triple<>("1234.57", (Object) 1234.5678, "6"),
            new Triple<>("1234.568", (Object) 1234.5678, "7"),
            new Triple<>("1234.5678", (Object) 1234.5678, "8"),
            new Triple<>("1234.56780", (Object) 1234.5678, "9")
    );

    @Test
    public void testToExponential() {
        for (Triple<String, Object, String> triple : EXP_TESTS) {
            assertEquals(triple.a, eval(String.format("$.toExponential(%s)", triple.c),
                    triple.b));
        }
    }

    @Test(expected = SnapDataException.class)
    public void testBadMethod() {
        eval("$.badMethod()", 3);
    }

    @Test(expected = SnapDataException.class)
    public void testInvalidFraction() {
        eval("$.toExponential(-1)", 3);
    }

    @Test
    public void testToFixed() {
        for (Triple<String, Object, String> triple : FIXED_TESTS) {
            assertEquals(triple.a, eval(String.format("$.toFixed(%s)", triple.c),
                    triple.b));
        }
    }

    @Test
    public void testLiteralToFixed() {
        for (Pair<String, String> pair : LIT_FIXED_TESTS) {
            assertEquals(pair.getLeft(), eval(pair.getRight()));
        }
    }

    @Test(expected = SnapDataException.class)
    public void testInvalidDigits() {
        eval("$.toFixed(-1)", 3);
    }

    @Test
    public void testToPrecision() {
        for (Triple<String, Object, String> triple : PRECISION_TESTS) {
            assertEquals(triple.a, eval(String.format("$.toPrecision(%s)", triple.c),
                    triple.b));
        }
    }
}
