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

package com.snaplogic.jsonpath.tokens;

/**
 * Token used to track the comma and colons used in the union
 * and array-slice notations.  This token is only used during
 * parsing and should not end up in the final tokenization
 * of a path.  The UnionToken and SliceToken classes are used
 * for the final path.
 *
 * @author tstack
 */
public class SeparatorToken extends PathToken {
    private final char separator;

    /**
     * @param separator A single character separator that can be
     *                  used in a JSON-path, usually a comma or
     *                  a colon for specifying a union or
     *                  slice, respectively.
     */
    public SeparatorToken(char separator) {
        this.separator = separator;
    }

    public char getSeparator() {
        return separator;
    }

    @Override
    public int hashCode() {
        return separator;

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeparatorToken)) {
            return false;
        }

        final SeparatorToken that = (SeparatorToken) o;

        return this.separator == that.separator;
    }

    @Override
    public String toString() {
        return Character.toString(separator);
    }
}
