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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript array unshift method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class UnshiftTest extends ExpressionTest {

    @Test
    public void testUnshift() {
        assertEquals(BigInteger.valueOf(5),
                eval("['test1', 'test2', \"test3\"].unshift('test4', 'test5')"));
    }

    @Test
    public void testUnshiftData() {
        List<Object> data = Lists.<Object>newArrayList("test1", "test2", "test3");
        assertEquals(BigInteger.valueOf(5), eval("$.unshift('test4', 'test5')", data));
        assertThat(data, CoreMatchers.<List<Object>>equalTo(
                Lists.<Object>newArrayList("test4", "test5", "test1", "test2", "test3")));
    }

}
