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

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A variable scope that handles our underscore prefixed pipeline parameter variables.
 *
 * @author tstack
 */
public class EnvironmentScope implements Scope {
    private static final String PREFIX = "_";

    private final Map<String, Object> environ;
    private final Set<String> keySet;

    /**
     * @param environ Map of pipeline variable names to their values.
     */
    public EnvironmentScope(final Map<String, Object> environ) {
        this.environ = environ;
        if (environ != null) {
            keySet = environ.keySet().stream().map(key -> "_" + key).collect(Collectors.toSet());
        } else {
            keySet = Collections.EMPTY_SET;
        }
    }

    @Override
    public Object get(final String name) {
        if (!name.startsWith(PREFIX)) {
            return UNDEFINED;
        }

        String varName = name.substring(1);
        Object retval = environ.get(varName);
        if (retval == null && !environ.containsKey(varName)) {
            return UNDEFINED;
        }
        return retval;
    }

    @Override
    public Set<String> keySet() {
        return keySet;
    }
}
