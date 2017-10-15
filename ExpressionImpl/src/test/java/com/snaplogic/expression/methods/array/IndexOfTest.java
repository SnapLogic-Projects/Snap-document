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
 * Tests for javascript array index of method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class IndexOfTest extends ExpressionTest {
    @Test
    public void testIndexOf() {
        assertEquals(new BigInteger("1"),
                eval("['test1','test2','test3'].indexOf('test2')"));
    }

    @Test
    public void testIndexOfNotFound() {
        assertEquals(new BigInteger("-1"),
                eval("['test1','test2','test3'].indexOf('test4')"));
    }

    @Test
    public void testIndexOfStart() {
        assertEquals(new BigInteger("3"),
                eval("['test1','test2','test3', 'test2'].indexOf('test2', 2)"));
    }

    @Test
    public void testIndexOfStartComplex() {
        assertEquals(new BigInteger("3"),
                eval("['test1','test2','test3', 'test2'].indexOf('test2', 4 / 2)"));
    }

    @Test
    public void testIndexOfStartLarger() {
        assertEquals(new BigInteger("-1"),
                eval("['test1','test2','test3', 'test2'].indexOf('test2', 20)"));
    }

    @Test
    public void testIndexOfNotFoundStart() {
        assertEquals(new BigInteger("-1"),
                eval("['test1','test2','test3'].indexOf('test2', 2)"));
    }

    @Test
    public void testIndexOfNegativeStart() {
        assertEquals(new BigInteger("1"),
                eval("['test1','test2','test3', 'test2'].indexOf('test2', -2)"));
    }
}
