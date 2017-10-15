/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.expression;

import com.google.common.collect.ImmutableSet;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.expression.util.LiteralUtils;
import com.snaplogic.jsonpath.CompiledExpressionHolder;
import com.snaplogic.jsonpath.InvalidPathSyntaxException;
import com.snaplogic.jsonpath.PathExpressionService;
import com.snaplogic.jsonpath.tokens.NameToken;
import com.snaplogic.util.DefaultValueHandler;

import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import static com.snaplogic.expression.Messages.PLEASE_CHECK_YOUR_EXPRESSION;

/**
 * A JSON-Path expression service that uses the SnapLogic expression language.
 *
 * @author tstack
 */
public class JsonPathExpressionService implements PathExpressionService {
    private static final Pattern STATIC_STRING = Pattern.compile(
            "\"((?:\\\\.|[^\"])+)\"|'((?:\\\\.|[^'])+)'");
    private static final DefaultValueHandler DEFAULT_VALUE_HANDLER = new DefaultValueHandler();
    private static final ImmutableSet<String> KEY_SET = ImmutableSet.of(NameToken.ROOT_ELEMENT,
            NameToken.CURRENT_KEY, NameToken.CURRENT_ELEMENT, NameToken.CURRENT_VALUE);
    private final Stack<Scope> compatScopes = new Stack<>();

    @Override
    public CompiledExpressionHolder compile(final String path, final int offset,
                                            final String inputString) throws InvalidPathSyntaxException {
        // Check if the expression is just an integer or string constant so that
        // we can avoid the expression language.
        try {
            Integer intValue = Integer.valueOf(inputString);
            return new CompiledExpressionHolder(intValue);
        } catch (Exception nfe) {
            if (STATIC_STRING.matcher(inputString).matches()) {
                String strValue = LiteralUtils.unescapeString(inputString);
                return new CompiledExpressionHolder(strValue);
            } else {
                try {
                    return new CompiledExpressionHolder(ExpressionUtil.compile(inputString));
                } catch (RuntimeException e) {
                    InvalidPathSyntaxException exception = new InvalidPathSyntaxException(
                            offset, e.getMessage(), e)
                            .withPath(inputString)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
                    throw exception;
                }
            }
        }
    }

    @Override
    public Object evaluate(final CompiledExpressionHolder code, final Object scopes, final Object
            root, final Object key, final Object value) throws InvalidPathException {
        try {
            Object constantOrJaninoExpr = code.getCode();
            if (!(constantOrJaninoExpr instanceof SnapLogicExpression)) {
                return constantOrJaninoExpr;
            }
            Scope wrapperScope = new Scope() {
                @Override
                public Object get(final String name) {
                    switch (name) {
                        case NameToken.ROOT_ELEMENT:
                            return root;
                        case NameToken.CURRENT_KEY:
                            if (key != null) {
                                return key;
                            } else {
                                return Scope.UNDEFINED;
                            }
                        case NameToken.CURRENT_ELEMENT:
                        case NameToken.CURRENT_VALUE:
                            return value;
                    }

                    return Scope.UNDEFINED;
                }

                @Override
                public Set<String> keySet() {
                    return KEY_SET;
                }
            };

            ScopeStack newScopes = new ScopeStack();
            // XXX Need to support scopes attached to this object for the sake of backwards
            // compatibility
            if (!compatScopes.isEmpty()) {
                newScopes.pushAllScopes(compatScopes);
            }
            if (scopes != null) {
                newScopes.pushAllScopes((Stack<Scope>) scopes);
            } else {
                newScopes.push(new GlobalScope());
            }
            newScopes.push(wrapperScope);
            SnapLogicExpression expr = (SnapLogicExpression) code.getCode();
            return expr.evaluate(root, newScopes, DEFAULT_VALUE_HANDLER);
        } catch (Throwable e) {
            throw new InvalidPathException(e.getMessage(), e)
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION);
        }
    }

    public Stack<Scope> getScopes() {
        return compatScopes;
    }
}
