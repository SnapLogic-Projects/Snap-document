/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.Writer;

import static com.snaplogic.expression.Messages.INVALID_HEX_ESCAPE;
import static com.snaplogic.expression.Messages.SHORT_HEX_ESCAPE;

/**
 * Translator for '\xNN' escape sequences in JavaScript string literals.
 *
 * @author tstack
 */
public class HexUnescaper extends CharSequenceTranslator {
    private static final int HEX_RADIX = 16;
    private static final int HEX_LENGTH = 2 + 2;
    private static final String HEX_PREFIX = "\\x";

    @Override
    public int translate(final CharSequence input, final int index, final Writer writer) throws
            IOException {
        if (input.charAt(index) == HEX_PREFIX.charAt(0)) {
            if (index + 1 < input.length() && input.charAt(index + 1) == HEX_PREFIX.charAt(1)) {
                if (index + HEX_LENGTH <= input.length()) {
                    CharSequence hexValue = input.subSequence(index + HEX_PREFIX.length(),
                            index + HEX_LENGTH);

                    try {
                        int value = Integer.parseInt(hexValue.toString(), HEX_RADIX);
                        writer.write((char) value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(String.format(INVALID_HEX_ESCAPE,
                                hexValue), e);
                    }
                    return HEX_LENGTH;
                } else {
                    throw new IllegalArgumentException(String.format(SHORT_HEX_ESCAPE,
                            input.subSequence(index, input.length())));
                }
            }
        }
        return 0;
    }
}
