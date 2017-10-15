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

import com.google.common.collect.ImmutableMap;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.snap.api.SnapDataException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;

;

/**
 * Tests for arrow functions
 *
 * @author tstack
 */
public class ArrowFunctionTest extends ExpressionTest {

    @Test
    public void constant() {
        assertEquals(BigInteger.valueOf(1), eval("(() => 1)()"));
    }

    @Test
    public void passthru() {
        assertEquals("Hello, World", eval("(x => 'Hello, ' + x)('World')"));
    }

    @Test
    public void square() {
        assertEquals(BigDecimal.valueOf(4), eval("(x => x * x)(2)"));
    }

    @Test
    public void nested() {
        assertEquals(BigDecimal.valueOf(3), eval("(x => (y => x + y)(1))(2)"));
    }

    @Test
    public void nullDefault() {
        assertNull(eval("(x => x)()"));
    }

    @Test
    public void indexCall() {
        assertEquals(BigDecimal.valueOf(4), eval(
                "({ foo: x => x * x, bar: () => this.foo(2) }).bar()"));
    }

    @Test
    public void nullDefaults() {
        assertEquals(Arrays.asList(null, null, null), eval("((x, y, z) => [x, y, z])()"));
    }

    @Test
    public void defaultParameter() {
        assertEquals(BigInteger.ONE, eval("((x = 1) => x)()"));
    }

    @Test
    public void defaultParameters() {
        assertEquals(BigDecimal.valueOf(6), eval("((x = 2, y = 3) => x * y)()"));
    }

    @Test
    public void defaultParametersOr() {
        assertEquals(BigDecimal.valueOf(6), eval("((x = 2, y = 3 || 4) => x * y)()"));
    }

    @Test
    public void defaultSelfReference() {
        assertEquals(BigDecimal.valueOf(6), eval("((x = 2, y = x + 1) => x * y)()"));
    }

    @Test
    public void restOnly() {
        assertEquals(Collections.EMPTY_LIST, eval("((...rest) => rest)()"));
        assertEquals(Arrays.asList("abc", "def", "ghi"),
                eval("((...rest) => rest).apply(null, ['abc', 'def', 'ghi'])"));
    }

    @Test
    public void restWithOthers() {
        assertEquals(BigDecimal.valueOf(7),
                eval("((x, y, ...rest) => rest.reduce((p, c) => p + c, 0))(1, 2, 3, 4)"));
    }

    @Test
    public void twoParams() {
        assertEquals(BigDecimal.valueOf(6), eval("((x, y) => x * y)(2, 3)"));
    }

    @Test
    public void docRef() {
        assertEquals("Hello, World!", eval("(() => 'Hello, ' + $)()", "World!"));
    }

    @Test
    public void limiter() {
        assertEquals(BigInteger.valueOf(15), eval("(a => a > 15 ? 15 : a)(16)"));
    }

    @Test
    public void testBadReturn() {
        try {
            eval("x => 1");
            fail();
        } catch (SnapDataException e) {
            assertEquals(
                    "Unsupported expression result: /* ArrowFunction @ line 1:0 */ x => 1",
                    e.getMessage());
        }
    }

    @Test
    public void reuse() {
        Object meta = eval("{ amount: 2, adder: x => x + this.amount }");
        assertEquals(BigDecimal.valueOf(3), eval("$.adder(1)", meta));
    }

    @Test
    public void defaultWithThis() {
        Object meta = eval("{ amount: 1, deffunc: (x = this.amount) => x }");
        assertEquals(BigInteger.valueOf(1), eval("$.deffunc()", meta));
    }

    @Test
    public void recursion() {
        Object meta = eval("{ fib: n => n <= 1 ? n : this.fib(n - 1) + this.fib(n - 2) }");
        assertEquals(BigDecimal.valueOf(8), eval("$.fib(6)", meta));
    }

    @Test
    public void nestedThis() {
        Object meta = eval("{ val: 1, inner: { val: 2, func: () => this.val } }");
        assertEquals(BigInteger.valueOf(2), eval("$.inner.func()", meta));
    }

    @Test
    public void varCapture() {
        ScopeStack scopeStack = new ScopeStack();
        scopeStack.push(new GlobalScope());
        scopeStack.push(new BasicScope(ImmutableMap.of("__foo__", "bar")));
        Object obj = eval("{ f: () => __foo__, g: () => __foo__ }", null, scopeStack);
        scopeStack.pop();
        assertEquals("bar", eval("$.f()", obj));
    }

    @Test
    public void badVarCaptureNoFail() {
        eval("{ f: () => _foo__ }");
    }

    @Test
    public void purity() {
        Map<String, Object> backing = new HashMap<>();
        BasicScope basicScope = new BasicScope(backing);
        ScopeStack scopeStack = new ScopeStack();
        scopeStack.push(new GlobalScope());
        scopeStack.push(basicScope);

        Object funcs = eval("{ pureWrapper: x => 1, impureWrapper: x => x.shift() }");
        backing.put("lib", funcs);

        List list = new ArrayList<>();
        list.add(BigInteger.ZERO);
        list.add(BigInteger.ONE);
        list.add(BigInteger.valueOf(2));
        Object result;
        result = eval("lib.pureWrapper($) + 'abc' + lib.pureWrapper($)", list, scopeStack);
        assertEquals("1abc1", result);

        result = eval(
                "lib.impureWrapper($) + 'abc' + Math.abs(2) + Math.abs(2) + lib" +
                        ".impureWrapper" +
                        "($) + 'def' + lib" +
                        ".impureWrapper($)", list, scopeStack);
        assertEquals("0abc221def2", result);
    }

    @Test
    public void testImpureObjects() {
        Object result = eval("'1.2'.split('.')[1] + '1.2'.split('.')[1] + '1.2'.split('.')[1] + " +
                "'1.2'.split('.').pop() + '1.2'.split('.')[1]");
        assertEquals("22222", result);
    }

    @Test
    public void badVarCaptureFail() {
        evalError("{ f: () => _foo__ }.f()",
                  "_foo__ is undefined",
                  null,
                  "Perhaps you meant one of the following properties: Array, Base64, Boolean, " +
                          "Date, DateTime");
    }
}
