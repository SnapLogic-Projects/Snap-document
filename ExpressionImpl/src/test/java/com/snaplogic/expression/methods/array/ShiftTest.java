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
import static org.junit.Assert.assertNull;


/**
 * Tests for javascript array shift method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ShiftTest extends ExpressionTest {
    @Test
    public void testShift() {
        assertEquals(BigInteger.valueOf(1), eval("[1, 2, 3].shift()"));
    }

    @Test
    public void testShiftEmpty() {
        assertNull(eval("[].shift()"));
    }

    @Test
    public void testShiftData() {
        Object data = ImmutableMap.<String, Object>of("list",
                Lists.newArrayList("test1", "test2", "test3"));
        Object expectedData = ImmutableMap.<String, Object>of(
                "list", Lists.newArrayList("test2", "test3"));
        assertEquals("test1", eval("$list.shift()", data));
        assertThat(data, equalTo(expectedData));
    }
}
