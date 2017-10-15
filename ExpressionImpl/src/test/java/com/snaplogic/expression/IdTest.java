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
import com.google.common.collect.Lists;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests for ids in expression language.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class IdTest extends ExpressionTest {
    @Test
    public void testSimpleId() {
        Object data = ImmutableMap.<String, Object>of("test", "abc123");
        assertEquals("abc123", eval("$test", data));
    }

    @Test
    public void testUnderscoredId() {
        Object data = ImmutableMap.<String, Object>of("test_id", "abc123");
        assertEquals("abc123", eval("$test_id", data));
    }

    @Test(expected = SnapDataException.class)
    public void testRootIdNotMap() {
        List<String> data = Lists.newArrayList("test", "abc123");
        assertEquals("abc123", eval("$test", data));
    }

    @Test(expected = SnapDataException.class)
    public void testPropRefNotMap() {
        Object data = ImmutableMap.<String, Object>of("test",
                ImmutableMap.of("testb", Lists.newArrayList("testc")));
        eval("$test.testb.testc", data);
    }

    @Test(expected = SnapDataException.class)
    public void testPropRefUndefined() {
        Object data = ImmutableMap.<String, Object>of("test",
                ImmutableMap.of("testb", Lists.newArrayList("testc")));
        eval("$test.testc.testd", data);
    }

    @Test
    public void testComplexId() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", "abc123"));
        assertEquals(eval("$test.testa", data), "abc123");
    }

    @Test
    public void testLongerComplexId() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", ImmutableMap.of("testb", "abc123")));
        assertEquals(eval("$test.testa.testb", data), "abc123");
    }

    @Test
    public void testParenComplexId() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", ImmutableMap.of("testb", "abc123")));
        assertEquals("abc123", eval("($test.testa).testb", data));
    }

    @Test
    public void testStringIndex() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", ImmutableMap.of("testb", "abc123")));
        assertEquals("abc123", eval("$['test']['testa']['testb']", data));
    }

    @Test
    public void testStringIndexWithDot() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", ImmutableMap.of("testb.something", "abc123")));
        assertEquals("abc123", eval("$['test']['testa']['testb.something']", data));
    }


    @Test(expected = SnapDataException.class)
    public void testStringIndexNonMapStructure() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", "testb"));
        eval("$['test']['testa']['testb']", data);
    }

    @Test
    public void testNumberIndex() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", Lists.newArrayList("abc", "abc123")));
        assertEquals("abc123", eval("$['test']['testa'][1]", data));
    }

    @Test
    public void testParenNumberIndex() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", Lists.newArrayList("abc", "abc123")));
        assertEquals("abc123", eval("($['test']['testa'])[1]", data));
    }

    @Test(expected = SnapDataException.class)
    public void testNumberIndexNonListStructure() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", false));
        eval("$['test']['testa'][1]", data);
    }

    @Test(expected = SnapDataException.class)
    public void testUnsupportedObject() {
        Object data = ImmutableMap.of("test", new Object());
        assertEquals("abc123", eval("$test.trim()", data));
    }

    @Test(expected = SnapDataException.class)
    public void testMissingId() {
        Object data = ImmutableMap.<String, Object>of("test", "abc123");
        eval("$test2", data);
    }
    @Test(expected = SnapDataException.class)
    public void testRelationalMissingId() {
        Object data = ImmutableMap.<String, Object>of("test", "abc123");
        eval("$test2 > 5", data);
    }

    @Test
    public void testGetNullValue() {
        Object data = new HashMap<String, Object>() {{
            put("test", null);
        }};
        assertTrue((Boolean) eval("$test == null", data));
    }

    @Test
    public void testEmptyMap() {
        Object data = ImmutableMap.<String, Object>of("test", new HashMap<>());
        assertFalse((Boolean) eval("$.isEmpty()", data));
        assertTrue((boolean) eval("$.test.isEmpty()", data));
    }

    @Test(expected = SnapDataException.class)
    public void testNestedMissingValue() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.<String, Object>of("foo", "bar"));
        assertTrue((Boolean) eval("$test.test2 == null", data));
    }

    @Test
    public void testMissingPath() {
        Object data = ImmutableMap.<String, Object>of("num", BigInteger.valueOf(1),
                "test", Arrays.asList(null, ImmutableMap.<String, Object>of("foo", "bar")));
        try {
            eval("false || $test[$.test[$num].test2.test3]", data);
        } catch (SnapDataException e) {
            assertTrue(e.getMessage().contains("$.test[$num].test2 "));
        }
        try {
        } catch (SnapDataException e) {
            assertTrue(e.getMessage().contains("$.test[$num].test2 "));
        }
    }

    @Test
    public void testGetNestedNullValue() {
        Object data = ImmutableMap.<String, Object>of(
                "test",
                new HashMap<String, Object>() {{
                    put("test", null);
                }}
        );
        assertTrue((Boolean) eval("$test.test == null", data));
    }

    @Test(expected = ExecutionException.class)
    public void testPropRefSyntaxError() {
        Object data = ImmutableMap.<String, Object>of("first", "Bob", "last", "Smith");
        eval("$first$$$last", data);
    }

    @Test
    public void testEnvParam() {
        Map<String, Object> envMap = ImmutableMap.<String, Object>of("url", "www.google.com");
        assertEquals("www.google.com", eval("_url", envMap));
    }

    @Test(expected = SnapDataException.class)
    public void testEnvParamBadPath() {
        Map<String, Object> envMap = ImmutableMap.of();
        eval("_url", envMap);
    }

    @Test(expected = SnapDataException.class)
    public void testBadAccessor() {
        Object data = ImmutableMap.of("list", Arrays.asList(1, 2));

        eval("$list[null]", data);
    }
}
