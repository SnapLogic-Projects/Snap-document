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

import com.snaplogic.api.ExecutionException;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;


/**
 * Test for string search method.
 *
 * @author jilnoes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class SearchTest extends ExpressionTest {
    private static final String TEST_STR = "'For more information, see Chapter 3.4.5.1'";

    @Test
    public void testSearchRegexTrue() {
        assertTrue(((BigInteger) eval(TEST_STR + ".search(/(chapter \\d+(\\.\\d)*)/i)"))
                .compareTo(BigInteger.ZERO) >= 0);
    }

    @Test
    public void testSearchRegexFalse() {
        assertTrue(((BigInteger) eval(TEST_STR + ".search(/(chapter \\d+(\\.\\d)*)/)"))
                .compareTo(BigInteger.ZERO) < 0);
    }

    @Test
    public void testSearchStringTrue() {
        assertTrue(((BigInteger) eval(TEST_STR + ".search('more')"))
                .compareTo(BigInteger.ZERO) >= 0);
    }

    @Test
    public void testSearchStringFalse() {
        assertTrue(((BigInteger) eval(TEST_STR + ".search('fore')"))
                .compareTo(BigInteger.ZERO) < 0);
    }

    @Test(expected = ExecutionException.class)
    public void testBadPattern() {
        eval(TEST_STR + ".search(/(chapter \\d+(\\.\\d)*/i)");
    }

    @Test(expected = ExecutionException.class)
    public void testBadFlags() {
        eval(TEST_STR + ".search(/(chapter \\d+(\\.\\d)*/iaaaz)");
    }
}
