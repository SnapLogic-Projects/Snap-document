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

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the encodeURIComponent/decodeURIComponent JavaScript functions.
 *
 * @author tstack
 */
public class URICodecTest extends ExpressionTest {
    private static final List<Pair<String, String>> VALUES = Arrays.asList(
            Pair.of("abc123", "abc123"),
            Pair.of("abc%20123", "abc 123"),
            Pair.of("abc%20123%25%2F%3C", "abc 123%/<")
    );

    @Test
    public void testCodec() {
        for (Pair<String, String> pair : VALUES) {
            assertEquals(pair.getLeft(), eval(String.format("encodeURIComponent('%s')",
                    pair.getRight())));
            assertEquals(pair.getRight(), eval(String.format("decodeURIComponent('%s')",
                    pair.getLeft())));
        }
    }
}
