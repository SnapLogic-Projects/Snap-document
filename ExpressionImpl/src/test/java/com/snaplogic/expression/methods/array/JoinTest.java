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

import com.snaplogic.expression.ExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript array join method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class JoinTest extends ExpressionTest {
    @Test
    public void testJoin() {
        assertEquals("test1,test2,test3", eval("['test1', 'test2', 'test3'].join(',')"));
    }

    @Test
    public void testJoinNoSeparator() {
        assertEquals("test1,test2,test3", eval("['test1', 'test2', 'test3'].join()"));
    }

    @Test
    public void testBadJoin() {
        assertEquals("a,", eval("['a', null].join()"));
    }
}
