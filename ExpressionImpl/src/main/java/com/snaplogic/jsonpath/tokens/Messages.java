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

package com.snaplogic.jsonpath.tokens;

/**
 * Messages used by the classes.
 *
 * @author tstack
 */
class Messages {
    static final String EXPECTING_BOOLEAN = "Expecting boolean result for filter expression: %s";
    static final String EXPECTED_OBJ_OR_ARRAY = "Expecting object or array for wildcard path, " +
            "found: %s";
    static final String DIFFERENT_TYPES = "Union subscript values are of differing types: %s";
    static final String EXPECTING_ARRAY = "Expecting array for JSON-Path array indexes, found: %s";
    static final String EXPECTING_OBJECT = "Expecting object for JSON-Path field reference, " +
            "found: %s";
    static final String EXPECTED_OBJ_OR_ARRAY_FOR_FILTER = "Expecting object or array for " +
            "filtered path: %s; found: %s";
    static final String EXPRESSION_VALUE_CANNOT_BE_NULL = "Expression value cannot be null";
    static final String FILTER_NEEDS_BOOL_RESOLUTION = "Change the expression so that it " +
            "evaluates to a boolean";
    static final String CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_OBJECT_OR_ARRAY_VALUE = "Change " +
            "the preceding path to refer to an object or array value";
    static final String UNION_SUBSCRIPT_VALUES_MUST_BE_THE_SAME_TYPE = "Union subscript values " +
            "must be the same type";
    static final String CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_ARRAY = "Change the preceding " +
            "path to refer to an array";
    static final String CHANCE_THE_PRECEDING_PATH_TO_REFER_TO_AN_OBJECT = "Chance the preceding " +
            "path to refer to an object";
    static final String UNABLE_TO_CONVERT_INT = "Unable to convert value to an integer: %s";
    static final String ENTER_AN_INTEGER_VALUE_FOR_THE_SLICE_EXPR = "Enter an integer" +
            " value for the array-slice expression";
    static final String SLICE_VALUE_NOT_AN_INTEGER = "Array-slice value is not an integer: %s";
    static final String SLICE_STEP_CANNOT_BE_ZERO = "Slice step cannot be zero";
    static final String USE_A_POSITIVE_OR_NEGATIVE_NUMBER_FOR_THE_STEP = "Use a positive or " +
            "negative number for the step";
    static final String EXPECTING_A_STRING_AS_GROUP_KEY = "Expecting a string or primitive type " +
            "as the group key";
    static final String CHANGE_THE_EXPRESSION_TO_RETURN_A_STRING = "Change the expression to " +
            "return a string";
    static final String INVALID_SORT_EXPRESSION = "Invalid sort expression";
    static final String EXPECT_AN_ARRAY_TO_SORT = "Expecting an array to sort, found: %s";
}
