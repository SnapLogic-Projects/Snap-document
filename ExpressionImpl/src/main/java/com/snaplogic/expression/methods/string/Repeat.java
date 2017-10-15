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

package com.snaplogic.expression.methods.string;

import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.MethodUtils;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.snaplogic.expression.methods.string.Messages.*;

/**
 * Implements the javascript string repeat method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/repeat
 *
 * @author tstack
 */
public enum Repeat implements Method {
    INSTANCE;

    @Override public Object evaluate(final Object member, final List args) {
        String str = (String) member;
        int count = MethodUtils.getArgAsBigDecimal(args, 0).intValue();
        if (count < 0) {
            throw new SnapDataException(INVALID_COUNT_ARGUMENT_FOR_REPEAT)
                    .formatWith(count)
                    .withReason(ARGUMENT_IS_LESS_THAN_ZERO)
                    .withResolution(REPEAT_RESOLUTION);
        }

        return StringUtils.repeat(str, count);
    }
}
