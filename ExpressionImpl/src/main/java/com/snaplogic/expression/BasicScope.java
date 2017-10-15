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

import java.util.Map;
import java.util.Set;

/**
 * A simple variable scope implementation backed by a Map.
 *
 * @author tstack
 */
public class BasicScope implements Scope {
    private final Map<String, Object> backing;

    /**
     * @param backing Map of variable names to their values.
     */
    public BasicScope(Map<String, Object> backing) {
        this.backing = backing;
    }

    @Override
    public Object get(final String name) {
        Object retval = backing.get(name);

        if (retval == null && !backing.containsKey(name)) {
            return UNDEFINED;
        }
        return retval;
    }

    @Override
    public Set<String> keySet() {
        return backing.keySet();
    }
}
