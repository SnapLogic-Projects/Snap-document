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

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for javascript array reverse method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ReverseTest extends ExpressionTest {
    @Test
    public void testReverse() {
        assertThat(eval("[1, 2, 3].reverse()"), CoreMatchers.<Object>equalTo(
                Lists.newArrayList(BigInteger.valueOf(3), BigInteger.valueOf(2),
                        BigInteger.valueOf(1))));
    }

    @Test
    public void testReverseEmptyList() {
        assertThat(eval("[].reverse()"), CoreMatchers.<Object>equalTo(Lists.newArrayList()));
    }
}
