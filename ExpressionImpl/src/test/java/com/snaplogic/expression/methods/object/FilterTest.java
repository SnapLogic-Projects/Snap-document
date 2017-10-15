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

package com.snaplogic.expression.methods.object;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.ExpressionTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Test for the filter() method on objects.
 *
 * @author tstack
 */
public class FilterTest extends ExpressionTest {
    @Test
    public void testNone() {
        Assert.assertEquals(Collections.EMPTY_MAP, eval(
                "{a: 1, b: 2}.filter(() => false)"));
    }

    @Test
    public void testNonBoolean() {
        Assert.assertEquals(Collections.EMPTY_MAP, eval(
                "{a: 1, b: 2}.filter(() => null)"));
    }

    @Test
    public void testAll() {
        Object obj = ImmutableMap.of(
                "foo", "bar",
                "baz", "boo"
        );

        Assert.assertEquals(obj, eval("$.filter(() => true)", obj));
    }

    @Test
    public void testInts() {
        Object obj = ImmutableMap.of(
                "foo", 1,
                "bar", "baz"
        );

        Map<String, Integer> expected = ImmutableMap.of(
                "foo", 1
        );

        Assert.assertEquals(expected, eval("$.filter(v => v instanceof Number)", obj));
    }

    @Test
    public void testKeyPrefix() {
        Object obj = ImmutableMap.of(
                "foo:abc", 1,
                "foo:def", 2,
                "bar:def", "baz"
        );

        Map<String, Integer> expected = ImmutableMap.of(
                "foo:abc", 1,
                "foo:def", 2
        );

        Assert.assertEquals(expected, eval("$.filter((v, k) => k.startsWith('foo:'))", obj));
    }

    @Test
    public void testInternal() {
        Object obj = ImmutableMap.of(
                "keys", Arrays.asList("abc", "ghi"),
                "abc", 1,
                "def", 2,
                "ghi", 3
        );

        Map<String, Integer> expected = ImmutableMap.of(
                "abc", 1,
                "ghi", 3
        );

        Assert.assertEquals(expected, eval("$.filter((v, k, o) => o.keys.indexOf(k) != -1)", obj));
    }
}
