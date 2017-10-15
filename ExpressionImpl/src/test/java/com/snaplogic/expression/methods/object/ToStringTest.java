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

package com.snaplogic.expression.methods.object;

import com.snaplogic.expression.ExpressionTest;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ToString}.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ToStringTest extends ExpressionTest {
    private static final String MSG = "Hello, World!";

    private static final List<Pair<String, Object>> OBJECTS_TO_TEST = Arrays.asList(
            Pair.of(MSG, (Object) MSG.getBytes(StandardCharsets.UTF_8)),
            Pair.of("1234", (Object) new BigInteger("1234")),
            Pair.of("1234.56", (Object) new BigDecimal("1234.56")),
            Pair.of("1234", (Object) 1234),
            Pair.of("1234.5", (Object) 1234.5),
            Pair.of("12", (Object) Short.valueOf((short) 12)),
            Pair.of("123456789", (Object) 123456789L),
            Pair.of("1234.56", (Object) 1234.56f)
    );

    @Test
    public void testToString() {
        for (Pair<String, Object> pair : OBJECTS_TO_TEST) {
            assertEquals(pair.getLeft(), eval("$.toString()", pair.getRight()));
        }
    }

    @Test
    public void testToStringRadix() {
        assertEquals("55", eval("$.toString(16)", 0x55));
        assertEquals("c", eval("$.toString(16)", 12.34));
        assertEquals("-c", eval("$.toString(16)", -12));
        assertEquals("12.34", eval("$.toString(10)", 12.34));
        assertEquals("NaN", eval("$.toString(16)", Double.NaN));
        assertEquals("Infinity", eval("$.toString(16)", Double.POSITIVE_INFINITY));
        assertEquals("Infinity", eval("$.toString(16)", Double.NEGATIVE_INFINITY));
        assertEquals("21i3v9", eval("$.toString(36)", 123456789));
        assertEquals("110", eval("$.toString(2)", 6));
    }

    @Test(expected = SnapDataException.class)
    public void testToStringWithBadRadix1() {
        eval("(1).toString(1)");
    }

    @Test(expected = SnapDataException.class)
    public void testToStringWithBadRadix2() {
        eval("(1).toString(37)");
    }

    @Test(expected = SnapDataException.class)
    public void testToStringWithBadRadixString() {
        eval("(1).toString('foo')");
    }

    @Test
    public void testDateToString() {
        assertEquals("2013-05-25T12:00:00.000Z",
                eval("Date.parse('2013-05-25 12:00:00Z').toString()"));
    }

    @Test
    public void testNaNToString() {
        assertEquals("NaN", eval("NaN.toString()"));
    }

    @Test
    public void testInfinityToString() {
        assertEquals("Infinity", eval("Infinity.toString()"));
    }
}
