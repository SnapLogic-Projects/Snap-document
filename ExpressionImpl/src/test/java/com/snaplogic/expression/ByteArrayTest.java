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

package com.snaplogic.expression;

import com.google.common.primitives.Bytes;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Tests for byte arrays.
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ByteArrayTest extends ExpressionTest {
    private static final byte[] BITS = {
            11,
            22,
            33,
            44
    };

    private static final byte[] BITS_WITH_REPEAT = {
            11,
            22,
            33,
            44,
            33,
            22,
            11
    };

    private static final byte[] EMPTY = new byte[0];

    private static final String MSG = "Hello, World!";

    @Test
    public void testLength() {
        assertEquals(BigInteger.valueOf(4), eval("$.length", BITS));
    }

    @Test
    public void testInstanceof() {
        assertTrue((Boolean) eval("$ instanceof Uint8Array", BITS));
    }

    @Test
    public void testOf() {
        assertArrayEquals(new byte[] {(byte) 0xff, 1, 2, 3}, (byte[]) eval(
                "Uint8Array.of(0xff, 1, 2, 3)"));
    }

    @Test
    public void testOfNonNumbers() {
        assertArrayEquals(new byte[] {0, (byte) 65000, 1}, (byte[]) eval(
                "Uint8Array.of('Hello', 65000, true)"));
    }

    @Test
    public void testConcat() {
        assertArrayEquals(Bytes.concat(BITS, BITS), (byte[]) eval("Uint8Array.of().concat($, $)",
                BITS));
    }

    @Test
    public void testConcatStrings() {
        assertArrayEquals(Bytes.concat(BITS, "Hello, World!".getBytes(StandardCharsets.UTF_8)),
                (byte[]) eval("Uint8Array.of().concat($, 'Hello, World!')", BITS));
    }

    @Test
    public void testSubarrayEmpty() {
        assertArrayEquals(new byte[0], (byte[]) eval("$.subarray(0, 0)", BITS));
    }

    @Test
    public void testSubarrayWithStart() {
        assertArrayEquals(new byte[] {22, 33, 44}, (byte[]) eval("$.subarray(1)", BITS));
    }

    @Test
    public void testSubarrayWithStartAndEnd() {
        assertArrayEquals(new byte[] {22, 33}, (byte[]) eval("$.subarray(1, 3)", BITS));
    }

    @Test
    public void testSubarrayWithInvalid() {
        assertArrayEquals(new byte[] {11, 22, 33}, (byte[]) eval("$.subarray(-1, 3)", BITS));
    }

    @Test
    public void testFalsey() {
        assertEquals(BigInteger.valueOf(42), eval("$ || 42", EMPTY));
    }

    @Test
    public void testTruthy() {
        assertArrayEquals(BITS, (byte[]) eval("$ || 42", BITS));
    }

    @Test
    public void testIndexOf() {
        assertEquals(BigInteger.valueOf(1), eval("$.indexOf(22)", BITS));
    }

    @Test
    public void testIndexOfWithOffset() {
        assertEquals(BigInteger.valueOf(-1), eval("$.indexOf(22, 2)", BITS));
    }

    @Test
    public void testIndexOfNotFound() {
        assertEquals(BigInteger.valueOf(-1), eval("$.indexOf(123)", BITS));
    }

    @Test
    public void testLastIndexOf() {
        assertEquals(BigInteger.valueOf(1), eval("$.lastIndexOf(22)", BITS));
    }

    @Test
    public void testLastIndexOfWithOffset() {
        assertEquals(BigInteger.valueOf(-1), eval("$.lastIndexOf(22, 0)", BITS));
    }

    @Test
    public void testLastIndexOfNotFound() {
        assertEquals(BigInteger.valueOf(-1), eval("$.lastIndexOf(123)", BITS));
    }

    @Test
    public void testLastIndexOfWithRepeats() {
        assertEquals(BigInteger.valueOf(5), eval("$.lastIndexOf(22)", BITS_WITH_REPEAT));
    }

    @Test
    public void testEquals() {
        assertTrue((Boolean) eval("$ == $", BITS));
    }

    @Test
    public void testNotEquals() {
        assertFalse((Boolean) eval("$ != $", BITS));
    }

    @Test
    public void testEqualsLiteral() {
        assertTrue((Boolean) eval("$ == Uint8Array.of(11, 22, 33, 44)", BITS));
    }

    @Test
    public void testLessThan() {
        assertFalse((Boolean) eval("$ < $"));
        assertTrue((Boolean) eval("null < Uint8Array.of(11, 22, 44)"));
        assertTrue((Boolean) eval("Uint8Array.of(11, 22) < Uint8Array.of(11, 22, 33)"));
        assertTrue((Boolean) eval("Uint8Array.of(11, 22, 33) < Uint8Array.of(11, 22, 44)"));
    }

    @Test
    public void testAddition() {
        assertEquals("1Hello, World!", eval("1 + $", MSG.getBytes(StandardCharsets.UTF_8)));
        assertEquals("Hello, World!1", eval("$ + 1", MSG.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testSubtraction() {
        assertEquals((Object) Double.NaN, eval("1 - $", MSG.getBytes(StandardCharsets.UTF_8)));
        assertEquals((Object) Double.NaN, eval("$ - 1", MSG.getBytes(StandardCharsets.UTF_8)));
    }
}
