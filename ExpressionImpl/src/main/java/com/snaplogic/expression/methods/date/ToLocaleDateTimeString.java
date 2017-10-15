/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013 - 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.expression.methods.date;

import com.snaplogic.expression.methods.Method;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

import static com.snaplogic.expression.methods.date.Messages.INVALID_MEMBER_TYPE_EXCEPTION;
import static com.snaplogic.expression.methods.date.Messages.INVALID_MEMBER_TYPE_RESOLUTION;

/**
 * Supports the conversion of a {@link DateTime} object into a
 * {@link org.joda.time.LocalDateTime} object
 *
 * @author mklumpp
 */
public enum ToLocaleDateTimeString implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        // TODO - MK: implement rest of arguments as defined for the function in javascript
        //            Currently we only support timeZone argument
        if (member instanceof DateTime) {
            Map<String, Object> params = DateMethod.parseArguments(args);
            DateTime dateTime = DateMethod.createBaseDateTime(member, params);
            if (params != null) {
                String format = (String) params.get(DateMethod.DATE_FORMAT);
                if (!StringUtils.isBlank(format)) {
                    return dateTime.toLocalDateTime().toString(format);
                }
                return dateTime.toLocalDateTime().toString();
            }
            return ((DateTime) member).toLocalDateTime().toString();
        }
        throw new SnapDataException(INVALID_MEMBER_TYPE_EXCEPTION)
                .withReason(INVALID_MEMBER_TYPE_EXCEPTION)
                .withResolution(INVALID_MEMBER_TYPE_RESOLUTION);
    }
}