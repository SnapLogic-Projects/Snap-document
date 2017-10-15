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

package com.snaplogic.expression.methods.date;

import com.snaplogic.expression.methods.Method;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.math.BigInteger;
import java.util.List;

/**
 * Implementation of
 * {@see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date
 * /getDay}
 *
 * @author tstack
 */
public enum GetDay implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        DateTime dateTime = DateMethod.convertMember(member);
        int retval = dateTime.getDayOfWeek();

        if (retval == DateTimeConstants.SUNDAY) {
            retval = 0;
        }
        return BigInteger.valueOf(retval);
    }
}
