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

package com.snaplogic.jsonpath;

import org.apache.commons.lang3.StringUtils;

import static com.snaplogic.jsonpath.Messages.FIELD_NOT_FOUND_IN_JSON_OBJECT;

/**
 * Exception thrown when a requested field in a JSON object could not be found.
 *
 * @author tstack
 */
public class FieldNotFoundException extends PathNotFoundException {
    private final String field;

    public FieldNotFoundException(final String field, final String message) {
        this(field, message, null);
    }

    public FieldNotFoundException(final String field, final String message,
            final Throwable cause) {
        super(message, cause);
        this.field = field;
    }

    @Override
    public String getMessage() {
        String retval = super.getMessage();
        if (StringUtils.isEmpty(retval)) {
            // Formatting this message can be expensive, so we delay it until it's needed.
            retval = String.format(FIELD_NOT_FOUND_IN_JSON_OBJECT, field);
        }
        return retval;
    }

    public String getField() {
        return this.field;
    }
}
