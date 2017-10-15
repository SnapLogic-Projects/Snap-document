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

package com.snaplogic.expression.methods.object;

import com.snaplogic.expression.ExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link com.snaplogic.expression.methods.map.Extend}.
 *
 * @author tolyva
 */

public class IsEmptyTest extends ExpressionTest {

    @Test
    public void testIsEmptyFalse() {
        assertFalse((Boolean)eval("{first:'Joe'}.isEmpty()"));
    }

    @Test
    public void testIsEmptyTrue() {
        assertTrue((Boolean)eval("{}.isEmpty()"));
    }

}
