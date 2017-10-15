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
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript array push method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class PushTest extends ExpressionTest {

    @Test
    public void testPush() {
        assertEquals(BigInteger.valueOf(3), eval("[].push(1, 2, 3)"));
    }

    @Test
    public void testPushData() {
        Object data = ImmutableMap.<String, Object>of(
                "list", Lists.newArrayList(), "first1", "Bob", "first2", "Bill");
        Object expectedData = ImmutableMap.<String, Object>of(
                "list", Lists.newArrayList("Bob", "Bill"),
                "first1", "Bob", "first2", "Bill");
        assertEquals(BigInteger.valueOf(2), eval("$list.push($first1, $['first2'])", data));
        assertThat(data, equalTo(expectedData));
    }
}
