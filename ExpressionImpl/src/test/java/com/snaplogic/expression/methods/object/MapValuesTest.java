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

/**
 * Test the mapValues() method on objects.
 *
 * @author tstack
 */
public class MapValuesTest extends ExpressionTest {
    private static final Object TEST_OBJECT = ImmutableMap.of(
            "abc", "123",
            "def", 1
    );

    @Test
    public void testIdentity() {
        Assert.assertEquals(TEST_OBJECT, eval("$.mapValues((v, k) => v)", TEST_OBJECT));
    }

    @Test
    public void testPrefix() {
        Assert.assertEquals(ImmutableMap.of(
                "abc", "foo:123",
                "def", "foo:1"
        ), eval("$.mapValues((v, k) => 'foo:' + v)", TEST_OBJECT));
    }

    @Test
    public void testValues() {
        Assert.assertEquals(ImmutableMap.of(
                "abc", "abc=123",
                "def", "def=1"
        ), eval("$.mapValues((v, k) => k + '=' + v)", TEST_OBJECT));
    }

    @Test
    public void testInternal() {
        Assert.assertEquals(ImmutableMap.of(
                "abc", "123",
                "def", "1"
        ), eval("$.mapValues((v, k, o) => '' + o[k])", TEST_OBJECT));
    }
}
