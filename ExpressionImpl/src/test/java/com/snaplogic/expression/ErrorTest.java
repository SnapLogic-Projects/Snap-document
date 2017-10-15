/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import org.junit.Test;

/**
 * Tests for error messaging.
 *
 * @author tstack
 */
public class ErrorTest extends ExpressionTest {
    @Test
    public void testUnterminatedString() {
        evalError("1 + \n'foo",
                "Expression parsing failed near -- 1 + \\n >> 'foo << ",
                "Unterminated string literal at line 2:0");
    }

    @Test
    public void testUnterminatedDoubleString() {
        evalError("1 + \n\"foo",
                "Expression parsing failed near -- 1 + \\n >> \"foo << ",
                "Unterminated string literal at line 2:0");
    }

    @Test
    public void testUnterminatedObjLiteral() {
        evalError("({ foo: 'bar' ).foo",
                "Expression parsing failed near -- foo: 'bar' >>  <<  ).foo",
                "Object literal starting at line 1:1 was not terminated");
    }

    @Test
    public void testUnterminatedCall() {
        evalError("parseInt('123'",
                "Expression parsing failed near -- ('123' >>  << ",
                "Function call starting at line 1:8 is missing a closing parentheses");
    }

    @Test
    public void testUnterminatedArray() {
        evalError("[1, 2, 3",
                "Expression parsing failed near -- [1,2,3 >>  << ",
                "Array literal starting at line 1:0 was not terminated");
    }

    @Test
    public void testUnterminatedParameterList() {
        evalError("(a, b, c => a + b + c",
                "Expression parsing failed near -- (a,b,c >>  << ",
                "Arrow function parameter list starting at line 1:0 is missing a closing right " +
                        "parentheses");
    }

    @Test
    public void testEquals() {
        evalError("1 = 2",
                "Expression parsing failed near -- 1  >> = <<  2",
                "Attempt to use assignment at line 1:2, which is not supported in the expression " +
                        "language");
    }

    @Test
    public void duplicateParameterName() {
        evalError("((x, x) => 1)(1, 2)",
                "Expression parsing failed near -- ((x,  >> x << ) => 1)(1",
                "Parameter name 'x' has already been used");
    }

    @Test
    public void stackOverflow() {
        evalError("({ boom: () => this.boom() }).boom()",
                "Too much recursion in method: boom",
                "The function called itself too many times");
    }

    @Test
    public void notAFunction() {
        evalError("({ boom: 1 }).boom()",
                "Property 'boom' is not a function",
                "Property value is a BIG_INTEGER and not a function");
    }

    @Test
    public void invalidPropertyRef() {
        evalError("({ empty: {}, name: 'badref', deref: () => this.empty[this.name] }).deref()",
                  "this.empty[this.name] is undefined",
                  "'badref' was not found while evaluating the sub-expression 'this.empty[this" +
                          ".name]'",
                  "You can check for the existence of a field with the 'in' operator (e.g. 'abc' " +
                          "in $obj)");
    }

    @Test
    public void unknownGlobalMethod() {
        evalError("Math.randomUID()",
                  "Math.randomUID is undefined",
                  "'randomUID' was not found while evaluating the sub-expression 'Math.randomUID'",
                  "You can check for the existence of a field with the 'in' operator (e.g. 'abc' " +
                          "in $obj) or perhaps you meant to reference one of the following " +
                          "methods: randomUUID, random");
    }

    @Test
    public void unknownGlobalVariable() {
        evalError("Math.a",
                  "Math.a is undefined",
                  "'a' was not found while evaluating the sub-expression 'Math.a'",
                  "You can check for the existence of a field with the 'in' operator (e.g. 'abc' " +
                          "in $obj) or perhaps you meant to reference one of the following " +
                          "properties: E, PI, abs, max, LN2");
    }

    @Test
    public void unknownProperty() {
        evalError("({foo: 1, bazzer: 2}).fo",
                  "({foo: 1, bazzer: 2}).fo is undefined",
                  "'fo' was not found while evaluating the sub-expression '({foo: 1, bazzer: 2})" +
                          ".fo'",
                  "You can check for the existence of a field with the 'in' operator (e.g. 'abc' " +
                          "in $obj) or perhaps you meant to reference one of the following " +
                          "properties: foo");
    }

    @Test
    public void unknownPropertyIndex() {
        evalError("({foo: 1, bazzer: 2})['fo']",
                  "({foo: 1, bazzer: 2})['fo'] is undefined",
                  "'fo' was not found while evaluating the sub-expression '({foo: 1, bazzer: 2})" +
                          "['fo']'",
                  "You can check for the existence of a field with the 'in' operator (e.g. 'abc' " +
                          "in $obj) or perhaps you meant to reference one of the following " +
                          "properties: foo");
    }

    @Test
    public void invokeNull() {
        evalError("null.plussDays(1)",
                  "Cannot invoke a method on a null value",
                  "Value referenced in the sub-expression 'null.plussDays(1)' is null",
                  "Please check expression syntax");
    }

    @Test
    public void nullPropertyRef() {
        evalError("null.foo",
                  "Cannot lookup a property on a null value",
                  "Value referenced in the sub-expression 'null.foo' is null",
                  "Please check expression syntax and data types.");
    }

    @Test
    public void unknownMemberMethod() {
        evalError("Date.parse(1234567).plussDays(1)",
                  "Date type does not have a method named: plussDays",
                  "'plussDays' was not found while evaluating the sub-expression 'Date.parse" +
                          "(1234567).plussDays(1)'",
                  "You can check for the existence of a field with the 'in' operator (e.g. 'abc' " +
                          "in $obj) or perhaps you meant to reference one of the following " +
                          "methods: plusDays, plusYears");
    }
}