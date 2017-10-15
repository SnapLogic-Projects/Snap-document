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
package com.snaplogic.util;

import com.google.inject.Singleton;
import com.snaplogic.Document;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.services.SnapExpressionService;
import com.snaplogic.snap.api.SnapDataException;

import java.util.Stack;

/**
 * The DataValueHandler that returns null for undefined references.
 *
 * @author tstack
 */
@Singleton
public class NullValueHandler extends DefaultValueHandler {
    @Override
    public Object handleEval(final SnapExpressionService snapExpressionService,
                             final String expression, final Document document, final Object data,
                             final Stack<Scope> scopes) {
        try {
            return super.handleEval(snapExpressionService, expression, document, data, scopes);
        } catch (SnapDataException e) {
            return null;
        }
    }

    @Override
    public Object handleUndefinedReference(final Document originalDocument, final String fieldName,
            final String fieldPath) throws SnapDataException {
        return null;
    }
}