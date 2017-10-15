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

package com.snaplogic.expression.methods.string;

/**
 * Messages for package com.snaplogic.expression.methods.string
 *
 * @author jinloes
 */
class Messages {
    static final String EVALUATE_EXPRESSION_FAILED = "Failed to evaluate expression.";
    static final String STRING_DOES_NOT_HAVE_METHOD = "String type does not have method: %s";
    static final String POSSIBLE_METHODS = "Possible methods: %s";
    static final String INVALID_FORMAT_FOR_ARGUMENTS = "Invalid format for arguments: %s -- %s";
    static final String MAKE_SURE_THE_ARGS_ARE_COMPATIBLE = "Make sure the" +
            " format string and arguments are compatible";
    static final String PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX = "Please check your expression syntax";
    static final String UNABLE_TO_COMPILE_JS_EXPRESSION = "Unable to compile JS expression: %s";
    static final String UNEXPECTED_JAVA_SCRIPT_ENGINE_RESULT = "Unexpected JavaScript engine " +
            "result: %s";
    static final String INVALID_COUNT_ARGUMENT_FOR_REPEAT = "Invalid count argument for repeat: %s";
    static final String ARGUMENT_IS_LESS_THAN_ZERO = "Argument is less than zero";
    static final String REPEAT_RESOLUTION = "The repeat() method expects a value greater than or " +
            "equal to zero";

    static final String INVALID_ARGUMENTS_NUMBER_FOR_METHOD = "Invalid number of arguments for " +
            "%s: %s";
    static final String ARGUMENTS_NUMBER_IS_NOT_APPROPRIATE = "Exactly %s arguments are expected";
    static final String REPLACE_ALL_RESOLUTION = "The replaceAll() method expects exact two " +
            "arguments: The first argument is the search string and the second one is the " +
            "replace text.";
}
