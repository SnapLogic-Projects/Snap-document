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
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for javascript array slice method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class SliceTest extends ExpressionTest {
    @Test
    public void testSlice() {
        assertThat(eval("['test1', 'test2', 'test3', 'test4'].slice(1,3)"),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test2", "test3")));
    }

    @Test
    public void testSliceCopy() {
        List<String> list = new ArrayList<>(Arrays.asList("abc", "123"));
        List<String> copy = eval("$.slice(0, 1)", list);
        list.set(0, "def");
        Assert.assertEquals(Arrays.asList("abc"), copy);
    }

    @Test
    public void testSliceData() {
        List<Object> data = Lists.<Object>newArrayList("test1", "test2", "test3", "test4");
        assertThat(eval("$.slice(-2, -1)", data),
                CoreMatchers.<Object>equalTo(Lists.newArrayList("test3")));
        assertThat(data, CoreMatchers.<List<Object>>equalTo(
                Lists.<Object>newArrayList("test1", "test2", "test3", "test4")));
    }
}
