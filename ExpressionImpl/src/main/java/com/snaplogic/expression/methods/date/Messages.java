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

/**
 * Messages
 *
 * @author mklumpp
 */
class Messages {
    static final String DATE_DOES_NOT_HAVE_METHOD = "Date type does not have method: %s";
    static final String POSSIBLE_METHODS = "Possible methods: %s";
    static final String INVALID_MEMBER_TYPE_EXCEPTION = "Invalid member type, " +
            "expected is a date type";
    static final String INVALID_MEMBER_TYPE_RESOLUTION = "Please provide a date type by " +
            "using Date.now().toLocaleDateString()";
    static final String ARGUMENT_PARSING_RESOLUTION = "Please refer to the option section under " +
            "https://developer.mozilla" +
            ".org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/toLocaleDateString." +
            "Note: Only timeZone option is currently supported";
    static final String ARGUMENT_PARSING_EXCEPTION = "Function arguments are defined " +
            "incorrectly. Only supported arguments are {\"timeZone\":\"zoneValue\"}";
    static final String INVALID_DAY_OF_WEEK_RESOLUTION = "Expecting a value in the range [1,7]";
    static final String INVALID_ARGUMENT = "Invalid argument";
    static final String EXPECTING_A_VALID_DAY_NUMBER = "Expecting a valid day number";
    static final String EXPECTING_A_VALUE_IN_RANGE_0_23 = "Expecting a value in the range [0,23]";
    static final String EXPECTING_A_VALUE_IN_RANGE_0_999 = "Expecting a value in the range [0,999]";
    static final String EXPECTING_A_VALUE_IN_RANGE_0_59 = "Expecting a value in the range [0,59]";
    static final String EXPECTING_A_VALUE_IN_RANGE_1_12 = "Expecting a value in the range [1,12]";
}