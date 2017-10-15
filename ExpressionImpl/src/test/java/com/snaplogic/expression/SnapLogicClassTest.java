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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the functions in the 'sl' object.
 *
 * @author tstack
 */
public class SnapLogicClassTest extends ExpressionTest {
    private static final Map<String, Object> ENV_DATA = new HashMap<String, Object>() {{
        put("foo", "$bar");
    }};

    @Test
    public void testTemplatize() {
        assertEquals("foo $bar[0].baz['foo']",
                eval("sl.templatize(\"'foo ' + $bar[0].baz['foo']\")"));
//        assertEquals("foo $bar[0].baz['foo']",
    }

    @Test
    public void testTemplatizeWithArgs() {
        assertEquals("foo $bar[0].baz['foo'] 0",
                eval("sl.templatize(\"'foo ' + $bar[0].baz['foo'] + ' ' + arguments[0]\", 0)"));
//                "+ $bar[0].baz['foo'] + ' ' + arguments[0]\", 0)"));
    }

    @Test
    public void testTemplatizeWithNesting() {
        assertEquals("foo $bar",
                eval("sl.templatize(\"'foo ' + sl.templatize(_foo)\")", new HashMap<>(),
                        ENV_DATA));
//        assertEquals("foo $bar",
//                        ENV_DATA));
    }

    @Test
    public void testZip() {
        assertEquals(Arrays.asList(
                Arrays.asList(new BigInteger("1"), new BigInteger("3")),
                Arrays.asList(new BigInteger("2"), new BigInteger("4"))
        ), eval("sl.zip([1, 2], [3, 4])"));
    }

    @Test
    public void testZipNoArgs() {
        assertEquals(Collections.emptyList(), eval("sl.zip()"));
    }

    @Test
    public void testZipMismatchedLength() {
        assertEquals(Arrays.asList(
                Arrays.asList(new BigInteger("1"), new BigInteger("2"))
        ), eval("sl.zip([1], [2, 3])"));
    }

    @Test
    public void testZipEmptyArg() {
        assertEquals(Collections.emptyList(), eval("sl.zip([], [2, 3])"));
    }

    @Test(expected = SnapDataException.class)
    public void testZipNonListArg() {
        eval("sl.zip([1, 2], 1)");
    }

    @Test
    public void testRange() {
        assertEquals(Arrays.asList(
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(2)
        ), eval("sl.range(3)"));
    }

    @Test
    public void testRangeWithZero() {
        assertEquals(Collections.emptyList(), eval("sl.range(0)"));
    }

    @Test
    public void testRangeWithStart() {
        assertEquals(Arrays.asList(
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(2)
        ), eval("sl.range(1, 3)"));
    }

    @Test
    public void testRangeWithStartAndStep() {
        assertEquals(Arrays.asList(
                BigDecimal.valueOf(1)
        ), eval("sl.range(1, 3, 2)"));
    }

    @Test
    public void testRangeWithReversedStartAndStop() {
        assertEquals(Collections.emptyList(), eval("sl.range(2, 1)"));
    }

    @Test
    public void testRangeWithSameStartAndStop() {
        assertEquals(Collections.emptyList(), eval("sl.range(1, 1)"));
    }

    @Test
    public void testRangeWithSameStartAndStopInReverse() {
        assertEquals(Collections.emptyList(), eval("sl.range(1, 1, -1)"));
    }

    @Test(expected = SnapDataException.class)
    public void testRangeWithNoArgs() {
        assertEquals(Collections.emptyList(), eval("sl.range()"));
    }

    @Test
    public void testRangeEmpty() {
        assertEquals(Collections.emptyList(), eval("sl.range(3, 0)"));
    }

    @Test
    public void testRangeInReverse() {
        assertEquals(Arrays.asList(
                BigDecimal.valueOf(3),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(1)
        ), eval("sl.range(3, 0, -1)"));
    }

    @Test
    public void testRangeWithNegativeStart() {
        assertEquals(Arrays.asList(
                BigDecimal.valueOf(-3),
                BigDecimal.valueOf(-2),
                BigDecimal.valueOf(-1)
        ), eval("sl.range(-3, 0)"));
    }

    @Test
    public void testRangeWithBigNumber() {
        assertEquals(Arrays.asList(
                new BigDecimal("10000000000000000000000000000000")
        ), eval("sl.range(10000000000000000000000000000000, 10000000000000000000000000000001)"));
    }

    @Test(expected = SnapDataException.class)
    public void testRangeWithExtraArgs() {
        eval("sl.range(3, 0, -1, 0, 1)");
    }

    @Test(expected = SnapDataException.class)
    public void testRangeWithZeroStep() {
        eval("sl.range(3, 0, 0)");
    }

    @Test
    public void testGetHostByName() throws Exception {
        InetAddress[] inetAddresses = InetAddress.getAllByName("snaplogic.com");
        List<Object> expected = new ArrayList<>();
        for (InetAddress inetAddress : inetAddresses) {
            expected.add(ImmutableMap.builder()
                    .put("address", inetAddress.getHostAddress())
                    .put("family", "ipv4")
                    .put("hostname", "snaplogic.com")
                    .build());
        }
        assertEquals(expected, eval("sl.gethostbyname('snaplogic.com')"));
        inetAddresses = InetAddress.getAllByName(inetAddresses[0].getHostAddress());
        expected.clear();
        for (InetAddress inetAddress : inetAddresses) {
            expected.add(ImmutableMap.builder()
                    .put("address", inetAddress.getHostAddress())
                    .put("family", "ipv4")
                    .put("hostname", inetAddress.getHostName())
                    .build());
        }
        assertEquals(expected, eval("sl.gethostbyname($)", inetAddresses[0].getHostAddress()));
    }

    @Test
    public void testGetHostByNameWithBadName() throws Exception {
        assertEquals(Collections.emptyList(), eval(
                "sl.gethostbyname('non-existent.domain.name.abcdefghij')"));
    }

    @Test(expected = SnapDataException.class)
    public void testGetHostByNameWithNoArgs() throws Exception {
        eval("sl.gethostbyname()");
    }

    @Test
    public void testEnsureArrayWithString() throws Exception {
        assertEquals(Arrays.asList("foo"), eval("sl.ensureArray('foo')"));
    }

    @Test
    public void testEnsureArrayWithList() throws Exception {
        assertEquals(Arrays.asList("foo"), eval("sl.ensureArray(['foo'])"));
    }

    @Test(expected = SnapDataException.class)
    public void testEnsureArrayWithNoArgs() throws Exception {
        eval("sl.ensureArray()");
    }

    @Test(expected = SnapDataException.class)
    public void testEnsureArrayWithTwoArgs() throws Exception {
        eval("sl.ensureArray(1, 2)");
    }
}
