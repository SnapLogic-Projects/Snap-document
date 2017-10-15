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

import com.snaplogic.api.Lint;
import com.snaplogic.api.SnapException;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.grammars.SnapExpressionsLexer;
import com.snaplogic.grammars.SnapExpressionsParser;
import com.snaplogic.snap.api.SnapDataException;
import com.snaplogic.util.DefaultValueHandler;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.Java;
import org.codehaus.janino.Parser;
import org.codehaus.janino.Scanner;
import org.junit.After;
import org.junit.Before;
import sl.EvaluatorUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Base class for writing expression tests.
 *
 * @author jinloes
 */
public class ExpressionTest {

    /**
     * Custom LintReporter class for checking multiline strings
     *
     * @author choward18
     */
    public class ExpressionLintReporter implements com.snaplogic.common.services.LintReporter {

        public final List<String> messages = new ArrayList<>();

        @Override
        public void report(Lint msg) {
            this.messages.add(msg.getName());
        }

    }

    protected ExpressionLintReporter LintReporter = new ExpressionLintReporter();

    @Before
    public void pushLint() {
        LintReporter.LOCAL_HOLDER.push(LintReporter);
    }

    @After
    public void popLint() {
        LintReporter.LOCAL_HOLDER.pop();
    }

    public <T> T eval(String inputStr) {
        return (T) eval(inputStr, null);
    }

    public <T> T eval(String inputStr, Object data) {
        return eval(inputStr, data, (Map<String, Object>) null);
    }

    public <T> T eval(String inputStr, Map<String, Object> envData) {
        return eval(inputStr, null, envData);
    }

    public <T> T eval(String inputStr, Object data, Map<String, Object> envData) {
        ScopeStack scopeStack = new ScopeStack();
        scopeStack.push(new GlobalScope());
        if (envData != null) {
            scopeStack.push(new EnvironmentScope(envData));
        }
        return eval(inputStr, data, scopeStack);
    }

    public <T> T eval(String inputStr, Object data, ScopeStack scopeStack) {
        Pair<ParseTree, JaninoStringGeneratorVisitor> parseTreeVisitorPair =
                parse(inputStr, data);
        JaninoStringGeneratorVisitor janinoStringGeneratorVisitor = parseTreeVisitorPair.getRight();
        SnapLogicExpression evaluator = janinoStringGeneratorVisitor.buildExpression(
                inputStr, parseTreeVisitorPair.getKey());
        try {
            Object retval = evaluator.evaluate(data, scopeStack, new DefaultValueHandler());

            if (retval instanceof Number) {
                boolean validNumber = false;

                if (retval instanceof BigDecimal || retval instanceof BigInteger) {
                    validNumber = true;
                }
                if (retval instanceof Double) {
                    double dval = (double) retval;
                    if (Double.isInfinite(dval) || Double.isNaN(dval)) {
                        validNumber = true;
                    }
                }
                if (!validNumber) {
                    fail("Expression language numbers should be BitIntegers or BigDecimals");
                }
            }
            return (T) retval;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable th) {
            throw new SnapDataException(th, "Unhandled exception");
        } finally {
            EvaluatorUtils.ExpressionContext expressionContext = EvaluatorUtils
                    .CONTEXT_THREAD_LOCAL.get();
            assertNull(expressionContext.scopes);
        }
    }

    public Pair<ParseTree, JaninoStringGeneratorVisitor> parse(String inputStr, Object data) {
        CharStream input = new ANTLRInputStream(inputStr);
        SnapExpressionsLexer lexer = new BailSnapExpressionsLexer(inputStr, input);
        TokenStream tokens = new CommonTokenStream(lexer);
        SnapExpressionsParser parser = new SnapExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy(inputStr));
        ParseTree tree = parser.eval();
        JaninoStringGeneratorVisitor visitor = new JaninoStringGeneratorVisitor(data, null,
                null);
        return Pair.of(tree, visitor);
    }

    public void evalError(String inputStr, String expectedFailure, String expectedReason,
            String expectedResolution) {
        try {
            eval(inputStr);
            fail();
        } catch (SnapException e) {
            String msg;
            if (expectedResolution != null) {
                msg = String.format("Error mismatch\n" +
                                "        evalError(\"%s\",\n" +
                                "                  \"%s\",\n" +
                                "                  \"%s\",\n" +
                                "                  \"%s\");" +
                                "\n",
                        StringEscapeUtils.escapeJava(inputStr),
                        StringEscapeUtils.escapeJava(e.getMessage()),
                        StringEscapeUtils.escapeJava(e.getReason()),
                        StringEscapeUtils.escapeJava(e.getResolution()));
            } else {
                msg = String.format("Error mismatch\n" +
                                "        evalError(\"%s\",\n" +
                                "                  \"%s\",\n" +
                                "                  \"%s\");" +
                                "\n",
                        StringEscapeUtils.escapeJava(inputStr),
                        StringEscapeUtils.escapeJava(e.getMessage()),
                        StringEscapeUtils.escapeJava(e.getReason()));
            }
            assertEquals(msg, expectedFailure, e.getMessage());
            assertEquals(msg, expectedReason, e.getReason());
            if (expectedResolution != null) {
                assertEquals(msg, expectedResolution, e.getResolution());
            }
        }
    }

    public void evalError(String inputStr, String expectedFailure, String expectedReason) {
        evalError(inputStr, expectedFailure, expectedReason, null);
    }

    // Shows the atom generated by janino for some sample java expression
    // Keep this one for future reference.
    public void astGeneration() throws IOException, CompileException {
        String expression = "new byte[] { (byte)0xe0, 0x4f, (byte)0xd0,0x20, " +
                "(byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, " +
                "0x2b,0x30, 0x30, (byte)0x9d }";
        Java.Atom atom = (new Parser(new Scanner(null, new ByteArrayInputStream(expression
                .getBytes())))).parseExpression();
        System.out.println(atom);
    }
}