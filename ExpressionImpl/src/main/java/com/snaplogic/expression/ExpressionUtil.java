/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014 SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.grammars.SnapExpressionsLexer;
import com.snaplogic.grammars.SnapExpressionsParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Utilities methods for interacting with the expression language.
 *
 * @author tstack
 */
public final class ExpressionUtil {
    private static final SnapLogicExpression BLANK_EXPR = (doc, scopes, handler) -> null;

    public static SnapLogicExpression compile(String inputStr) {
        CharStream input = new ANTLRInputStream(inputStr);
        SnapExpressionsLexer lexer = new BailSnapExpressionsLexer(inputStr, input);
        TokenStream tokens = new CommonTokenStream(lexer);
        SnapExpressionsParser parser = new SnapExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy(inputStr));
        final ParseTree tree = parser.eval();
        JaninoStringGeneratorVisitor visitor = new JaninoStringGeneratorVisitor();

        return visitor.buildExpression(inputStr, tree);
    }

    private ExpressionUtil() {

    }
}
