package com.snaplogic.jsonpath;

/**
 * Messages used by the classes.
 *
 * @author tstack
 */
public class Messages {
    static final String UNTERMINATED_STRING_CONSTANT = "Unterminated string constant: %s";
    static final String EMPTY_SUBSCRIPT_COMPONENT_IN_PATH = "Empty subscript component in path";
    static final String EXPECTING_EXPRESSION_IN_SUBSCRIPT = "Expecting a JavaScript expression in" +
            " a subscript: %s";
    static final String UNTERMINATED_SUBSCRIPT_COMPONENT_IN_PATH = "Unterminated subscript " +
            "component in path: %s";
    static final String UNEXPECTED_TOKEN_IN_PATH = "Unexpected token in path: %s";
    static final String EXPECTING_EXPRESSION_WITH_PAREN = "Expecting a JavaScript " +
            "expression surrounded by parentheses: %s";
    static final String UNTERMINATED_SUBSCRIPT_COMPONENT_IN_EXPRESSION = "Unterminated subscript " +
            "component in expression: %s";
    static final String UNTERMINATED_PARENTHESIS_IN_EXPRESSION = "Unterminated parenthesis in " +
            "expression: %s";
    static final String JSON_PATH_ARRAY_INDEX_IS_NEGATIVE = "JSON-Path array index is negative";
    static final String JSON_PATH_ARRAY_INDEX_IS_TOO_LARGE = "JSON-Path array index is too " +
            "large: %d (array size is: %d)";
    static final String FIELD_NOT_FOUND_IN_JSON_OBJECT = "Field not found in JSON object: %s";
    static final String EXPECTING_ARRAY_FOR_JSON_PATH_ARRAY_INDEX = "Expecting array for " +
            "JSON-Path array index, found: %s";
    static final String EXPECTING_OBJECT = "Expecting object for JSON-Path field reference '%s'," +
            " found: %s";
    static final String UNSUPPORTED_TOKEN_IN_PATH = "Unsupported token in path: %s";
    static final String UNEXPECTED_QUOTE = "Unexpected quote: %s";
    static final String QUOTES_IN_BRACKETS = "Quoted identifiers should be surrounded by " +
            "brackets (e.g. $.example['key'])";
    static final String FIELD_TO_DELETE_DOES_NOT_EXIST = "Field to delete does not exist: %s";
    static final String STATICALLY_DEFINED_JSON_PATH_IS_INVALID = "Statically-defined JSON-Path " +
            "is invalid: %s";
    static final String UNTERMINATED_RESOLUTION = "String constant must end with a (%c) character";
    static final String UNTERMINATED_SUBSCRIPT_RESOLUTION = "Subscripts must be terminated with a" +
            " right-bracket (]) character.";
    static final String UNTERMINATED_EXPR_RESOLUTION = "Expressions must be terminated with " +
            "a close-parenthesis ')' character.";
    static final String EMPTY_SUBSCRIPT_RESOLUTION = "Insert an integer index, field name, " +
            "expression or filter";
    static final String UNEXPECTED_RIGHT_BRACKET_RESOLUTION = "Remove the character or add the " +
            "missing ([) character";
    static final String UNEXPECTED_EXPR_RESOLUTION = "Surround expressions with a subscript " +
            "component (e.g. [(1 + 1)])";
    static final String FILTER_WITHOUT_EXPR_RESOLUTION = "Filter expressions should be followed " +
            "by an expression (e.g. [?($.foo == 'bar')]";
    static final String READ_STATIC_PATH_FAILED = "Unable to read static data from a statically " +
            "defined path";
    static final String ENSURE_THE_INDEX_IS_WITHIN_THE_ARRAY_BOUNDS = "Ensure the index is within" +
            " the array bounds";
    static final String CHECK_THE_JAVA_SCRIPT_EXPRESSION = "Check the JavaScript expression";
    static final String CHANGE_THE_PATH_TO_REFER_TO_AN_ARRAY = "Change the path to refer to an " +
            "array";
    static final String CHANGE_THE_PATH_TO_REFER_TO_AN_OBJECT = "Change the path to refer to an " +
            "object";
    static final String ROOT_OVERWRITE_TYPE_MISMATCH = "The root document cannot be overwritten " +
            "with a different type of data";
    static final String WRITE_THE_SAME_TYPE_OF_DATA_TO_THE_ROOT = "Write the same type of data to" +
            " the root";
    static final String A_JSON_PATH_CANNOT_END_WITH_A_DESCENT_TOKEN = "A JSON-Path cannot end " +
            "with a descent token";
    static final String APPEND_A_FIELD_NAME_OR_ARRAY_INDEX = "Append a field name or array index";
    static final String UNION_AND_ARRAY_SLICES_CANNOT_BE_MIXED = "Union and array-slices cannot " +
            "be mixed";
    static final String USE_ONLY_A_UNION_OR_SLICE_EXPRESSION = "Use only a union or slice " +
            "expression";
    static final String EXPECTED_UNION_OR_SLICE = "Expected a union or array-slice, " +
            "but did not find one.";
    static final String NO_UNION_OR_SLICE_RESOLUTION = "Use a comma to separate fields for a " +
            "union or a slice expression (i.e. array[start:stop:step])";
    static final String TOO_MANY_COMPONENTS_IN_ARRAY_SLICE = "Too many components in array slice";
    static final String TOO_MANY_COMPS_IN_SLICE = "Array-slices should only have three " +
            "components (start:stop:step)";
    static final String SINGLE_PATH_HAS_ORDERING = "Path includes an ordering specification, " +
            "but does not match multiple paths";
    static final String CHANGE_PATH_TO_RETURN_MULTIPLE_RESULTS = "Change the path to match " +
            "multiple paths by using a wildcard, union, descent, or filter.";
    static final String UNTERMINATED_ORDERING_CLAUSE = "Unterminated ordering clause";
    static final String UNTERMINATED_CURLY_RESOLUTION = "The ordering clause must be terminated " +
            "with a right-curly-brace character (})";
    static final String INVALID_CHARACTER_IN_ORDERING_CLAUSE = "Invalid character in ordering " +
            "clause at: %s";
    static final String INVALID_CHAR_IN_ORDER_CLAUSE_RESOLUTION = "The ordering clause has the " +
            "format: {(<|>)path1[,(<|>)path2, ...]}";
    static final String EXTRANEOUS_RIGHT_PARENTHESIS = "Extraneous right parenthesis: %s";
    static final String REMOVE_THE_EXTRA_PARENTHESIS = "Remove the extra parenthesis";
    static final String EXPECTING_INTEGER_FOR_JSON_PATH_ARRAY_INDEX_FOUND = "Expecting integer " +
            "for JSON-Path array index, found: %s";
    static final String CHANGE_THE_EXPRESSION_TO_RETURN_AN_INTEGER = "Change the expression to " +
            "return an integer";
    static final String UNABLE_TO_COMPILE_NUMBER = "Unable to compile number";
    static final String UNABLE_TO_COMPILE_STRING = "Unable to compile string";
    static final String EXPECTING_NUMBERS_OR_STRINGS_IN_RAW_PATH = "Expecting numbers or strings " +
            "in raw path";
    static final String UNABLE_TO_WRITE_DATA_TO_A_STATICALLY_DEFINED_PATH = "Unable to write data" +
            " to a statically defined path";

    private Messages() {
    }
}

