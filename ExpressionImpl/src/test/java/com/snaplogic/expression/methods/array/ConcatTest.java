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
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for javascript array concat method.
 *
 * @author jinloe
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ConcatTest extends ExpressionTest {
    @Test
    public void testConcat() {
        assertThat(eval("['test1', 'test2'].concat(1, 'test3', 2)"),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test1", "test2",
                        BigInteger.valueOf(1), "test3", BigInteger.valueOf(2))));
    }

    @Test
    public void testConcatData() {
        Object data = ImmutableMap.<String, Object>of(
                "list", Lists.newArrayList("test1", "test2"),
                "first", "Joe", "last", "Jackson");
        Object expectedData = ImmutableMap.<String, Object>of(
                "list", Lists.newArrayList("test1", "test2"),
                "first", "Joe", "last", "Jackson");
        assertThat(eval("$list.concat($first, $['last'])", data),
                CoreMatchers.<Object>equalTo(
                        Lists.newArrayList("test1", "test2", "Joe", "Jackson")));
        assertThat(data, equalTo(expectedData));
    }
}
