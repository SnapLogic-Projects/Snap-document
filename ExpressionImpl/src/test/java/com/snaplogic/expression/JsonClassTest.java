/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the methods in the JSON class.
 *
 * @author tstack
 */
public class JsonClassTest extends ExpressionTest {
    private static final String MSG = "Hello, World!";
    private static final List<Pair<String, Object>> TEST_VALUES = Arrays.asList(
//            Pair.<String, Object>of("1", BigInteger.valueOf(1)),
//            Pair.<String, Object>of("{}", Collections.emptyMap()),
            Pair.<String, Object>of("[1,2,3.1,\"one\",true]", Arrays.asList(
                    BigInteger.valueOf(1),
                    BigInteger.valueOf(2),
                    new BigDecimal("3.1"),
                    "one",
                    true
            ))
    );

    @Test
    public void testJsonParse() {
        for (Pair<String, Object> pair : TEST_VALUES) {
//            assertEquals(pair.getRight(), eval("JSON.parse($)", pair.getLeft()));
//            assertEquals(pair.getRight(), janinoEval("JSON.parse($)", pair.getLeft()));
//            assertEquals(pair.getLeft(), eval("JSON.stringify($)", pair.getRight()));
            assertEquals(pair.getLeft(), eval("JSON.stringify($)", pair.getRight()));
        }
    }

    @Test(expected = SnapDataException.class)
    public void testBadParse() {
        eval("JSON.parse('{')");
    }

    @Test(expected = SnapDataException.class)
    public void testParseNoArgs() {
        eval("JSON.parse()");
    }

    @Test
    public void testStringifyWithDateInUnixEp() {
        assertEquals("\"1466419108081\"", eval("JSON.stringify($)",
                "1466419108081"));
    }

    @Test
    public void testStringifyWithDate() {
        assertEquals("\"1995-08-09T00:00:00.000Z\"", eval("JSON.stringify($)",
                new DateTime(807926400000L, DateTimeZone.UTC)));
    }

    @Test
    public void testParseWithDate() {
        assertEquals(new DateTime(2016, 3, 22, 18, 33, 51, 156, DateTimeZone.UTC),
                eval("JSON.parse('{\"_snaptype_datetime\": \"2016-03-22T18:33:51.156 UTC\"}')"));
    }

    @Test(expected = SnapDataException.class)
    public void testBadStringify() {
        eval("JSON.stringify($)", new Object());
    }

    @Test(expected = SnapDataException.class)
    public void testStringifyNoArgs() {
        eval("JSON.stringify()");
    }

    @Test
    public void testStringifyByteArray() {
        assertEquals(String.format("\"%s\"", Base64.encodeBase64String(MSG.getBytes())),
                eval("JSON.stringify($)", MSG.getBytes(StandardCharsets.UTF_8)));
    }
}
