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

import static org.junit.Assert.assertThat;

/**
 * Tests for array splice method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class SpliceTest extends ExpressionTest {

    @Test
    public void testSpliceRemove() {
        assertThat(eval("[1, 2, 3].splice(1)"),
                CoreMatchers.<Object>equalTo(
                        Lists.newArrayList(BigInteger.valueOf(2), BigInteger.valueOf(3))));
    }

    @Test
    public void testSpliceRemoveData() {
        List<Object> data = Lists.<Object>newArrayList("test1", "test2", "test3");
        assertThat(eval("$.splice(1)", data),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test2", "test3")));
        assertThat(data, CoreMatchers.<List<Object>>equalTo(Lists.<Object>newArrayList("test1")));
    }

    @Test
    public void testSpliceRemoveRangeData() {
        List<Object> data = Lists.<Object>newArrayList("test1", "test2", "test3");
        assertThat(eval("$.splice(1, 1)", data),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test2")));
        assertThat(data, CoreMatchers.<List<Object>>equalTo(
                Lists.<Object>newArrayList("test1", "test3")));
    }

    @Test
    public void testSpliceRemoveNegativeRangeData() {
        List<Object> data = Lists.<Object>newArrayList("test1", "test2", "test3");
        assertThat(eval("$.splice(-1, 2)", data),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test3")));
        assertThat(data, CoreMatchers.<List<Object>>equalTo(
                Lists.<Object>newArrayList("test1", "test2")));
    }

    @Test
    public void testSpliceAddRange() {
        List<Object> data = Lists.<Object>newArrayList("test1", "test2", "test3");
        assertThat(eval("$.splice(1, 1, 'test4', 'test5', 'test6')", data),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test2")));
        assertThat(data, CoreMatchers.<List<Object>>equalTo(
                Lists.<Object>newArrayList("test1", "test4", "test5", "test6", "test3")));
    }

    @Test
    public void testSpliceAddNegativeRange() {
        List<Object> data = Lists.<Object>newArrayList("test1", "test2", "test3");
        assertThat(eval("$.splice(-2, 2, 'test4', 'test5', 'test6')", data),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test2", "test3")));
        assertThat(data, CoreMatchers.<List<Object>>equalTo(
                Lists.<Object>newArrayList("test1", "test4", "test5", "test6")));
    }
}
