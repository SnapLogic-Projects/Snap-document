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
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for instanceof operator.
 *
 * @author tstack
 */
public class InstanceOfTest extends ExpressionTest {
    private static final List<String> TRUE_TESTS = Arrays.asList(
            "null instanceof Null",
            "true instanceof Boolean",
            "false instanceof Boolean",
            // Technically, '1 instanceof Number' in javascript is false.  But,
            // we're not going to carry over that brain-dead behavior here.
            "1 instanceof Number",
            "1.0 instanceof Number",
            "'foo' instanceof String",
            "[1, 2, 3] instanceof Array",
            "{ a: 1 } instanceof Object",
            "Date.now() instanceof Date"
    );

    private static final List<String> FALSE_TESTS = Arrays.asList(
            "null instanceof Number",
            "null instanceof Boolean",
            "null instanceof String",
            "null instanceof Array",
            "null instanceof Object",
            "null instanceof Date",
            "1 instanceof Null",
            "1 instanceof Boolean",
            "1 instanceof String",
            "1 instanceof Array",
            "1 instanceof Object",
            "1 instanceof Date",
            "true instanceof Null",
            "true instanceof Number",
            "true instanceof String",
            "true instanceof Array",
            "true instanceof Object",
            "true instanceof Date",
            "'foo' instanceof Null",
            "'foo' instanceof Number",
            "'foo' instanceof Boolean",
            "'foo' instanceof Array",
            "'foo' instanceof Object",
            "'foo' instanceof Date",
            "[1, 2, 3] instanceof Null",
            "[1, 2, 3] instanceof Number",
            "[1, 2, 3] instanceof Boolean",
            "[1, 2, 3] instanceof String",
            "[1, 2, 3] instanceof Object",
            "[1, 2, 3] instanceof Date",
            "{ a: 1 } instanceof Null",
            "{ a: 1 } instanceof Number",
            "{ a: 1 } instanceof Boolean",
            "{ a: 1 } instanceof String",
            "{ a: 1 } instanceof Array",
            "{ a: 1 } instanceof Date",
            "Date.now() instanceof Null",
            "Date.now() instanceof Number",
            "Date.now() instanceof Boolean",
            "Date.now() instanceof String",
            "Date.now() instanceof Array",
            "Date.now() instanceof Object"
    );

    @Test
    public void testTrue() {
        for (String expr : TRUE_TESTS) {
            assertTrue(expr, (boolean) eval(expr));
        }
    }

    @Test
    public void testFalse() {
        for (String expr : FALSE_TESTS) {
            assertFalse(expr, (boolean) eval(expr));
        }
    }

    @Test(expected = SnapDataException.class)
    public void testBadType() {
        eval("1 instanceof 1");
    }

    @Test
    public void testStringVar() {
        assertTrue((boolean) eval("$foo instanceof String", ImmutableMap.<String, String>of("foo",
                "bar")));
    }
}
