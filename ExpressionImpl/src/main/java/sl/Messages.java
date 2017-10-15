package sl;

/**
 * Messages for the util package.
 *
 * @author tstack
 */
public class Messages {
    static final String MATH_OPERATOR_TYPE_MISMATCH = "%s operator expects types Number, " +
            "Number: Found %s, %s";
    static final String MATH_OPERATOR_REQUIRES_NUMBERS = "%d operator requires numbers.";
    static final String BOOLEAN_OPERATOR_EXPECTS = "%s operator expects Boolean, Boolean. " +
            "Found types: %s, %s";
    static final String BOOLEAN_OPERATOR_REQUIRES = "%s operator requires Boolean operands.";
    static final String CHECK_OPERAND_TYPES = "Please check the operand types.";
    static final String PLEASE_CHECK_YOUR_EXPRESSION = "Please check expression syntax " +
            "and data types.";
    static final String OPERATOR_NOT_SUPPORTED = "Operator %d is not supported.";
    static final String INDEX_ACCESS_ON_NON_LIST = "Trying to perform index-based access on a " +
            "non list type. Found type: %s";
    static final String EVAL_ACCESSOR_FAILED = "Could not evaluate accessor: %s";
    static final String KEY_ACCESS_ON_NON_KEY_VALUE = "Trying to access key mapping on non " +
            "key/value type. Found type: %s";
    static final String CANNOT_COMPARE_VALUE_TO_DATE = "Cannot compare value %s to date";
    static final String TYPE_NOT_SUPPORTED_FOR_COMPARISON = "Type %s not supported for comparison.";
    static final String PLEASE_CHECK_EXPRESSION_SYNTAX = "Please check expression syntax";
    static final String GLOBAL_FUNCTION_NOT_SUPPORTED = "Global function is not supported.";
    static final String UTF_8_ENCODING_NOT_FOUND = "UTF-8 encoding not found";
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
    static final String PROPERTY_LENGTH = "length";
    static final String BAD_STRING_INDEX = "Index out-of-bounds for string: %s; length: %s";
    static final String STRING_DOES_NOT_CONTAIN_THE_GIVEN_INDEX = "String does not contain the " +
            "given index";
}

