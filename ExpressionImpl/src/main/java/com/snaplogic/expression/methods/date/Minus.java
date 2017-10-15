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
import com.snaplogic.expression.methods.MethodUtils;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Extension of the JavaScript Date object with this Joda method:
 * {@see http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html#minus(long)}
 *
 * @author tstack
 */
public enum Minus implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        DateTime dateTime = DateMethod.convertMember(member);

        return dateTime.minus(MethodUtils.getArgAsNumber(args, 0).longValue());
    }
}
