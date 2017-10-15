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

import com.google.common.collect.ImmutableSet;
import com.snaplogic.common.expressions.Scope;

import java.util.Set;

/**
 * Scope for the document value.
 *
 * @author tstack
 */
public class DocumentScope implements Scope {
    private static final String DOCUMENT_SYMBOL = "$";
    private static final ImmutableSet<String> KEY_SET = ImmutableSet.of(DOCUMENT_SYMBOL);
    private final Object value;

    public DocumentScope(final Object value) {
        this.value = value;
    }

    @Override
    public Object get(final String name) {
        if (DOCUMENT_SYMBOL.equals(name)) {
            return value;
        }

        return UNDEFINED;
    }

    @Override
    public Set<String> keySet() {
        return KEY_SET;
    }
}
