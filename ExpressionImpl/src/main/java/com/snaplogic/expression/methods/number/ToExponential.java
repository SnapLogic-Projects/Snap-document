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
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

import static com.snaplogic.expression.methods.number.Messages.INVALID_FRACTION_DIGITS;
import static com.snaplogic.expression.methods.number.Messages.USE_A_POSITIVE_INTEGER;

/**
 * Implementation of the toExponential() method for JavaScript numbers.
 *
 * @author tstack
 */
public enum ToExponential implements Method {
    INSTANCE;

    private static final int MAX_PRECISION = 20;
    private static final String FORMAT_TEMPLATE = "0.%sE0";

    private static final ThreadLocal<DecimalFormat> EXP_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat(getFormatString(MAX_PRECISION));
        }
    };
    private static final String EXP_MINUS = "e-";
    private static final String EXP_PLUS = "e+";
    private static final String EXP_BARE = "e";

    @Override
    public Object evaluate(final Object member, final List args) {
        DecimalFormat decimalFormat = EXP_FORMAT.get();
        // Check for a fraction digit argument.
        if (!args.isEmpty() && args.get(0) instanceof Number) {
            Object fractionDigitsObj = args.get(0);
            int fractionDigits = ((Number) fractionDigitsObj).intValue();
            if (fractionDigits < 0) {
                throw new SnapDataException(INVALID_FRACTION_DIGITS)
                        .withReason(INVALID_FRACTION_DIGITS)
                        .withResolution(USE_A_POSITIVE_INTEGER);
            }
            String customFormat = getFormatString(fractionDigits);
            decimalFormat = new DecimalFormat(customFormat);
        }

        // Munge the string to make it look the same as you would get in JavaScript.
        String retval = decimalFormat.format(member).replace('E', 'e');

        if (!retval.contains(EXP_MINUS)) {
            retval = retval.replace(EXP_BARE, EXP_PLUS);
        }

        return retval;
    }

    /**
     * Construct the format string for DecimalFormat.
     *
     * @param fractionDigits The number of digits after the decimal to include.
     * @return A format string suitable for the DecimalFormat class.
     */
    private static String getFormatString(final int fractionDigits) {
        return String.format(FORMAT_TEMPLATE, StringUtils.repeat('#', fractionDigits));
    }
}
