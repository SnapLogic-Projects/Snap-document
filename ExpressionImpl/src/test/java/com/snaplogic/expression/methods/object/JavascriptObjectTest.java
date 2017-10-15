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

import com.snaplogic.api.ExecutionException;
import com.snaplogic.expression.ExpressionTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link com.snaplogic.expression.classes.JavascriptObject}.
 *
 * @author hpatel
 */

public class JavascriptObjectTest extends ExpressionTest {

    @Test(expected = ExecutionException.class)
    public void testObjectMethodNotImplemented() {
        eval("Object.aaa()");
        Assert.fail();
    }
}
