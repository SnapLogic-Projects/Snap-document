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
import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for string concat method.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class ConcatTest extends ExpressionTest {
    @Test
    public void testConcat() {
        Object data = ImmutableMap.<String, Object>of(
                "message1", "Test test test test.",
                "message2", " More testing.",
                "message3", ImmutableMap.of("text", " Message3"));
        assertEquals("Test test test test. More testing. Message3",
                eval("$message1.concat($message2, $message3.text)", data));
    }

    @Test
    public void testConcatComplex() {
        Object data = ImmutableMap.<String, Object>of("first_name", "Bob",
                "last_name", "Johnson");
        assertEquals("Bob Johnson", eval("$first_name.concat(' ' + $last_name)", data));
    }

    @Test(expected = SnapDataException.class)
    public void testConcatTypo() {
        Object data = ImmutableMap.<String, Object>of("first_name", "Bob",
                "last_name", "Johnson");
        assertEquals("Bob Johnson", eval("$first_name.comcat(' ' + $last_name)", data));
    }
}
