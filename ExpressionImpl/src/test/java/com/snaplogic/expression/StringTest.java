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
import com.snaplogic.api.ExecutionException;
import com.snaplogic.expression.util.LiteralUtils;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for parsing strings in expression language.
 *
 * @author jinloes
 */
@SuppressWarnings("HardCodedStringLiteral")
public class StringTest extends ExpressionTest {

    @Test
    public void testMultilineString() {
        if (LiteralUtils.CHECK_FOR_MULTI_LINE) {
            assertEquals("Line Feed\n Hex", eval(
                    "'Line Feed\n Hex'"));
            assertEquals("com.snaplogic.expression.util.LiteralUtils.MULTI_LINE_STRING",
                    LintReporter.messages.get(LintReporter.messages.size() - 1));
        }
    }

    @Test
    public void testMultilineStringWithCorrection() {
        if (LiteralUtils.CHECK_FOR_MULTI_LINE) {
            assertEquals("Line Feed Hex", eval(
                    "'Line Feed\\\n Hex'"));
            assertEquals(true, LintReporter.messages.isEmpty());
        }
    }

    @Test
    public void testEmptyString() {
        assertEquals("", eval("\"\""));
    }

    @Test
    public void test1CharString() {
        assertEquals("a", eval("'a'"));
    }

    @Test
    public void testDoubleQuoteString() {
        assertEquals("snap", eval("\"snap\""));
    }

    @Test
    public void testDoubleQuoteStringWithEscape() {
        assertEquals("\"snap\"", eval("\"\\\"snap\\\"\""));
    }

    @Test
    public void testSingleQuoteString() {
        assertEquals("snap", eval("'snap'"));
    }

    @Test
    public void testSingleQuoteStringWithEscape() {
        assertEquals("'snap'", eval("'\\\'snap\\\''"));
    }

    @Test
    public void testSingleQuoteStringWithEscapeDoubleQuote() {
        assertEquals("\"snap\"", eval("'\\\"snap\\\"'"));
    }

    @Test
    public void testStringWithEscapes() {
        assertEquals("Backslash\\Tab\tLine Feed\nHex\042", eval(
                "'Backslash\\\\Tab\\tLine Feed\\nHex\\x22'"));
    }

    @Test(expected = ExecutionException.class)
    public void testStringWithInvalidEscape() {
        eval("'\\x2'");
    }

    @Test
    public void testNull() {
        assertEquals(null, eval("null"));
    }

    @Test
    public void testStringLength() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", ImmutableMap.of("testb", "abc123")));
        assertEquals(new BigInteger("6"), eval("$test.testa.testb.length", data));
    }

    @Test
    public void testStringLengthComparison() {
        Object data = ImmutableMap.<String, Object>of(
                "test", ImmutableMap.of("testa", ImmutableMap.of("testb", "abc123")));
        assertTrue((Boolean) eval("$test.testa.testb.length == 6", data));
    }

    @Test
    public void testStringLiteralLength() {
        assertTrue((Boolean) eval("'abc123'.length == 6"));
    }


    @Test
    public void testMongoDbQuery() {
        Object data = ImmutableMap.<String, Object>of("dateTime",
                new DateTime(2131238129771L).toDateTime(DateTimeZone.UTC));
        String query = "'{\"dateTime\": {$lte: {$date: \"' + $dateTime + '\"}}}'";
        assertEquals("{\"dateTime\": {$lte: {$date: \"2037-07-15T02:35:29.771Z\"}}}",
                eval(query, data));
    }

    @Test(expected = ExecutionException.class)
    public void testBadSyntax() {
        eval("'foo' _bar");
    }

    @Test
    public void testSprintfBool() {
        assertEquals("true", eval("'%b'.sprintf(true)"));
    }

    @Test
    public void testSprintfDate() {
        assertEquals("2012", eval("'%tY'.sprintf(LocalDate.parse('2012-07-07'))"));
        assertEquals("2012", eval("'%tY'.sprintf(LocalDateTime.parse('2012-07-07 11:22:33'))"));
        assertEquals("22", eval("'%tM'.sprintf(LocalTime.parse('11:22:33'))"));
    }

    @Test
    public void testSprintfHex() {
        assertEquals("0x55", eval("'0x%x'.sprintf(0x55)"));
    }

    @Test(expected = SnapDataException.class)
    public void testSprintfInvalidArg() {
        assertEquals("", eval("'%f'.sprintf(1)"));
    }

    @Test(expected = SnapDataException.class)
    public void testSprintfInvalidFormat() {
        assertEquals("", eval("'%z'.sprintf(1)"));
    }

    private static final List<Pair<String, String>> FROM_CHAR_CODE_TESTS = Arrays.asList(
            Pair.of("", "String.fromCharCode()"),
            Pair.of("A", "String.fromCharCode(65)"),
            Pair.of("AB", "String.fromCharCode(65, 66)"),
            Pair.of("", "String.fromCharCode('abc')"),
            Pair.of("A", "String.fromCharCode('65')"),
            Pair.of("", "String.fromCharCode(257102985710297102789)"),
            Pair.of("", "String.fromCharCode(NaN)"),
            Pair.of("a", "String.fromCharCode('a'.charCodeAt(0))")
    );

    @Test
    public void testFromCharCode() {
        for (Pair<String, String> pair : FROM_CHAR_CODE_TESTS) {
            assertEquals(pair.getLeft(), eval(pair.getRight()));
        }
    }

    @Test
    public void testIndex() {
        assertEquals("0", eval("'012345'[0]"));
        assertEquals("5", eval("'012345'[5]"));
    }

    @Test(expected = SnapDataException.class)
    public void testBadIndex() {
        eval("'012345'[-1]");
    }

    @Test(expected = SnapDataException.class)
    public void testBadIndex2() {
        eval("'012345'[23]");
    }
}
