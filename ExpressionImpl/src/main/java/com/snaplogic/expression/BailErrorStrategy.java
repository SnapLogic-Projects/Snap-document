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

import com.snaplogic.api.ExecutionException;
import com.snaplogic.grammars.SnapExpressionsParser;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.Utils;
import org.apache.commons.lang.StringUtils;

import static com.snaplogic.expression.Messages.*;

/**
 * Parser strategy that bails on first error.
 *
 * @author jinloes
 */
public class BailErrorStrategy extends DefaultErrorStrategy {
    private String expression;
    private static final String ERROR_MESSAGE = "Mismatched input %s at line %d:%d. " +
            "Expecting one of: %s";

    public static String extractSubExpression(String expression, int startIndex, int stopIndex) {
        String tokenPrefix = expression.substring(Math.max(0, startIndex - 10), startIndex);
        String token = startIndex <= stopIndex ? expression.substring(startIndex, stopIndex + 1) :
                StringUtils.EMPTY;
        String tokenSuffix = expression.substring(Math.min(stopIndex + 1, expression.length()),
                Math.min(stopIndex + 10, expression.length()));

        return Utils.escapeWhitespace(String.format(
                "Expression parsing failed near -- %s >> %s << %s", tokenPrefix, token,
                tokenSuffix), false);
    }

    public static String unterminatedString(String expr, int startIndex, int line, int pos) {
        throw new ExecutionException(BailErrorStrategy.extractSubExpression(
                expr, startIndex, expr.length() - 1))
                .withReason(String.format("Unterminated string literal at line %d:%d", line, pos))
                .withResolution("Insert the close quote character at the end of the string " +
                        "literal");
    }

    public static String unterminatedComment(String expr, int startIndex, int line, int pos) {
        throw new ExecutionException(BailErrorStrategy.extractSubExpression(
                expr, startIndex, expr.length() - 1))
                .withReason(String.format("Unterminated comment at line %d:%d", line, pos))
                .withResolution("Close the comment with '*/'");
    }

    public static String expressionSnippet(String expr) {
        String retval = Utils.escapeWhitespace(expr.substring(0, Math.min(30, expr.length())),
                false);

        if (retval.length() < expr.length()) {
            retval += " ...";
        }
        return retval;
    }

    public BailErrorStrategy(final String expression) {
        super();
        this.expression = expression;
    }

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        // If the error message ends up not being good enough, we might be able to inspect the rule
        // context in the exception.
        Token token = e.getOffendingToken();
        if (token == null) {
            token = recognizer.getCurrentToken();
        }

        String reason, resolution = PLEASE_CHECK_EXPRESSION_SYNTAX;

        if (token.getType() == SnapExpressionsParser.ASSIGN) {
            reason = String.format("Attempt to use assignment at line %d:%d, which is not " +
                    "supported in the expression language", token.getLine(), token
                    .getCharPositionInLine());
            resolution = "If you meant to compare two values, use two equals signs (==)";
        } else {
            reason = String.format(ERROR_MESSAGE, getTokenErrorDisplay(
                    recognizer.getCurrentToken()), token.getLine(), token
                            .getCharPositionInLine(),
                    recognizer.getExpectedTokens().toString(recognizer.getTokenNames()));
        }

        throw new ExecutionException(e, extractSubExpression(expression, token.getStartIndex(),
                token.getStopIndex()))
                .withReason(reason)
                .withResolution(resolution);
    }

    @Override
    public void reportUnwantedToken(final Parser recognizer) {
        if (inErrorRecoveryMode(recognizer)) {
            return;
        }

        Token t = recognizer.getCurrentToken();
        String tokenName = getTokenErrorDisplay(t);
        IntervalSet expecting = getExpectedTokens(recognizer);
        throw new ExecutionException(COULD_NOT_COMPILE_EXPRESSION)
                .formatWith(expressionSnippet(expression))
                .withReason(String.format(ENCOUNTERED_EXTRANEOUS_INPUT,
                        tokenName, t.getLine(), t.getCharPositionInLine(), expecting
                                .toString(recognizer.getTokenNames())))
                .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
    }

    @Override
    public Token recoverInline(Parser recognizer) throws RecognitionException {
        throw new ExecutionException(COULD_NOT_COMPILE_EXPRESSION)
                .formatWith(expressionSnippet(expression))
                .withReason(String.format(ERROR_MESSAGE, getTokenErrorDisplay(
                        recognizer.getCurrentToken()),
                        recognizer.getExpectedTokens().toString(recognizer.getTokenNames())))
                .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
    }
}
