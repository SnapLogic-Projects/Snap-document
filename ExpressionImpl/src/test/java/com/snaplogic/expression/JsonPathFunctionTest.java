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

import com.google.common.collect.ImmutableMap;
import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the jsonPath JavaScript functions.
 *
 * @author tstack
 */
public class JsonPathFunctionTest extends ExpressionTest {
    @Test
    public void testSimplePath() {
        assertEquals(Arrays.asList("baz"), eval("jsonPath({foo: {bar: 'baz'}}, '$..bar')"));
    }

    @Test
    public void testPathWithEnv() {
        Map<String, Object> envData = ImmutableMap.of("envvar", (Object) "bar");
        assertEquals("bar", eval("jsonPath({}, '$.eval(_envvar)')", envData));
    }

    @Test
    public void testWithNoPath() {
        assertEquals("bar", eval("jsonPath('bar')"));
    }

    @Test(expected = SnapDataException.class)
    public void testInvalidPath() {
        eval("jsonPath({}, '$.a[')");
    }

    @Test
    public void testJaninoInvalidPath() {
        try {
            eval("jsonPath({}, '$.a[')");
            fail();
        } catch (SnapDataException e) {
            assertEquals("Invalid JSON-path: $.a[", e.getMessage());
        }
    }

    @Test
    public void testSuffix() {
        assertEquals("foo#one,bar#two", eval(
                "jsonPath({foo: 'one', bar: 'two'}, '$.map(key + \\'#\\' + value).*').join(',')"));
    }
}
