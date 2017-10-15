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
 * Tests javascript array toString method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ToStringTest extends ExpressionTest {
    @Test
    public void testToString() {
        assertEquals("test1,test2,test3", eval("['test1','test2','test3'].toString()"));
    }

    @Test
    public void testToStringWithNull() {
        assertEquals("test1,,test3", eval("['test1',null,'test3'].toString()"));
    }
}
