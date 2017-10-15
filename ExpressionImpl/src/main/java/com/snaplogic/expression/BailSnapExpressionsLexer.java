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
import com.snaplogic.grammars.SnapExpressionsLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.Utils;
import org.apache.commons.lang3.StringUtils;

import static com.snaplogic.expression.Messages.*;

/**
 * Lexer that does not recover on invalid expressions.
 *
 * @author jinloes
 */
public class BailSnapExpressionsLexer extends SnapExpressionsLexer {
    private String expression;

    public BailSnapExpressionsLexer(final String expression, final CharStream input) {
        super(input);
        this.expression = expression;
    }

    @Override
    public void recover(LexerNoViableAltException e) {
        String offendingToken = StringUtils.EMPTY;
        int startIndex = e.getStartIndex();
        if (startIndex >= 0 && startIndex < getInputStream().size()) {
            offendingToken = e.getInputStream().getText(Interval.of(startIndex, startIndex));
            offendingToken = Utils.escapeWhitespace(offendingToken, false);
        }
        String expressionSnippet = BailErrorStrategy.expressionSnippet(expression);

        throw new ExecutionException(e, COULD_NOT_COMPILE_EXPRESSION)
                .formatWith(expressionSnippet)
                .withReason(String.format(INVALID_TOKEN, offendingToken, expressionSnippet))
                .withResolution(PLEASE_CHECK_EXPRESSION_SYNTAX);
    }
}
