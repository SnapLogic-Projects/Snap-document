/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.snap.api.SnapDataException;
import org.antlr.v4.runtime.misc.Triple;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for the eval() function.
 *
 * @author tstack
 */
public class EvalTest extends ExpressionTest {
    private static final List<Triple<Object, String, Object>> TEST_VALUES = Arrays.asList(
            new Triple<>((Object) new BigInteger("1"), "'1'", null),
            new Triple<>((Object) new BigInteger("1"), "'$'", (Object) new BigInteger("1")),
            new Triple<>((Object) new BigDecimal("2"), "'$ + 1'", (Object) 1),
            new Triple<>((Object) "bar", "'_foo'", null),
            new Triple<>((Object) new BigInteger("1"), "1", null),
            new Triple<>((Object) "FIRST_NAME = bar", "_param", (Object)
                    new HashMap<String, Object>() {{
                put("FIRST_NAME", "bar");
            }})
    );

    @Test
    public void testEval() {
        Map<String, Object> envData = new HashMap<String, Object>() {{
            put("foo", "bar");
            put("param", "'FIRST_NAME = ' + $FIRST_NAME");
        }};
        for (Triple<Object, String, Object> triple : TEST_VALUES) {
            assertEquals(triple.a, eval(String.format("eval(%s)", triple.b), triple.c, envData));
        }
    }

    @Test(expected = SnapDataException.class)
    public void testEvalError() {
        eval("eval('foo bar')");
    }

    @Test
    public void testEvalNoArgs() {
        assertNull(eval("eval()"));
    }

    @Test
    public void testEvalContext() {
        assertEquals("Hello", eval("true && eval('$') && eval('$')", "Hello"));
    }
}