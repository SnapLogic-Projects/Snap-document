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

package com.snaplogic.expression.methods.object;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.ExpressionTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HasOwnProperty}.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class HasOwnPropertyTest extends ExpressionTest {
    @Test
    public void testHasOwnProperty() {
        Object data = ImmutableMap.of("first", "Joe");
        assertEquals(true, eval("$.hasOwnProperty('first')", data));
        assertEquals(0, LintReporter.messages.size());
    }

    @Test
    public void testHasOwnPropertyFalse() {
        Object data = ImmutableMap.of("first", "Joe");
        assertEquals(false, eval("$.hasOwnProperty('abc')", data));
        assertEquals(0, LintReporter.messages.size());
    }

    @Test
    public void testHasOwnPropertyNonStr() {
        Object data = ImmutableMap.of("first", "Joe");
        assertEquals(false, eval("$.hasOwnProperty(5)", data));
        assertEquals(0, LintReporter.messages.size());
    }

    @Test
    public void testHasOwnPropertyNullLint() {
        Map<Object, Object> data = new HashMap<>();
        data.put("first", null);
        eval("$.hasOwnProperty('first')", data);
        assertEquals("com.snaplogic.expression.methods.object.HasOwnProperty.NULL_VALUE",
                LintReporter.messages.get(LintReporter.messages.size() - 1));
    }

    @Test
    public void testHasOwnPropertyInvalidLint() {
        Object data = ImmutableMap.of("first", "joe");
        eval("$.hasOwnProperty('$first')", data);
        assertEquals("com.snaplogic.expression.methods.object.HasOwnProperty.INVALID_VALUE",
                LintReporter.messages.get(LintReporter.messages.size() - 1));
    }
}
