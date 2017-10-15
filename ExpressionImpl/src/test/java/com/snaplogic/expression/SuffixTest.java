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

package com.snaplogic.expression;

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


/**
 * Test for string search method.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class SuffixTest extends ExpressionTest {
    @Test
    public void testNullMethodDeref() {
        try {
            eval("$['Foo'].split('/', 1)", Collections.emptyMap());
        } catch (SnapDataException e) {
            assertEquals("$['Foo'] is undefined", e.getMessage());
        }
    }

    @Test
    public void testInvalidMethodDeref() {
        try {
            eval("null.split('/', 1)");
        } catch (SnapDataException e) {
            assertEquals("Cannot invoke a method on a null value", e.getMessage());
        }
    }

    @Test
    public void testMissingFieldIndex() {
        try {
            eval("$['Foo']", Collections.emptyMap());
        } catch (SnapDataException e) {
            assertEquals("$['Foo'] is undefined", e.getMessage());
        }
    }

    @Test
    public void testByteArrayIndex() {
        assertEquals(BigInteger.valueOf(255), eval("$[1]", new byte[] { 11, (byte) 0xff, 33 }));
    }
}
