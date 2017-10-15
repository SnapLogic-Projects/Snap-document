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
import com.snaplogic.snap.api.SnapDataException;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;

import java.util.List;

import static com.snaplogic.expression.methods.date.Messages.EXPECTING_A_VALID_DAY_NUMBER;
import static com.snaplogic.expression.methods.date.Messages.INVALID_ARGUMENT;

/**
 * Extension of the JavaScript Date object with this Joda method:
 * {@see http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html#withDayOfYear(int)}
 *
 * @author tstack
 */
public enum WithDayOfYear implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        DateTime dateTime = DateMethod.convertMember(member);

        try {
            return dateTime.withDayOfYear(MethodUtils.getArgAsNumber(args, 0).intValue());
        } catch (IllegalFieldValueException e) {
            throw new SnapDataException(e, INVALID_ARGUMENT)
                    .withReason(e.getMessage())
                    .withResolution(EXPECTING_A_VALID_DAY_NUMBER);
        }
    }
}
