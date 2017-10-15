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

import com.google.common.collect.ImmutableMap;
import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for map methods.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class MapMethodTest extends ExpressionTest {

    @Test
    public void testGet() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals(
                "abc123", eval("$.get('test')", data));
    }

    @Test
    public void testGetNotThere() {
        Object data = ImmutableMap.of("test1", "abc123");
        assertNull(eval("$.get('test')", data));
    }

    @Test
    public void testGetNotThereWithDefault() {
        Object data = ImmutableMap.of("test1", "abc123");
        assertEquals("default", eval("$.get('test', 'default')", data));
    }

    @Test
    public void testGetThereWithDefault() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals("abc123", eval("$.get('test', 'default')", data));
    }

    @Test
    public void testGetWithNumbers() {
        Object data = ImmutableMap.of("1", BigInteger.TEN);
        assertEquals(
                BigInteger.TEN, eval("$.get(1)", data));
    }

    @Test
    public void testGetWithNoArgs() {
        Object data = ImmutableMap.of("test", "abc123");
        assertNull(eval("$.get()", data));
    }
    @Test
    public void testExtend() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals(ImmutableMap.<String, Object>of(
                "test", "abc123",
                "foo", "bar"
        ), eval("$.extend({ foo: 'bar' })", data));
    }

    @Test
    public void testExtendWithNoArgs() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals(ImmutableMap.of(
                "test", "abc123"
        ), eval("$.extend()", data));
    }

    @Test
    public void testExtendWithMultiple() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals(ImmutableMap.of(
                "test", "abc123",
                "foo", "bar",
                "one", new BigInteger("1")
        ), eval("$.extend({ foo: 'bar' }, { one: 1 })", data));
    }

    @Test
    public void testExtendWithNull() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals(ImmutableMap.of(
                "test", "abc123",
                "foo", "bar"
        ), eval("$.extend({ foo: 'bar' }, null)", data));
    }

    @Test
    public void testExtendWithNumbers() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals(ImmutableMap.of(
                "test", "abc123",
                "0", "bar"
        ), eval("$.extend(['bar'])", data));
    }

    @Test
    public void testExtendWithPairs() {
        Object data = ImmutableMap.of("test", "abc123");
        assertEquals(ImmutableMap.of(
                "test", "abc123",
                "foo", "bar",
                "baz", new BigInteger("1")
        ), eval("$.extend([['foo', 'bar'], ['baz', 1]])", data));
    }

    @Test
    public void testExtendOrdering() {
        Map result = eval("{}.extend([['key1', 'bar'], ['key2', 1]])");
        assertEquals(ImmutableMap.of(
                "key1", "bar",
                "key2", new BigInteger("1")
        ), result);
        assertEquals(Arrays.asList("key1", "key2"), new ArrayList(result.keySet()));
    }

    @Test(expected = SnapDataException.class)
    public void testExtendWithBadArg() {
        Object data = ImmutableMap.of("test", "abc123");
        eval("$.extend({ foo: 'bar' }, 1)", data);
    }

    @Test
    public void testExtendAdd() {
        Object data = ImmutableMap.of("first", "Joe");
        Object dataExpected = ImmutableMap.of("first", "Joe","last","Smith");
        assertEquals(dataExpected, eval("$.extend({last:'Smith'})", data));
    }

    @Test
    public void testMergeNumber() {
        Object data = ImmutableMap.of("first", "Joe");

        assertEquals(BigInteger.valueOf(1), eval("$.merge(1)", data));
    }

    @Test
    public void testMergeShallow() {
        Object data = ImmutableMap.of("first", "Joe", "last", "Smith");

        assertEquals(ImmutableMap.of(
                "first", "Joe",
                "last", "Blow"
        ), eval("$.merge({last: 'Blow'})", data));
    }

    @Test
    public void testMergeShortArray() {
        Object data = ImmutableMap.of(
                "first", "Joe",
                "children", Arrays.asList(1, 2));

        assertEquals(ImmutableMap.of(
                "first", "Joe",
                "children", Arrays.asList(
                        BigInteger.valueOf(1),
                        BigInteger.valueOf(2),
                        BigInteger.valueOf(3))
        ), eval("$.merge({children: [1, 2, 3]})", data));
    }

    @Test
    public void testMergeLongArray() {
        Object data = ImmutableMap.of(
                "first", "Joe",
                "children", Arrays.asList(1, 2, 3, 4));

        assertEquals(ImmutableMap.of(
                "first", "Joe",
                "children", Arrays.asList(
                        BigInteger.valueOf(2),
                        2,
                        3,
                        4)
        ), eval("$.merge({children: [2]})", data));
    }

    @Test
    public void testMergeNested() {
        Object data = ImmutableMap.of(
                "first", "Joe",
                "children", Arrays.asList(
                        ImmutableMap.of(
                                "first", "Child1"
                        ),
                        ImmutableMap.of(
                                "first", "Child2"
                        )));

        assertEquals(ImmutableMap.of(
                "first", "Joe",
                "children", Arrays.asList(
                        ImmutableMap.of(
                                "first", "newChild"
                        ),
                        ImmutableMap.of(
                                "first", "Child2"
                        )
                )
        ), eval("$.merge({children: [{first: 'newChild'}]})", data));
    }
}
