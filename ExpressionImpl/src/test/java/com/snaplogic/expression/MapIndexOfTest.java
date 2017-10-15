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

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Tests for indexes on map. Index of a map is the key. The index
 * can be a number, boolean, string, or null.
 *
 * @author sadhana
 */
@SuppressWarnings("HardCodedStringLiteral")
public class MapIndexOfTest extends ExpressionTest {
    @Test
    public void testIndexOf() {
        assertEquals("abc123", eval("{7 : \"abc123\"}[7]"));
    }

    @Test(expected = SnapDataException.class)
    public void testInvalidIndex() {
        eval("{7 : \"abc123\"}[9]");
    }

    @Test
    public void testBooleanIndexOf() {
        assertEquals("abc123", eval("{\"true\" : \"abc123\"}[true]"));
    }

    @Test
    public void testStringIndexOf() {
        assertEquals(new BigInteger("2"), eval("([1, 2, 3])['1']"));
    }

    @Test
    public void testNullIndexOf() {
        assertEquals("abc123", eval("{\"null\" : \"abc123\"}[null]"));
    }
}