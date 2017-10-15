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

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.expression.JsonPathExpressionService;
import com.snaplogic.jsonpath.tokens.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the parsing of JSON-Paths.
 *
 * @author tstack
 */
public class PathParserTest {
    private static final List<Pair<String, List<PathToken>>> PATHS_TO_TEST = Arrays.asList(
            // Test the basics
            Pair.of("$", asList(new NameToken("$"))),
            Pair.of("$.foo", asList(new NameToken("$"), new NameToken("foo"))),
            // Allow whitespace
            Pair.of("$. foo . bar", asList(new NameToken("$"), new NameToken("foo"),
                    new NameToken("bar"))),
            // Be nice and allow the first dot to be left off.
            Pair.of("$foo", asList(new NameToken("$"), new NameToken("foo"))),

            // Be nice and prepend the '$' symbol.
            Pair.of("foo", asList(new NameToken("$"), new NameToken("foo"))),

            // Wildcard can be used at the top level or in a subscript
            Pair.of("$.foo.*", asList(new NameToken("$"), new NameToken("foo"),
                    new WildcardToken())),
            Pair.of("$.foo. *", asList(new NameToken("$"), new NameToken("foo"),
                    new WildcardToken())),
            Pair.of("$.foo[*]", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new WildcardToken()))),
            // Allow whitespace around wildcard.
            Pair.of("$.foo[ * ]", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new WildcardToken()))),

            // Check strings in a subscript.
            Pair.of("$.foo['bar\\n']", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new ExpressionToken("'bar\\n'", null)))),
            Pair.of("$.foo[\"bar\"]", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new ExpressionToken("\"bar\"", null)))),

            Pair.of("$.foo.['bar']", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new ExpressionToken("'bar'", null)))),
            Pair.of("$.['foo'].['bar']", asList(new NameToken("$"),
                    new SubscriptToken(new ExpressionToken("'foo'", null)),
                    new SubscriptToken(new ExpressionToken("'bar'", null)))),

            // Make sure the parser is not tripped up by right brackets in the string.
            Pair.of("$.foo['bar[]']", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new ExpressionToken("'bar[]'", null)))),
            Pair.of("$.foo[ 'bar[]']", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new ExpressionToken("'bar[]'", null)))),

            // Check expressions in a subscript.
            Pair.of("$.foo.bar[(@.length - 1)]", asList(new NameToken("$"), new NameToken("foo"),
                    new NameToken("bar"),
                    new SubscriptToken(new ExpressionToken("(@.length - 1)", null)))),
            Pair.of("$.foo.bar[?(@['a'].val > parseInt(20))]", asList(new NameToken("$"),
                    new NameToken("foo"),
                    new NameToken("bar"),
                    new SubscriptToken(new FilterToken(
                            new ExpressionToken("(@['a'].val > parseInt(20))", null))))),
            Pair.of("$.foo.bar[(_name ? 't' : 'f')]", asList(new NameToken("$"),
                    new NameToken("foo"),
                    new NameToken("bar"),
                    new SubscriptToken(new ExpressionToken("(_name ? 't' : 'f')", null)))),

            // Check the descendant path.
            Pair.of("$.foo..bar[?(@.val > 20)]", asList(new NameToken("$"), new NameToken("foo"),
                    new DescentToken(), new NameToken("bar"),
                    new SubscriptToken(new FilterToken(new ExpressionToken("(@.val > 20)",
                            null))))),

            // Check union operator.
            Pair.of("$.foo[1,2,3]", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new UnionToken(Arrays.asList(
                            new ExpressionToken("1", null), new ExpressionToken("2", null),
                            new ExpressionToken("3", null)
                    ))))),

            Pair.of("$.foo[1:2:3]", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new SliceToken(
                            new ExpressionToken("1", null),
                            new ExpressionToken("2", null),
                            new ExpressionToken("3", null)
                    )))),
            Pair.of("$.foo[:]", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new SliceToken(
                            null,
                            null,
                            null
                    )))),
            Pair.of("$.foo[:2]", asList(new NameToken("$"), new NameToken("foo"),
                    new SubscriptToken(new SliceToken(
                            null,
                            new ExpressionToken("2", null),
                            null
                    )))),
            Pair.of("$.eval(value)", asList(
                    new NameToken("$"),
                    new EvalToken("value", null))),
            Pair.of("$.foo(value)", asList(
                    new NameToken("$"),
                    new NameToken("foo(value)")))
    );

    private static final String[] INVALID_PATHS_TO_TEST = {
            // Close bracket without an open
            "$'foo']",
            // Close paren without an open
            "$)",
            // Unterminated subscript
            "$['foo'",
            // Invalid chars at the top level
            "$('foo')",
            // Empty subscript
            "$[]",
            // Unterminated expression
            "$[($.bar[",
            // Unterminated string
            "$['foo",
            // Unterminated string
            "$[\"foo",
            // Descent without a final token.
            "$..",
            // Mixed union and slice
            "$[0,1:2:3]",
            // Too many components in slice.
            "$[1:2:3:4]",
            // Extra right paren
            "$[?(@ > 5))]",
            // Unterminated paren
            "$.eval(value",
    };

    // These paths will be run through parse/unparse and should be the same.
    private static final String[] ROUNDTRIP_PATHS_TO_TEST = {
            "$",
            "$.foo.bar",
            "$['baz']",
            "$['middle \\' quote']",
            "$.foo[(_index + 1)]",
            "$.foo[0]",
            "$.foo[0,1,2]",
            "$.foo[?(a == b)]",
            "$.foo..bar",
            "$.foo[-1]",
            "$.foo[*]",
            "$..*",
            "$.eval(value).foo",
    };

    // These paths will be normalized by the parser.
    private static final List<Pair<String, String>> UNPARSE_PATHS_TO_TEST = Arrays.asList(
            Pair.of("foo", "$.foo"),
            Pair.of("$ . foo", "$.foo"),
            Pair.of("$.foo[ * ]", "$.foo[*]")
    );

    private static final PathExpressionService EMPTY_EXPR_HANDLER = new JsonPathExpressionService();

    private static List<PathToken> asList(PathToken... args) {
        return Arrays.asList(args);
    }

    @Test
    public void testGoodPaths() throws Exception {
        for (Pair<String, List<PathToken>> pair : PATHS_TO_TEST) {
            try {
                List<PathToken> parsedTokens = PathParser.parse(pair.getLeft(), EMPTY_EXPR_HANDLER);

                assertEquals(pair.getLeft(), pair.getRight(), parsedTokens);
            } catch (InvalidPathException e) {
                throw new AssertionError(pair.getLeft(), e);
            }
        }
    }

    @Test
    public void testBadPaths() throws Exception {
        for (String path : INVALID_PATHS_TO_TEST) {
            try {
                List<PathToken> tokens = PathParser.parse(path, EMPTY_EXPR_HANDLER);
                fail("Expected InvalidPathSyntaxException to be thrown for -- " + path +
                        "; received: " + tokens);
            } catch (InvalidPathSyntaxException e) {
                assertNotEquals(String.format("Missing path for %s -- %s", path, e.getMessage()),
                        "", e.getPath());
                assertNotEquals(null, e.getResolution());
            } catch (Throwable e) {
                throw new AssertionError(path, e);
            }
        }
    }

    @Test
    public void testRoundTripPaths() throws Exception {
        for (String path : ROUNDTRIP_PATHS_TO_TEST) {
            try {
                assertEquals(String.format(
                        "Original path does not match unparse()'d path: %s", path), path,
                        PathParser.unparse(PathParser.parse(path, EMPTY_EXPR_HANDLER)));
            } catch (Throwable e) {
                throw new AssertionError(path, e);
            }
        }
    }

    @Test
    public void testUnparsePaths() throws Exception {
        for (Pair<String, String> pair : UNPARSE_PATHS_TO_TEST) {
            assertEquals(pair.getRight(), PathParser.unparse(PathParser.parse(pair.getLeft(),
                    EMPTY_EXPR_HANDLER)));
        }
    }

    @Test
    public void testPartial() throws Exception {
        assertEquals("$.foo", PathParser.unparse(PathParser.parse("$.foo.bar",
                EMPTY_EXPR_HANDLER), 1));
    }
}
