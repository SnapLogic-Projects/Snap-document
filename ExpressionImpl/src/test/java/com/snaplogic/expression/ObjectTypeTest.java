/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2017, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.jsonpath.JsonPaths;
import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the ObjectType class
 */
public class ObjectTypeTest {
    @Test
    public void testToUnmodifiable() {
        JsonPath path = JsonPaths.compileStatic("$.child.str");
        JsonPath arrPath = JsonPaths.compileStatic("$.child.arr[1]");

        Map<String, Object> nested = new HashMap<String, Object>() {{
            put("num", 1);
            put("child", new HashMap<String, Object>() {{
                put("str", "Hello, World!");
                put("arr", new ArrayList<>(Arrays.asList(1, 2, 3)));
            }});
        }};

        Map<String, Object> unmodifiable = ObjectType.toUnmodifiable(nested);

        try {
            path.writeStatic(unmodifiable, "boom");
            fail();
        } catch (SnapDataException e) {
            assertEquals("Cannot set the property 'str' on an unmodifiable object", e.getMessage());
        }

        try {
            arrPath.writeStatic(unmodifiable, "boom");
            fail();
        } catch (SnapDataException e) {
            assertEquals("Cannot change element #1 in an unmodifiable array", e.getMessage());
        }
    }

    @Test
    public void testBigDecimalToString() {
        assertEquals("12300", ObjectType.toString(new BigDecimal("1.23e4")));
    }

    @Test
    public void testBytesToString() {
        assertEquals("foobar", ObjectType.toString("foobar".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testNullToString() {
        assertEquals("null", ObjectType.toString(null));
    }
}