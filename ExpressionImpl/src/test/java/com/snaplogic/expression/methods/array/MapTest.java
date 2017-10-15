/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods.array;

import com.google.common.collect.Lists;
import com.snaplogic.expression.ExpressionTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests for javascript array map method.
 *
 * @author tstack
 */
@SuppressWarnings("HardCodedStringLiteral")
public class MapTest extends ExpressionTest {
    @Test
    public void basic() {
        assertEquals(Lists.newArrayList(BigDecimal.valueOf(10), BigDecimal.valueOf(20)),
                eval("[1, 2].map(x => x * 10)"));
    }

    @Test
    public void globalFunc() {
        assertEquals(Arrays.asList(true, false), eval("['abc', 20].map(isNaN)"));
    }

    @Test
    public void classFunc() {
        assertEquals(Arrays.asList(BigDecimal.valueOf(2), BigDecimal.valueOf(3)),
                eval("[4, 9].map(Math.sqrt)"));
    }
}
