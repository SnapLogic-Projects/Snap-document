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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.snaplogic.expression.ExpressionTest;
import com.snaplogic.snap.api.SnapDataException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript arrays.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ArrayTest extends ExpressionTest {

    @Test
    public void testEmptyArray() {
        assertThat((List) eval("[]"), CoreMatchers.<List>equalTo(Lists.newArrayList()));
    }

    @Test
    public void testArrayWithTrailingComma() {
        assertThat((List) eval("['test',]"), CoreMatchers.<List>equalTo(Arrays.asList("test")));
    }

    @Test
    public void testNumArray() {
        assertThat((List) eval("[1, 2, 3, 4, 5]"), CoreMatchers.<List>equalTo(
                Lists.newArrayList(BigInteger.valueOf(1), BigInteger.valueOf(2),
                        BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5))));
    }

    @Test
    public void testStringArray() {
        assertThat((List) eval("[\"test\", 'test2']"), CoreMatchers.<List>equalTo(
                Lists.newArrayList("test", "test2")));
    }

    @Test
    public void testListFromObjectRef() {
        Object data = ImmutableMap.<String, Object>of(
                "Name", ImmutableMap.of("first", "John", "last", "Smith"));
        assertThat((List) eval("[$Name.first, $['Name']['last']]", data),
                CoreMatchers.<List>equalTo(Lists.newArrayList("John", "Smith")));
    }

    @Test
    public void testMixedList() {
        Object data = ImmutableMap.<String, Object>of(
                "Names", Lists.newArrayList(ImmutableMap.of("first", "Jim", "last", "Smith"),
                        ImmutableMap.of("first", "John", "last", "Johnson")),
                "Size", BigInteger.valueOf(25));
        assertThat((List) eval("[$Names[0].first, $['Names'][1]['last'], $Size]", data),
                CoreMatchers.<List>equalTo(Lists.newArrayList("Jim", "Johnson",
                        BigInteger.valueOf(25))));
    }

    @Test
    public void testArrayLength() {
        assertEquals(BigInteger.valueOf(5), eval("[1,2,3,4,5].length"));
    }

    @Test
    public void testSpread() {
        assertEquals(Arrays.asList("1", "2", "3", "4"), eval("['1', ...['2', '3'], '4']"));
    }

    @Test
    public void testSpreadVariable() {
        assertEquals(Arrays.asList("1", "2", "3", "4"), eval("['1', ...$, '4']", Arrays.asList("2",
                "3")));
    }

    @Test
    public void testSpreadNumber() {
        assertEquals(Arrays.asList("1", BigInteger.ONE), eval("['1', ...1]"));
    }

    @Test
    public void testSpreadNull() {
        assertEquals(Arrays.asList("1"), eval("['1', ...null]"));
    }

    @Test
    public void testSpreadEmpty() {
        assertEquals(Arrays.asList("1"), eval("['1', ...[]]"));
    }

    @Test(expected = SnapDataException.class)
    public void testBadIndex() {
        eval("$[0]", Collections.emptyList());
    }

    @Test(expected = SnapDataException.class)
    public void testNegIndex() {
        eval("$[-1]", Collections.emptyList());
    }
}
