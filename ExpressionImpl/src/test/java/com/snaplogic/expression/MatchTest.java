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

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Tests for match method.
 *
 * @author jinloes
 */
public class MatchTest extends ExpressionTest {

    @Test
    public void testMatch() {
        // javascript regex /(chapter \d+(\.\d)*)/i
        assertThat(eval("'For more information, see Chapter 3.4.5.1'" +
                ".match(/(chapter \\d+(\\.\\d)*)/i)"), equalTo((Object)
                Lists.newArrayList("Chapter 3.4.5.1", "Chapter 3.4.5.1", ".1")));
    }

    @Test
    public void testLetters() {
        // javascript regex /[A-E]/gi
        assertThat(eval("\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\"" +
                ".match(/[A-E]/gi)"),
                equalTo((Object) Lists.newArrayList("A", "B", "C", "D", "E", "a",
                        "b", "c", "d", "e")));
    }

    @Test
    public void testEscapedSlash() {
        assertEquals("/bar", eval("'/foo/bar'.match(/(\\/bar)/)[0]"));
    }

    @Test
    public void testStringRegex() {
        assertEquals(Lists.newArrayList("food"), eval("'food'.match('f' + 'o*' + 'd')"));
    }
}
