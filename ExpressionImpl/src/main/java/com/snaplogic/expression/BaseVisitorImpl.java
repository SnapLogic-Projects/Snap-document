/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.Document;
import com.snaplogic.common.expressions.DataValueHandler;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.grammars.SnapExpressionsBaseVisitor;
import com.snaplogic.grammars.SnapExpressionsParser;
import com.snaplogic.util.DefaultValueHandler;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Stack;

/**
 * BaseVisitorImpl is the base class for all the visitor implementations.
 *
 * @param <T> return type
 * @author ksubramanian
 * @since 2015
 */
public abstract class BaseVisitorImpl<T> extends SnapExpressionsBaseVisitor<T> {
    protected static final DataValueHandler DEFAULT_HANDLER = new DefaultValueHandler();
    protected static final GlobalScope GLOBAL_SCOPE = new GlobalScope();
    protected final DataValueHandler dataValueHandler;
    protected final Document originalDocument;
    protected final ScopeStack scopes = new ScopeStack();
    protected final Stack<SnapExpressionsParser.AtomicExpContext> currentAtomic = new Stack<>();

    public BaseVisitorImpl(Document originalDocument, DataValueHandler dataValueHandler) {
        this.originalDocument = originalDocument;
        this.dataValueHandler = dataValueHandler;
    }

    public Document getOriginalDocument() {
        return originalDocument;
    }

    public void pushAllScopes(Stack<Scope> newScopes) {
        for (Scope scope : newScopes) {
            this.scopes.push(scope);
        }
    }

    public void pushScope(Scope scope) {
        scopes.push(scope);
    }

    public void popScope() {
        scopes.pop();
    }

    public ScopeStack getScopes() {
        return this.scopes;
    }

    /**
     * Extract the source code for a path reference that starts with the
     * current atomic expression and ends with the given context.
     *
     * @param lastContext The end of the path.
     * @return The source code for the path.
     */
    protected String getCurrentPath(ParserRuleContext lastContext) {
        SnapExpressionsParser.AtomicExpContext atomicExpContext = currentAtomic.peek();
        Token startToken = atomicExpContext.getStart();
        CharStream charStream = startToken.getInputStream();
        int lastIndex = lastContext.getStop().getStopIndex();
        Interval interval = new Interval(startToken.getStartIndex(), lastIndex);
        return StringEscapeUtils.escapeJava(charStream.getText(interval));
    }
}
