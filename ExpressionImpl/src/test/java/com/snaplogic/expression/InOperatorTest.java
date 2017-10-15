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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Tests for in operator.
 *
 * @author choward18
 */
 public class InOperatorTest extends ExpressionTest {

    @Test
    public void testInSimple() {
        List messages = Arrays.asList("messages");
        Object data = ImmutableMap.<String, Object>of("messages", messages);
        assertTrue(eval("0 in $messages", data));
    }

    @Test
    public void testInNegative() {
        List messages = Arrays.asList("messages");
        Object data = ImmutableMap.<String, Object>of("messages", messages);
        assertFalse(eval("-1 in $messages", data));
    }

    @Test
    public void testInDecimalWrong() {
        List messages = Arrays.asList("messages", "messages1");
        Object data = ImmutableMap.<String, Object>of("messages", messages);
        assertFalse(eval("1.1 in $messages", data));
    }

    @Test
    public void testInDecimalRight() {
        List messages = Arrays.asList("messages", "messages1");
        Object data = ImmutableMap.<String, Object>of("messages", messages);
        assertTrue(eval("1.0 in $messages", data));
    }

    @Test
    public void testInNotThere() {
        List messages = Arrays.asList("messages", "messages1");
        Object data = ImmutableMap.<String, Object>of("messages", messages);
        assertFalse(eval("5 in $messages", data));
    }

    @Test
    public void testInWithExpression() {
        List messages = Arrays.asList("messages", "messages1","messages2" );
        Object data = ImmutableMap.<String, Object>of("messages", messages);
        assertTrue(eval("(1 + 1) in $messages", data));
    }

    @Test
    public void testInMultiple() {
        List messages = Arrays.asList("messages", "true","messages2" );
        Object data1 = ImmutableMap.<String, Object>of("messages", messages, "true", "a");
        Object data = ImmutableMap.<String, Object>of("messages", messages,
                "true", "a", "data", data1);
        assertTrue(eval("(1 + 1) in $messages in $data", data));
    }

    @Test
    public void testInWithBadType() {
        String messages = "messages";
        Object data = ImmutableMap.<String, Object>of("messages", messages);
        try {
            eval("5 in $messages", data);
        } catch (Exception e) {
            assertEquals("Unsupported type used for in operator: " +
                    "messages", e.getLocalizedMessage());
        }
    }
}