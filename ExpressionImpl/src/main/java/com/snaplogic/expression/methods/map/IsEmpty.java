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

package com.snaplogic.expression.methods.map;

import com.snaplogic.expression.methods.Method;

import java.util.List;
import java.util.Map;

/**
 * Implements a method to check if an object is empty.
 *
 * @author tstack
 */
public enum IsEmpty implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        if (member instanceof Map) {
            Map map = (Map) member;

            return map.isEmpty();
        }

        return false;
    }
}
