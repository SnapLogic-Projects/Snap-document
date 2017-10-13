package com.snaplogic.util;

/**
 * Messages for com.snaplogic.util package.
 *
 * @author jinloes
 */
@SuppressWarnings({"HardCodedStringLiteral, nls"})
public class Messages {
    static final String NO_EXPRESSION_DEFINED = "No expression defined";
    static final String NO_EXPRESSION_DEFINED_REASON = "The expression field has a blank " +
            "expression.";
    static final String NO_EXPRESSION_DEFINED_RESOLUTION = "Please remove the empty expression " +
            "from the snap properties";
    static final String EXPRESSION_EVAL_MUST_RETURN_STRING = "A string object is expected to be " +
            "returned by the expression evaluation";
    static final String EXPRESSION_EVAL_MUST_RETURN_STRING_REASON = "An expression property " +
            "can only be evaluated to a string object";
    static final String EXPRESSION_EVAL_MUST_RETURN_STRING_RESOLUTION = "Please verify the " +
            "expression is defined correctly and returns a string object";
    static final String EXPRESSION_EVAL_ERROR = "An error occurred while evaluating the " +
            "expression.";
    static final String UNDEFINED_REFERENCE = "%s is undefined";
    static final String REFERENCE_NOT_FOUND = "'%s' was not found in the containing object.";
    static final String UNDEFINED_TARGET_MAP = "No target property data defined";
    static final String UNDEFINED_SOURCE_MAP = "No source property data defined";

    static final String INCORRECT_JSON_PATH = "The provided JSON path %s is incorrect.";
    static final String INVALID_COLLECTION_OBJECT = "Only List and Map are supported" +
            " collection objects. Provided is %s";
    static final String NO_COLLECTION_OBJECT = "No collection object provided.";
    static final String NO_JSON_PATH = "No json path provided.";
    static final String PLEASE_CHECK_YOUR_EXPRESSION = "Please check expression syntax " +
            "and data types.";
    static final String INVALID_DATE_TIME_FORMAT = "Invalid datetime format %s";
    static final String INVALID_DATE_TIME_ZONE_FORMAT = "Invalid datetime zone format %s";
    static final String ERR_NULL_OBJECT = "Deep copy should never be called for a null object";

    private Messages() {
    }
}
