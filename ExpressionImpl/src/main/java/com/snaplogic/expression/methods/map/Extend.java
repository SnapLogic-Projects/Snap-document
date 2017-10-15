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

import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.snap.api.SnapDataException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.snaplogic.expression.methods.map.Messages.*;

/**
 * Implements an 'extend()' method that merges the members of the object
 * arguments into this object.
 *
 * @author tstack
 */
public enum Extend implements Method {
    INSTANCE;

    @Override
    public Object evaluate(final Object member, final List args) {
        Map retval = new LinkedHashMap<>((Map) member);
        for (Object arg : args) {
            Map map = convertToMap(arg);
            if (map == null) {
                continue;
            }
            retval.putAll(map);
        }
        return retval;
    }

    public static Map convertToMap(Object arg) {
        if (arg == null) {
            return null;
        }
        if (arg instanceof List) {
            Map<String, Object> map = new LinkedHashMap<>();
            List list = ((List) arg);
            int i = 0;

            for (Object elem : list) {
                String key = null;
                Object value = null;
                if (elem instanceof List) {
                    List listElem = ((List) elem);
                    if (listElem.size() == 2) {
                        // Treat it as a list of key/value pairs.
                        key = String.valueOf(listElem.get(0));
                        value = listElem.get(1);
                    }
                }
                if (key == null) {
                    // It doesn't look like a list of pairs, so use the index as the key
                    // value.
                    key = String.valueOf(i);
                    value = elem;
                }
                map.put(key, value);
                i += 1;
            }
            arg = map;
        }
        if (!(arg instanceof Map)) {
            throw new SnapDataException(ARGUMENT_IS_NOT_AN_OBJECT_FOUND)
                    .formatWith(ObjectType.objectToType(arg))
                    .withReason(ARGUMENTS_TO_EXTEND_MUST_BE_OBJECTS)
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
        }
        return (Map) arg;
    }
}
