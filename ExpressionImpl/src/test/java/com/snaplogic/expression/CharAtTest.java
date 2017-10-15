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

package com.snaplogic.expression;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for string charAt method.
 *
 * @author jinloes
 */
public class CharAtTest extends ExpressionTest {
    @Test
    public void testCharAt() {
        assertEquals("a", eval("'snaplogic'.charAt(2)"));
    }

    @Test
    public void testCharAtIndexOutOfBounds() {
        assertEquals("", eval("'snaplogic'.charAt(100)"));
    }

    @Test
    public void testCharAtFloat() {
        assertEquals("a", eval("'snaplogic'.charAt(2.6)"));
    }

    @Test
    public void testCharAtString() {
        assertEquals("s", eval("'snaplogic'.charAt('abc')"));
    }

    @Test
    public void testCharAtLessLen() {
        assertEquals("", eval("'snaplogic'.charAt(-1)"));
    }

    @Test
    public void testCharAtData() {
        Object data = ImmutableMap.<String, Object>of("first_name", "James",
                "last_name", "Jackson", "phone_num", "123-456-7890");
        assertEquals("", eval("$last_name.charAt($phone_num.indexOf(\"222\"))", data));
    }
}
