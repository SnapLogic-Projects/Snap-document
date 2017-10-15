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

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for typeof operator.
 *
 * @author tstack
 */
public class TypeOfTest extends ExpressionTest {
    private static final List<Pair<String, String>> TEST_VALUES = Arrays.asList(
            Pair.of("boolean", "typeof true"),
            Pair.of("number", "typeof 1"),
            Pair.of("string", "typeof 'foo'"),
            Pair.of("object", "typeof { a: 1 }"),
            Pair.of("array", "typeof [1, 2, 3]"),
            Pair.of("object", "typeof null"),
            Pair.of("object", "typeof Date.now()")
    );

    @Test
    public void testTrue() {
        for (Pair<String, String> comparison : TEST_VALUES) {
            assertEquals(comparison.getLeft(), eval(comparison.getRight()));
        }
    }
}
