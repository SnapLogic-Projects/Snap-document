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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for replace string method.
 *
 * @author jinloes
 */
public class ReplaceTest extends ExpressionTest {
    private static final String TEST_STR = "'the batter hit the ball with the bat'";
    private static final String QUOTE_STR = "'\"quotes\"'";
    @Test
    public void testReplaceWithRegex() {
        assertEquals("a batter hit a ball with a bat", eval(TEST_STR + ".replace(/the/g, 'a')"));
    }

    @Test
    public void testReplaceWithStr() {
        assertEquals("the bbtter hit the ball with the bat", eval(TEST_STR + ".replace('a', 'b')"));
    }

    @Test
    public void testReplaceSpecialStr() {
        assertEquals("foo bar", eval("'foo|bar'.replace('|', ' ')"));
    }

    @Test
    public void testReplaceSpecialStrAndRegularStr() {
        assertEquals("foo ar", eval("'foo|bar'.replace('|b', ' ')"));
    }

    @Test
    public void testReplaceTwoSpecialStr() {
        assertEquals("foo bar", eval("'foo|*bar'.replace('|*', ' ')"));
    }

    @Test
    public void testReplaceWithQuotes() {
        assertEquals("quotes", eval(QUOTE_STR + ".replace(/\"/g, '')"));
    }

    @Test
    public void testS15_5_4_11_A4_T1() {
        assertEquals("12abc def34", eval(
                "'abc12 def34'.replace(/([a-z]+)([0-9]+)/, (...args) => args[2] + args[1])"));
    }

    @Test
    public void testS15_5_4_11_A4_T2() {
        assertEquals("12abc 34def", eval(
                "'abc12 def34'.replace(/([a-z]+)([0-9]+)/g, (...args) => args[2] + args[1])"));
    }

    @Test
    public void testS15_5_4_11_A4_T3() {
        assertEquals("12aBc def34", eval(
                "'aBc12 def34'.replace(/([a-z]+)([0-9]+)/i, (...args) => args[2] + args[1])"));
    }

    @Test
    public void testS15_5_4_11_A4_T4() {
        assertEquals("12aBc 34dEf", eval(
                "'aBc12 dEf34'.replace(/([a-z]+)([0-9]+)/ig, (...args) => args[2] + args[1])"));
    }

    @Test
    public void testCurly() {
        assertEquals("abc", eval("'abc{'.replace(/(\"|:|{|})/g,'')"));
    }

    @Test
    public void testUnicode() {
        assertEquals("abc", eval("'a‘bc'.replace(/‘/g,'')"));
    }
}
