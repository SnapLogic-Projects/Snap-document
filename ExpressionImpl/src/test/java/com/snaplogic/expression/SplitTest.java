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

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for split method.
 */
public class SplitTest extends ExpressionTest {
    private static final String REGEX_TEST_STR = "\"Harry Trump ;Fred Barney; Helen Rigby ; " +
            "Bill Abel ;Chris Hand ;Jesse 'The Body' Ventura ;Billy \\\"The Kid\\\" \"";
    private static final String COMMA_TEST_STR = "\"Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov" +
            ",Dec\"";
    private static final String PIPE_TEST_STR = "\"Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov" +
            "|Dec\"";
    private static final String SPACE_TEST_STR = "\"How are you How are you\"";

    @Test
    public void testSplit() {
        assertThat(eval(COMMA_TEST_STR + ".split(',')"),
                equalTo((Object) Lists.newArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                        "Aug", "Sep", "Oct", "Nov", "Dec")));
    }

    @Test
    public void testSplitWithSpecialSeparator() {
        assertThat(eval(PIPE_TEST_STR + ".split('|')"),
                equalTo((Object) Lists.newArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                        "Aug", "Sep", "Oct", "Nov", "Dec")));
    }

    @Test
    public void testSplitRegex() {
        assertThat(eval(REGEX_TEST_STR + ".split(/\\s*;\\s*/)"),
                equalTo((Object) Lists.newArrayList("Harry Trump", "Fred Barney", "Helen Rigby",
                        "Bill Abel", "Chris Hand", "Jesse 'The Body' Ventura", "Billy \"The " +
                                "Kid\" ")));
    }

    @Test
    public void testSplitWithLimit() {
        assertThat(eval(COMMA_TEST_STR + ".split(',', 3)"),
                equalTo((Object) Lists.newArrayList("Jan", "Feb", "Mar")));
    }

    @Test
    public void testSplitWithLimitBySpecialSeparator() {
        assertThat(eval(PIPE_TEST_STR + ".split('|', 3)"),
                equalTo((Object) Lists.newArrayList("Jan", "Feb", "Mar")));
    }

    @Test
    public void testSplitWithHighLimit() {
        assertThat(eval(COMMA_TEST_STR + ".split(',', 15)"),
                equalTo((Object) Lists.newArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                        "Aug", "Sep", "Oct", "Nov", "Dec")));
    }

    @Test
    public void testSplitWithLimitStr() {
        assertThat(eval(COMMA_TEST_STR + ".split(',', 'abc')"),
                equalTo((Object) Lists.newArrayList()));
    }

    @Test
    public void testSplitBySeparatorWithSpace() {
        assertThat(eval(SPACE_TEST_STR + ".split('w a')"),
                equalTo((Object) Lists.newArrayList("Ho", "re you Ho", "re you")));
    }

    @Test
    public void testSplitEmpty() {
        assertEquals(Arrays.asList(""), eval("''.split('.')"));
    }

    @Test
    public void testSplitEmptyLimit() {
        assertEquals(Arrays.asList(""), eval("''.split('.', 2)"));
    }

    @Test
    public void testSplitEmptyZeroLimit() {
        assertEquals(Collections.EMPTY_LIST, eval("''.split('.', 0)"));
    }

    @Test
    public void testSplitNotFound() {
        assertEquals(Arrays.asList("abc"), eval("'abc'.split('.')"));
    }

    @Test
    public void testSplitNotFoundLimit() {
        assertEquals(Arrays.asList("abc"), eval("'abc'.split('.', 2)"));
    }

    @Test
    public void testSplitAdjacent() {
        assertEquals(Arrays.asList("a", "", "", "b", "c"), eval("'a   b c'.split(' ')"));
    }

    @Test
    public void testSplitAdjacentLimit() {
        assertEquals(Arrays.asList("a", ""), eval("'a   b c'.split(' ', 2)"));
    }

    @Test
    public void testPipeSeparator() {
        assertEquals(Arrays.asList("", "c", "", "", "d", "e", "", "f", ""),
                eval("'|c|||d|e||f|'.split('|')"));
    }

    @Test
    public void testPipeSeparatorAndLimit() {
        assertEquals(Arrays.asList("", "c", "", ""), eval("'|c|||d|e||f|'.split('|', 4)"));
    }
}
