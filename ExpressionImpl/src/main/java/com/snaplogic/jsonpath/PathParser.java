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

import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.expression.util.LiteralUtils;
import com.snaplogic.jsonpath.tokens.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.snaplogic.jsonpath.Messages.*;
import static com.snaplogic.jsonpath.tokens.NameToken.ROOT_ELEMENT;

/**
 * Parser for JSON-Paths.
 *
 * @author tstack
 */
public class PathParser {

    private static final Logger LOG = LoggerFactory.getLogger(PathParser.class);
    private static final String ESCAPED_FIELD_NAME = "'%s'";

    /**
     * Interface used by the parser to detect the end of a JSON-Path in an input string.
     */
    public interface TerminatorDetector {
        /**
         * Check if the given character is the terminator.
         *
         * @param ch The character to check.
         * @return True if the parser should stop consuming characters from the input string.
         */
        boolean checkForTerminator(char ch);
    }

    private static final Pattern JSON_PATH_FUNC = Pattern.compile(
            "^\\s*jsonPath\\(\\s*\\$\\s*,\\s*(.*)\\)\\s*$");
    private static final String EVAL = "eval";
    private static final String MAP = "map";
    private static final String SORT_ASC = "sort_asc";
    private static final String SORT_DESC = "sort_desc";
    private static final String GROUP_BY = "group_by";

    private static final Pattern PLAIN_CHARS = Pattern.compile("^\\w+$");

    /**
     * Parse a JSON-Path string into a list of tokens.
     *
     * @param path The path to parse.
     * @return A list of tokens that make up the path.
     * @throws InvalidPathException If there was an issue parsing the path.
     */
    public static List<PathToken> parse(String path, PathExpressionService expressionHandler) throws
            InvalidPathException {
        return new PathParser(path, expressionHandler, null).parse().tokens;
    }

    /**
     * Parse a string up to a given terminator.
     *
     * @param path The string to parse.
     * @param terminator The callback that detects the embedded end of the input string.
     * @return The length of the parsed JSON-path.
     * @throws InvalidPathException If there was a problem parsing the JSON-path.
     */
    public static int parseUpTo(String path, PathExpressionService expressionHandler,
            TerminatorDetector terminator) throws InvalidPathException {
        return new PathParser(path, expressionHandler, terminator).parse().end;
    }

    /**
     * Convert a sublist of PathTokens into a JSON-path string.
     *
     * @param tokens The tokens in the path.
     * @param maxIndex The index of the last token to include in the JSON-path string or -1
     *                 if all tokens should be included.
     * @return The JSON-path string.
     */
    public static String unparse(List<PathToken> tokens, int maxIndex) {
        StringBuilder retval = new StringBuilder();
        int index = 0;

        for (PathToken token : tokens) {
            if (maxIndex != -1 && index > maxIndex) {
                break;
            }

            if (index > 0 &&
                    !(token instanceof SubscriptToken) &&
                    !(token instanceof DescentToken) &&
                    retval.charAt(retval.length() - 1) != '.') {
                retval.append('.');
            }

            retval.append(token.toString());

            index += 1;
        }

        return retval.toString();
    }

    /**
     * Convert a sublist of PathTokens into a JSON-path string.
     *
     * @param tokens The tokens in the path.
     * @return The JSON-path string.
     */
    public static String unparse(List<PathToken> tokens) {
        return unparse(tokens, -1);
    }

    /**
     * Convert a "raw" path into a series of json-path PathTokens.  The "raw" path should
     * only consist of the Strings and Numbers that represent the field names and array
     * indexes to be traversed.
     *
     * @param path The contents of the raw path.
     * @param pathExpressionService The expression service to use when compiling expressions.
     * @return A list of PathTokens that represent the given raw path.
     */
    public static List<PathToken> tokenize(List<Object> path,
            PathExpressionService pathExpressionService) {
        List<PathToken> retval = new ArrayList<>();

        retval.add(NameToken.ROOT);
        for (int i = 0; i < path.size(); i++) {
            Object indexOrField = path.get(i);

            if (i == 0 && ROOT_ELEMENT.equals(indexOrField)) {
                continue;
            }
            if (indexOrField == null) {
                // Be nice and skip null values.
            } else if (indexOrField instanceof Number) {
                String index = indexOrField.toString();
                try {
                    // TODO tstack - we should add a ConstantExpressionToken rather than
                    // compiling constant expressions.
                    retval.add(new SubscriptToken(new ExpressionToken(index,
                            pathExpressionService.compile(index, 0, index))));
                } catch (InvalidPathException e) {
                    throw new ExecutionException(e, UNABLE_TO_COMPILE_NUMBER)
                            .withResolutionAsDefect();
                }
            } else if (indexOrField instanceof String) {
                String field = indexOrField.toString();
                if (PLAIN_CHARS.matcher(field).matches()) {
                    // The path can be written plainly, without square brackets and a quoted
                    // string.
                    retval.add(new NameToken(field));
                } else {
                    String escapedField = StringEscapeUtils.escapeEcmaScript(field);
                    String fieldExpr = String.format(ESCAPED_FIELD_NAME, escapedField);
                    try {
                        retval.add(new SubscriptToken(new ExpressionToken(fieldExpr,
                                pathExpressionService.compile(fieldExpr, 0, fieldExpr))));
                    } catch (InvalidPathSyntaxException e) {
                        throw new ExecutionException(e, UNABLE_TO_COMPILE_STRING)
                                .withResolutionAsDefect();
                    }
                }
            } else {
                throw new IllegalArgumentException(EXPECTING_NUMBERS_OR_STRINGS_IN_RAW_PATH);
            }
        }

        return retval;
    }

    /**
     * States for the parser.
     */
    private enum ParserState {
        STATE_INIT,
        STATE_TOP,
        STATE_NAME,
        STATE_SUBSCRIPT,
        STATE_STRING,
        STATE_ROOT_EXPR,
        STATE_FIRST_EXPR,
        STATE_EXPR,
    }

    private final String path;
    private final PathExpressionService expressionHandler;
    private final TerminatorDetector terminator;
    private final List<PathToken> tokens = new ArrayList<>();
    private int end = -1;

    private PathParser(String path, final PathExpressionService expressionHandler,
            TerminatorDetector terminator) {
        Matcher matcher = JSON_PATH_FUNC.matcher(path);
        if (matcher.matches()) {
            try {
                path = LiteralUtils.unescapeString(matcher.group(1));
            } catch (ExecutionException e) {
                // ignore
            }
        }
        this.path = path;
        this.expressionHandler = expressionHandler;
        this.terminator = terminator;
    }

    /**
     * Safely read a char from the path string without reading past the end.
     * @param offset The offset in the string to read.
     * @return The char from the string or NUL.
     */
    private char lookahead(final int offset) {
        if (offset < path.length()) {
            return path.charAt(offset);
        }
        return (char) 0;
    }

    /**
     * Read up to the end of a string.
     *
     * @param start The index of the opening quote in the string.
     * @return The index past the closing quote of the string.
     * @throws InvalidPathException If the string is not terminated.
     */
    private int consumeString(int start) throws InvalidPathException {
        for (int i = start; i < path.length(); i++) {
            char ch = path.charAt(i);

            switch (ch) {
                case '\\':
                    i += 1;
                    break;
                case '\'':
                case '"':
                    if (i > start && path.charAt(start) == ch) {
                        return i + 1;
                    }
                    break;
            }
        }

        throw new InvalidPathSyntaxException(start,
                String.format(UNTERMINATED_STRING_CONSTANT, path.substring(start)))
                .withPath(path)
                .withResolution(String.format(UNTERMINATED_RESOLUTION, path.charAt(start)));
    }

    /**
     * Read up to the end of a subscript path component.
     *
     * @param start The index of the opening bracket of the subscript.
     * @return The index of the closing bracket.
     * @throws InvalidPathException If there was an issue parsing the inside of the subscript.
     */
    private int consumeSubscript(int start) throws InvalidPathException {
        int tokensTail = tokens.size();
        int next, sizeDiff;

        next = parse(start + 1, ParserState.STATE_SUBSCRIPT);
        sizeDiff = tokens.size() - tokensTail;
        switch (sizeDiff) {
            case 0:
                throw new InvalidPathSyntaxException(start, EMPTY_SUBSCRIPT_COMPONENT_IN_PATH)
                        .withPath(path)
                        .withResolution(EMPTY_SUBSCRIPT_RESOLUTION);
            case 1:
                if (!(tokens.get(tokensTail) instanceof SeparatorToken)) {
                    tokens.add(new SubscriptToken(tokens.remove(tokens.size() - 1)));
                    break;
                }

            default: {
                List<ExpressionToken> children = new ArrayList<>();
                List<SeparatorToken> separatorList = new ArrayList<>();

                while (tokens.size() > tokensTail) {
                    PathToken token = tokens.remove(tokensTail);

                    if (token instanceof SeparatorToken) {
                        if (!separatorList.isEmpty() &&
                                !separatorList.get(0).equals(token)) {
                            // TODO tstack - It might be nice to support this type of syntax:
                            //    $.foo[0,15:20]
                            throw new InvalidPathSyntaxException(start,
                                    UNION_AND_ARRAY_SLICES_CANNOT_BE_MIXED)
                                    .withPath(path)
                                    .withResolution(USE_ONLY_A_UNION_OR_SLICE_EXPRESSION);
                        }

                        separatorList.add((SeparatorToken) token);

                        if (separatorList.size() > children.size()) {
                            children.add(null);
                        }
                        continue;
                    }

                    if (!(token instanceof ExpressionToken)) {
                        throw new InvalidPathSyntaxException(start,
                                String.format(EXPECTING_EXPRESSION_IN_SUBSCRIPT,
                                        path.substring(start)))
                                .withPath(path)
                                .withResolution(EMPTY_SUBSCRIPT_RESOLUTION);
                    }

                    children.add((ExpressionToken) token);
                }

                if (separatorList.isEmpty()) {
                    // I don't think the parser would allow this to happen...
                    throw new InvalidPathSyntaxException(start, EXPECTED_UNION_OR_SLICE)
                            .withPath(path)
                            .withResolution(NO_UNION_OR_SLICE_RESOLUTION);
                }

                switch (separatorList.get(0).getSeparator()) {
                    case ':':
                        while (children.size() < 3) {
                            children.add(null);
                        }

                        if (children.size() > 3) {
                            throw new InvalidPathSyntaxException(start,
                                    TOO_MANY_COMPONENTS_IN_ARRAY_SLICE)
                                    .withPath(path)
                                    .withResolution(TOO_MANY_COMPS_IN_SLICE);
                        }

                        tokens.add(new SubscriptToken(new SliceToken(
                                children.get(0),
                                children.get(1),
                                children.get(2))));
                        break;
                    case ',':
                        tokens.add(new SubscriptToken(new UnionToken(children)));
                        break;
                }
                break;
            }
        }
        if (lookahead(next) != ']') {
            throw new InvalidPathSyntaxException(start,
                    String.format(UNTERMINATED_SUBSCRIPT_COMPONENT_IN_PATH,
                            path.substring(start)))
                    .withPath(path)
                    .withResolution(UNTERMINATED_SUBSCRIPT_RESOLUTION);
        }

        return next;
    }

    /**
     * Attempt to consume a method call.
     *
     * @param i The index of opening paren.
     * @param methodName The name of the method.
     * @return The next index to consume.
     * @throws InvalidPathException If there was a problem parsing the path.
     */
    private int consumeMethod(int i, String methodName) throws InvalidPathException {
        int next = parse(i + 1, ParserState.STATE_EXPR);
        String expr = path.substring(i + 1, next);
        CompiledExpressionHolder code;
        switch (methodName) {
            case EVAL:
                code = expressionHandler.compile(path, i, expr);
                tokens.add(new EvalToken(expr, code));
                break;
            case MAP:
                code = expressionHandler.compile(path, i, expr);
                tokens.add(new MapToken(expr, code));
                break;
            case SORT_ASC:
                code = expressionHandler.compile(path, i, expr);
                tokens.add(new SortToken(SortToken.Ordering.ASCENDING, expr, code));
                break;
            case SORT_DESC:
                code = expressionHandler.compile(path, i, expr);
                tokens.add(new SortToken(SortToken.Ordering.DESCENDING, expr, code));
                break;
            case GROUP_BY:
                code = expressionHandler.compile(path, i, expr);
                tokens.add(new GroupByToken(expr, code));
                break;
            default:
                // Treat unrecognized method names as regular NameTokens.
                return 1;
        }
        return next - i + 1;
    }

    private int parse(int start, ParserState state)
            throws InvalidPathException {
        int i = start, nameStart = 0;
        ParserState nextState;

        charloop:
        while (i < path.length()) {
            char ch = path.charAt(i);
            int inc = 1;

            nextState = null;
            switch (state) {
                case STATE_INIT:
                    nextState = ParserState.STATE_TOP;
                    switch (ch) {
                        case '$':
                            break;
                        case ' ':
                        case '\t':
                            nextState = ParserState.STATE_INIT;
                            break;
                        default:
                            inc = 0;
                            break;
                    }
                    if (nextState == ParserState.STATE_TOP) {
                        tokens.add(new NameToken(ROOT_ELEMENT));
                    }
                    break;
                case STATE_TOP:
                    if (terminator != null && terminator.checkForTerminator(ch)) {
                        end = i;
                        break charloop;
                    }
                    switch (ch) {
                        case '*':
                            tokens.add(new WildcardToken());
                            break;
                        case '.':
                            if (lookahead(i + 1) == '.') {
                                tokens.add(new DescentToken());
                                nextState = ParserState.STATE_TOP;
                                inc = 2;
                            } else {
                                nextState = ParserState.STATE_NAME;
                            }
                            break;
                        case '[':
                            i = consumeSubscript(i);
                            break;
                        case ']':
                            throw new InvalidPathSyntaxException(i,
                                    String.format(UNEXPECTED_TOKEN_IN_PATH,
                                            path.substring(start)))
                                    .withPath(path)
                                    .withResolution(UNEXPECTED_RIGHT_BRACKET_RESOLUTION);
                        case '(':
                        case ')':
                            throw new InvalidPathSyntaxException(i,
                                    String.format(UNEXPECTED_TOKEN_IN_PATH,
                                            path.substring(start)))
                                    .withPath(path)
                                    .withResolution(UNEXPECTED_EXPR_RESOLUTION);
                        case '\'':
                        case '\"':
                            throw new InvalidPathSyntaxException(i,
                                    String.format(UNEXPECTED_QUOTE, path.substring(start)))
                                    .withPath(path)
                                    .withResolution(QUOTES_IN_BRACKETS);
                        default:
                            nextState = ParserState.STATE_NAME;
                            inc = 0;
                            break;
                    }
                    break;
                case STATE_NAME:
                    if (terminator != null && terminator.checkForTerminator(ch)) {
                        end = i;
                        break charloop;
                    }
                    switch (ch) {
                        case '*':
                            if (i == nameStart) {
                                tokens.add(new WildcardToken());
                                nextState = ParserState.STATE_TOP;
                            }
                            break;
                        case '.':
                        case '[': {
                            String nameString = path.substring(nameStart, i).trim();

                            if (!nameString.isEmpty()) {
                                tokens.add(new NameToken(nameString));
                            }
                            nextState = ParserState.STATE_TOP;
                            inc = 0;
                            break;
                        }
                        case '(':
                            String nameString = path.substring(nameStart, i).trim();
                            inc = consumeMethod(i, nameString);
                            if (inc > 1) {
                                nextState = ParserState.STATE_TOP;
                            }
                            break;
                        case ' ':
                            if (i == nameStart) {
                                // Ignore any leading spaces in a name.
                                nameStart = i + 1;
                            }
                            break;
                    }
                    break;
                case STATE_SUBSCRIPT:
                    switch (ch) {
                        case '*':
                            tokens.add(new WildcardToken());
                            break;
                        case '?': {
                            ExpressionToken exprToken;

                            if (lookahead(i + 1) != '(') {
                                throw new InvalidPathSyntaxException(i,
                                        String.format(EXPECTING_EXPRESSION_WITH_PAREN,
                                                path.substring(start)))
                                        .withPath(path)
                                        .withResolution(FILTER_WITHOUT_EXPR_RESOLUTION);
                            }

                            i = parse(i + 1, ParserState.STATE_ROOT_EXPR);
                            exprToken = (ExpressionToken) tokens.remove(tokens.size() - 1);
                            tokens.add(new FilterToken(exprToken));
                            inc = 0;
                            break;
                        }
                        case ',':
                        case ':':
                            tokens.add(new SeparatorToken(ch));
                            break;
                        case ' ':
                            break;
                        case ']':
                            return i;
                        case ')':
                            throw new InvalidPathSyntaxException(i,
                                    String.format(EXTRANEOUS_RIGHT_PARENTHESIS,
                                            path.substring(start)))
                                    .withPath(path)
                                    .withResolution(REMOVE_THE_EXTRA_PARENTHESIS);
                        default:
                            i = parse(i, ParserState.STATE_ROOT_EXPR);
                            inc = 0;
                            break;
                    }
                    break;
                case STATE_ROOT_EXPR: {
                    int next = parse(i, ParserState.STATE_FIRST_EXPR);
                    String exprString = path.substring(start, next);

                    tokens.add(new ExpressionToken(exprString,
                            expressionHandler.compile(path, start, exprString)));
                    return next;
                }
                case STATE_FIRST_EXPR:
                case STATE_EXPR:
                    switch (ch) {
                        case '\'':
                        case '\"':
                            i = parse(i, ParserState.STATE_STRING);
                            inc = 0;
                            break;
                        case '[': {
                            int next = parse(i + 1, ParserState.STATE_EXPR);

                            if (lookahead(next) != ']') {
                                throw new InvalidPathSyntaxException(i, String.format(
                                        UNTERMINATED_SUBSCRIPT_COMPONENT_IN_EXPRESSION,
                                        path.substring(start)))
                                        .withPath(path)
                                        .withResolution(UNTERMINATED_SUBSCRIPT_RESOLUTION);
                            }
                            i = next + 1;
                            inc = 0;
                            break;
                        }
                        case ',':
                        case ':':
                            if (state == ParserState.STATE_FIRST_EXPR) {
                                return i;
                            }
                            break;
                        case ']':
                        case ')':
                            return i;
                        case '(': {
                            int next = parse(i + 1, ParserState.STATE_EXPR);

                            if (lookahead(next) != ')') {
                                throw new InvalidPathSyntaxException(i, String.format(
                                        UNTERMINATED_PARENTHESIS_IN_EXPRESSION,
                                        path.substring(start)))
                                        .withPath(path)
                                        .withResolution(UNTERMINATED_EXPR_RESOLUTION);
                            }
                            i = next + 1;
                            inc = 0;
                            break;
                        }
                    }
                    break;
                case STATE_STRING:
                    return consumeString(i);
            }

            if (nextState != null) {
                switch (nextState) {
                    case STATE_NAME:
                        nameStart = i + inc;
                        break;
                }

                state = nextState;
            }

            i += inc;
        }

        switch (state) {
            case STATE_NAME:
                tokens.add(new NameToken(path.substring(nameStart, i).trim()));
                break;
            case STATE_FIRST_EXPR:
            case STATE_EXPR:
                throw new InvalidPathSyntaxException(i, String.format(
                        UNTERMINATED_PARENTHESIS_IN_EXPRESSION,
                        path.substring(start)))
                        .withPath(path)
                        .withResolution(UNTERMINATED_EXPR_RESOLUTION);
        }

        return i;
    }

    private PathParser parse() throws InvalidPathException {
        end = parse(0, ParserState.STATE_INIT);

        if (tokens.isEmpty()) {
            tokens.add(NameToken.ROOT);
        }

        if (tokens.get(tokens.size() - 1) instanceof DescentToken) {
            throw new InvalidPathSyntaxException(path.length() - 2,
                    A_JSON_PATH_CANNOT_END_WITH_A_DESCENT_TOKEN)
                    .withPath(path)
                    .withResolution(APPEND_A_FIELD_NAME_OR_ARRAY_INDEX);
        }

        return this;
    }
}
