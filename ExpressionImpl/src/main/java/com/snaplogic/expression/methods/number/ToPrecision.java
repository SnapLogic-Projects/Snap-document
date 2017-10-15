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
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.List;

import static com.snaplogic.expression.methods.number.Messages.INVALID_FRACTION_DIGITS;
import static com.snaplogic.expression.methods.number.Messages.USE_A_POSITIVE_INTEGER;

/**
 * Implementation of the toPrecision() method for JavaScript numbers.
 *
 * @author tstack
 */
public enum ToPrecision implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        if (args.isEmpty()) {
            return ToString.INSTANCE.evaluate(member, args);
        }
        int significantDigits = 0;
        // Check for a fraction digit argument.
        if (args.get(0) instanceof Number) {
            Object fractionDigitsObj = args.get(0);
            significantDigits = ((Number) fractionDigitsObj).intValue();
            if (significantDigits < 0) {
                throw new SnapDataException(INVALID_FRACTION_DIGITS)
                        .withReason(INVALID_FRACTION_DIGITS)
                        .withResolution(USE_A_POSITIVE_INTEGER);
            }
        }

        Object converted = ObjectType.attemptToConvertToBigDecimal(member);
        if (converted instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) converted;

            if (bigDecimal.precision() > significantDigits) {
                BigDecimal rounded = bigDecimal.round(new MathContext(significantDigits));
                // BigDecimal uses capital 'E', while JavaScript uses lowercase 'e'.
                return rounded.toString().replace('E', 'e');
            } else {
                // BigDecimal doesn't want to add trailing zeroes, so we need to do it ourselves.
                int trailingZeroCount = significantDigits - bigDecimal.precision();
                return bigDecimal.toPlainString() + StringUtils.repeat('0', trailingZeroCount);
            }
        }

        // If conversion fails, then member is something like NaN or infinity.
        return ToString.INSTANCE.evaluate(member, Collections.emptyList());
    }
}
