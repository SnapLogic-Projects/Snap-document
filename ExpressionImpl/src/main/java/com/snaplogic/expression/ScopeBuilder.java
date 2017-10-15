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

import com.snaplogic.common.expressions.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for constructing an expression language scope with a given set of
 * variables.
 *
 * @author tstack
 */
public class ScopeBuilder {
    private final Map<String, Object> backing = new HashMap<>();

    public ScopeBuilder addVariable(String name, Object value) {
        backing.put(name, value);
        return this;
    }

    public Scope build() {
        return new BasicScope(backing);
    }
}
