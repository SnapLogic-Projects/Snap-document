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

import com.snaplogic.expression.methods.Method;
import com.snaplogic.snap.api.SnapDataException;

import java.math.BigInteger;
import java.util.List;

import static com.snaplogic.expression.methods.number.Messages.*;

/**
 * Implements javascript Object toString method:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/
 * Object/toString
 *
 * @author tstack
 */
public enum ToString implements Method {
    INSTANCE;
    private static final String NAN = "NaN";
    private static final String INFINITY = "Infinity";
    private static final int DEFAULT_RADIX = 10;

    public Object evaluate(final Object member, final List args) {
        int radix = DEFAULT_RADIX;

        if (args != null && !args.isEmpty()) {
            Object radixObj = args.get(0);
            if (radixObj instanceof Number) {
                radix = ((Number) args.get(0)).intValue();
            } else {
                radix = -1;
            }
            if (radix < 2 || radix > 36) {
                throw new SnapDataException(INVALID_RADIX_VALUE)
                        .formatWith(radixObj)
                        .withReason(RADIX_VALUE_IS_OUT_OF_RANGE)
                        .withResolution(RADIX_VALUE_MUST_BE_BETWEEN_2_AND_36);
            }
        }

        if (member instanceof Double) {
            if (member.equals(Double.NaN)) {
                return NAN;
            } else if (member.equals(Double.NEGATIVE_INFINITY) ||
                    member.equals(Double.POSITIVE_INFINITY)) {
                return INFINITY;
            }
        } else if (member instanceof Float) {
            if (member.equals(Float.NaN)) {
                return NAN;
            } else if (member.equals(Float.NEGATIVE_INFINITY) ||
                    member.equals(Float.POSITIVE_INFINITY)) {
                return INFINITY;
            }
        }

        if (radix == DEFAULT_RADIX) {
            return member.toString();
        }

        BigInteger bigInteger;
        if (member instanceof BigInteger) {
            bigInteger = (BigInteger) member;
        } else {
            // We're not really handling floating point numbers here...
            try {
                bigInteger = new BigInteger(member.toString());
            } catch (NumberFormatException e) {
                bigInteger = BigInteger.valueOf(((Number) member).longValue());
            }
        }

        return bigInteger.toString(radix);
    }
}
