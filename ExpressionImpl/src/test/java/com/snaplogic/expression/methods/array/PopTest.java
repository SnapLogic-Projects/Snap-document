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

import com.google.common.collect.Lists;
import com.snaplogic.expression.ExpressionTest;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for javascript array pop method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class PopTest extends ExpressionTest {

    @Test
    public void testPopEmptyArray() {
        assertNull(eval("[].pop()"));
    }

    @Test
    public void testPopArgs() {
        assertNull(eval("[].pop(123, 123)"));
    }

    @Test
    public void testPop() {
        assertEquals(BigInteger.valueOf(3), eval("[1, 2, 3].pop(123, 123)"));
    }

    @Test
    public void testPopData() {
        List<String> data = Lists.newArrayList("test1", "test2", "test3");
        assertEquals("test3", eval("$.pop()", data));
        assertThat(data, CoreMatchers.<List<String>>equalTo(Lists.newArrayList("test1", "test2")));
    }

    @Test
    public void testPurity() {
        eval("$[1] + $[1] + $[1] + $.pop()", Lists.newArrayList("1", "2"));
    }
}
