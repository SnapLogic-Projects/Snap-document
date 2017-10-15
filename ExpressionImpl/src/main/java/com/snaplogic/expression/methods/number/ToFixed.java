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

package com.snaplogic.expression.methods.number;

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.snap.api.SnapDataException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import static com.snaplogic.expression.methods.number.Messages.INVALID_FRACTION_DIGITS;
import static com.snaplogic.expression.methods.number.Messages.USE_A_POSITIVE_INTEGER;

/**
 * Implementation of the toFixed() method for JavaScript numbers.
 *
 * @author tstack
 */
public enum ToFixed implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        int fractionDigits = 0;
        // Check for a fraction digit argument.
        if (!args.isEmpty() && args.get(0) instanceof Number) {
            Object fractionDigitsObj = args.get(0);
            fractionDigits = ((Number) fractionDigitsObj).intValue();
            if (fractionDigits < 0) {
                throw new SnapDataException(INVALID_FRACTION_DIGITS)
                        .withReason(INVALID_FRACTION_DIGITS)
                        .withResolution(USE_A_POSITIVE_INTEGER);
            }
        }

        Object converted = ObjectType.attemptToConvertToBigDecimal(member);
        if (converted instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) converted;

            return bigDecimal.setScale(fractionDigits, RoundingMode.HALF_UP).toPlainString();
        }

        // If conversion fails, then member is something like NaN or infinity.
        return ToString.INSTANCE.evaluate(member, Collections.emptyList());
    }
}
