/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2017, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for ReplaceAll string method.
 */
public class ReplaceAllTest extends ExpressionTest {
    private static final String TEST_STR = "'Jan|Feb|Mar|Jan|Feb|Mar'";

    @Test
    public void testReplaceAllBasic() {
        assertEquals("Apr|Feb|Mar|Apr|Feb|Mar", eval(TEST_STR + ".replaceAll('Jan', 'Apr')"));
    }

    @Test
    public void testReplaceAllByRegexStr() {
        assertEquals("Jan*Feb*Mar*Jan*Feb*Mar", eval(TEST_STR + ".replaceAll('|', '*')"));
    }

    @Test
    public void testReplaceAllByNoMatchRegexStr() {
        assertEquals("Jan|Feb|Mar|Jan|Feb|Mar", eval(TEST_STR + ".replaceAll('*', '|')"));
    }

    @Test
    public void testReplaceAllWithoutMatch() {
        assertEquals("Jan|Feb|Mar|Jan|Feb|Mar", eval(TEST_STR + ".replaceAll('Apr', 'Jan')"));
    }

    @Test
    public void testReplaceAllWithoutMatchAndRegex() {
        assertEquals("Jan|Feb|Mar|Jan|Feb|Mar", eval(TEST_STR + ".replaceAll('*', 'Jan')"));
    }

    @Test
    public void testReplaceAllWithEmptyString() {
        assertEquals("Jan", eval("''.replaceAll('', 'Jan')"));
    }

    @Test
    public void testReplaceAllWithEmptyReplacement() {
        assertEquals("Jan|Mar|Jan|Mar", eval(TEST_STR + ".replaceAll('Feb|', '')"));
    }

    @Test
    public void testReplaceAllWithEmptySearchString() {
        assertEquals("Jan", eval("'Jan'.replaceAll('', '')"));
    }

    @Test
    public void testReplaceAllWithNumber() {
        assertEquals("11311", eval("'12321'.replaceAll(2, 1)"));
    }

    @Test(expected = SnapDataException.class)
    public void testReplaceAllWithInvalidArgumentsNumber() {
        eval(TEST_STR + ".replaceAll('Jan')");
    }
}
