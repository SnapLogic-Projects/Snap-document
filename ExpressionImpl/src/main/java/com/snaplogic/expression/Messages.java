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

/**
 * Messages for package com.snaplogic.expression.
 *
 * @author jinloes
 */
class Messages {
    static final String MATH_OPERATOR_TYPE_MISMATCH = "%s operator expects types Number, " +
            "Number: Found %s, %s";
    static final String MATH_OPERATOR_REQUIRES_NUMBERS = "%s operator requires numbers.";
    static final String BOOLEAN_OPERATOR_EXPECTS = "%s operator expects Boolean, Boolean. " +
            "Found types: %s, %s";
    static final String BOOLEAN_OPERATOR_REQUIRES = "%s operator requires Boolean operands.";
    static final String CHECK_OPERAND_TYPES = "Please check the operand types.";
    static final String PLEASE_CHECK_YOUR_EXPRESSION = "Please check expression syntax " +
            "and data types.";
    static final String PROPERTY_LENGTH = "length";
    static final String OPERATOR_NOT_SUPPORTED = "Operator %s is not supported.";
    static final String INDEX_ACCESS_ON_NON_LIST = "Trying to perform index-based access on a " +
            "non list type. Found type: %s";
    static final String EVAL_ACCESSOR_FAILED = "Could not evaluate accessor: %s";
    static final String KEY_ACCESS_ON_NON_KEY_VALUE = "Trying to access key mapping on non " +
            "key/value type. Found type: %s";
    static final String CANNOT_COMPARE_VALUE_TO_NUMBER = "Cannot compare value %s to number";
    static final String PLEASE_CHECK_EXPRESSION_SYNTAX = "Please check expression syntax";
    static final String COULD_NOT_COMPILE_EXPRESSION = "Could not compile expression: %s ";
    static final String GLOBAL_FUNCTION_NOT_SUPPORTED = "Global function is not supported.";
    static final String INVALID_TOKEN = "Invalid token: '%s' for expression: %s";
    static final String UTF_8_ENCODING_NOT_FOUND = "UTF-8 encoding not found";
    static final String SHORT_HEX_ESCAPE = "Less than two digits in hex value: %s";
    static final String INVALID_HEX_ESCAPE = "Unable to parse hex value: %s";
    static final String ENCOUNTERED_EXTRANEOUS_INPUT = "Encountered extraneous input %s at line " +
            "%d:%d; expecting=%s";
    static final String INVALID_ACCESSOR_IN_PATH = "Invalid accessor (%s) in path: %s";
    static final String EXPECTING_AN_INTEGER_TO_ACCESS_AN_ARRAY_ELEMENT = "Expecting an integer " +
            "to access an array element";
    static final String EXPECTING_A_STRING_TO_ACCESS_AN_OBJECT_FIELD = "Expecting a string to " +
            "access an object field";
    static final String BAD_ARRAY_INDEX = "Index out-of-bounds for array: %s; " +
            "length: %s";
    static final String ARRAY_DOES_NOT_CONTAIN_THE_GIVEN_INDEX = "Array does not contain the " +
            "given index";
    static final String INVALID_JSON_PATH = "Invalid JSON-path: %s";
    static final String EXPECTING_TYPE_FOR_INSTANCEOF = "Expecting a type for the right-hand-side" +
            " of 'instanceof', found: %s";
    static final String THE_INSTANCEOF_OPERATOR_REQUIRES_A_TYPE = "The instanceof operator " +
            "requires a type";
    static final String INSTANCEOF_RESOLUTION = "Supply a type object, like: Null, String, " +
            "Number, Boolean, Date";
    static final String MISSING_VARIABLE = "Missing variable %s";
    static final String UNSUPPORTED_COMPARISON = "%s to %s comparison is unsupported.";
    static final String UNSUPPORTED_MATH_OR_ARITH = "%s %s %s is unsupported.";
    static final String UNSUPPORTED_COMPARISON_OPERATOR = "Encountered unsupported comparison" +
            " operator.";
    static final String CANNOT_SUBTRACT_STRINGS = "You cannot subtract non-number strings from " +
            "numbers or number strings.";
    static final String UNSUPPORTED_UNARY = "Encountered unsupported unary operator";
    static final String NEGATE_NON_NUMBER_STRING_FAIL = "Cannot negate a non number string.";
    static final String UNSUPPORTED_NEGATION = "Attempted to negate an unsupported object.";
    static final String UNSUPPORTED_NOT = "Attempted to not an unsupported object.";
    static final String UNSUPPORTED_OPERATION = "Encountered unsupported operation.";
    static final String MULTIPLY_NON_NUMBER_STRING_FAIL = "You cannot multiply non number strings.";
    static final String FLOOR_NON_NUMBER_STRING_ERROR = "Floor does not support non-number strings";
    static final String CANNOT_CAST_STRING_TO_NUMBER = "Cannot cast a non-number %s to a number";
    static final String CANNOT_COMPARE = "Cannot compare %s and %s";
    static final String UNSUPPORTED_METHOD = "Unsupported method: %s";
    static final String METHOD_NOT_SUPPORTED_FOR = "You cannot call %s on a %s";
    static final String PROPERTY_NOT_SUPPORTED_FOR = "You cannot access %s on a %s";
    static final String NULL_MEMBER_SUFFIX = "Encountered null member suffix.";
    static final String BAD_ARGS_TO = "%s has been given bad arguments";
    static final String INVALID_FRACTION_DIGITS = "toExponential() argument must be greater than " +
            "or equal to zero";
    static final String USE_A_POSITIVE_INTEGER = "Use a positive integer as" +
            " the first argument";
    static final String UNSUPPORTED_PROPERTY = "Unsupported property:%s %s";
    static final String METHOD_RAISED_EXCEPTION = "Encountered an error while executing the " +
            "method: %s";
    static final String BAD_STRING_INDEX = "Index out-of-bounds for string: %s; " +
            "length=%s";
    static final String STRING_DOES_NOT_CONTAIN_THE_GIVEN_INDEX = "String does not contain the " +
            "given index";
    static final String EXPECTING_AN_INTEGER_TO_ACCESS_A_STRING_ELEMENT = "Expecting an integer " +
            "to access a character in a string";
    static final String GLOBAL_OBJ_CANNOT_BE_CHANGED = "The object is globally shared data and " +
            "cannot be modified";
    static final String GLOBAL_ARRAY_CANNOT_BE_CHANGED = "The array is globally shared data and " +
            "cannot be modified";
    static final String COPY_OBJ_RESOLUTION = "Make a copy of this object with the '.extend()' " +
            "method";
    static final String COPY_ARRAY_RESOLUTION = "Make a copy of this array with the '.slice()' " +
            "method";

    private Messages() {
    }
}
