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
import com.snaplogic.api.ExecutionException;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for object literals.
 *
 * @author tstack
 */
public class ObjectLiteralTest extends ExpressionTest {

    private static final String BIG_LITERAL_TXT = "/big_literal.txt";

    @Test
    public void testEmpty() {
        assertEquals(Collections.emptyMap(), eval("{}"));
    }

    @Test
    public void testBasic() {
        assertEquals(new HashMap<String, Object>() {{
            put("foo", "bar");
        }}, eval("{ /* comment */ foo: 'bar' }"));
    }

    @Test
    public void testStringName() {
        assertEquals(new HashMap<String, Object>() {{
            put("name with space", "bar");
        }}, eval("{ 'name with space': 'bar' }"));
    }

    @Test
    public void testStringNameWithEscape() {
        assertEquals(new HashMap<String, Object>() {{
            put("name with \"space'", "bar");
        }}, eval("{ 'name with \\\"space\\'': 'bar' }"));
    }

    @Test
    public void testNumberName() {
        assertEquals(new HashMap<Object, Object>() {{
            put("7", "bar");
        }}, eval("{ 7: 'bar' }"));
    }

    @Test
    public void testDynamicName() {
        assertEquals(new HashMap<String, Object>() {{
            put("8", "bar");
        }}, eval("{ [ 1 + 7 ]: 'bar' }"));
    }

    @Test
    public void testDynamicNameWithVars() {
        Object data = ImmutableMap.<String, Object>of("test", "foo");
        assertEquals(new HashMap<String, Object>() {{
            put("foobar", "bar");
        }}, eval("{ [ $test + 'bar' ]: 'bar' }", data));
    }

    @Test
    public void testMultiple() {
        assertEquals(new HashMap<String, Object>() {{
            put("foo", "bar");
            put("one", BigInteger.valueOf(1));
        }}, eval("{ foo: 'bar', one: 1 }"));
    }

    @Test
    public void testBigLiteral() throws Exception {
        Map map = eval(IOUtils.toString(ObjectLiteralTest.class.getResourceAsStream(
                BIG_LITERAL_TXT)));
        assertEquals(1953, map.size());
    }

    @Test
    public void testThis() throws Exception {
        assertEquals(ImmutableMap.of(
                "k1", "foo",
                "k2", "foobar"
        ), eval("{ k1: 'foo', k2: this.k1 + 'bar' }"));
    }

    @Test
    public void testRoot() throws Exception {
        assertEquals(ImmutableMap.of(
                "k1", "foo",
                "k2", ImmutableMap.of("k3", ImmutableMap.of("k4", "foo"))
        ), eval("{ k1: 'foo', k2: { k3: { k4: __root__.k1 } } }"));
    }

    @Test
    public void testParent() throws Exception {
        assertEquals(ImmutableMap.of(
                "k1", "foo",
                "k2", ImmutableMap.of("k30", "hi", "k3", ImmutableMap.of("k4", "hi"))
        ), eval("{ k1: 'foo', k2: { k30: 'hi', k3: { k4: __parent__.k30 } } }"));
    }

    @Test(expected = ExecutionException.class)
    public void testStoringThis() {
        eval("{ k1: this }");
    }

    @Test(expected = ExecutionException.class)
    public void testStoringParent() {
        eval("{ k1: { k2: __parent__} }");
    }

    @Test(expected = ExecutionException.class)
    public void testStoringRoot() {
        eval("{ k1: __root__ }");
    }

    @Test(expected = SnapDataException.class)
    public void testCycleAttempt() throws Exception {
        eval("{ k1: 'foo', k2: { k3: __parent__.k2 } }");
    }

    @Test(expected = ExecutionException.class)
    public void testCycleAtRuntime() throws Exception {
        ObjectType.toUnmodifiable(eval("{ k1: jsonPath(this, '$') }"));
    }

    @Test(expected = SnapDataException.class)
    public void testNoParent() {
        eval("{ foo: __parent__.bar }");
    }

    @Test(expected = SnapDataException.class)
    public void testRootOrder() throws Exception {
        eval("{ k2: { k3: __root__.k1 }, k1: 'foo' }");
    }

    @Test
    public void testSpread() {
        assertEquals(ImmutableMap.of(
                "k1", "foo",
                "k2", "bar"
        ), eval("{ k1: 'foo', ...{k2: 'bar'} }"));
    }

    @Test
    public void testSpreadArray() {
        assertEquals(ImmutableMap.of(
                "0", "foo",
                "1", "bar"
        ), eval("{ ...['foo', 'bar'] }"));
    }

    @Test
    public void testSpreadNull() {
        assertEquals(ImmutableMap.of(
                "k1", "v1"
        ), eval("{ k1: 'v1', ...null }"));
    }

    @Test(expected = ExecutionException.class)
    public void testUnterminated() {
        eval("{ foo: 'bar'");
    }

    @Test
    public void testTrailingComma() {
        eval("{ foo: 'bar', }");
    }

    @Test(expected = ExecutionException.class)
    public void testUnterminatedDynamic() {
        eval("{ [ 1 : 'bar' }");
    }

    @Test(expected = ExecutionException.class)
    public void testEmptyDynamic() {
        eval("{ [ ] : 'bar' }");
    }

    @Test
    public void testErrorMessage() {
        evalError("{ foo: (1)) }",
                "Could not compile expression: { foo: (1)) } ",
                "Encountered extraneous input ')' at line 1:10; expecting={<EOF>, '||', '&&', " +
                        "'^', '==', '!=', '>', '<', '>=', '<=', '+', '-', '*', '/', '%', '?', " +
                        "'instanceof', 'in', '[', '}', '(', ',', PropertyRef}");
    }
}
