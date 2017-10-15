/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.methods.map;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.MethodUtils;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.object.ObjectMethod;
import com.snaplogic.util.DeepUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for map methods in expression language.
 *
 * @author jinloes
 */
public class MapMethod extends ObjectMethod {
    private static final String METHOD_SEPARATOR = ",";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, Method> MAP_METHODS = ImmutableMap.<String, Method>builder()
            .putAll(OBJECT_METHODS)
            .put("filter", Filter.INSTANCE)
            // These map methods come from lodash.
            .put("mapKeys", MapKeys.INSTANCE)
            .put("mapValues", MapValues.INSTANCE)
            .put("merge", Merge.INSTANCE)
            .put("isEmpty", IsEmpty.INSTANCE)
            .put("extend", Extend.INSTANCE)
            .put("get", GetWithDefault.INSTANCE)
            .build();

    public static Method getMethod(String methodName) {
        Method method = MAP_METHODS.get(methodName);
        if (method == null) {
            throw new UnknownMethodException(MAP_METHODS.keySet());
        }
        return method;
    }

    /**
     * Implementation of the filter() method, which is modeled after the method used for Arrays.
     */
    private enum Filter implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            JavascriptFunction func = MethodUtils.getFunction(args, 0);
            Map<String, Object> map = (Map) member;
            Map<String, Object> retval = new LinkedHashMap<>(map.size());

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                boolean include = ObjectType.toBoolean(func.eval(Arrays.asList(entry.getValue(),
                        entry.getKey(), map)));
                if (include) {
                    retval.put(entry.getKey(), entry.getValue());
                }
            }
            return retval;
        }
    }

    /**
     * Implementation of the mapKeys() method defined by lodash.
     *
     * https://lodash.com/docs/4.17.2#mapKeys
     */
    private enum MapKeys implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            JavascriptFunction func = MethodUtils.getFunction(args, 0);
            Map<String, Object> map = (Map) member;
            Map<String, Object> retval = new LinkedHashMap<>(map.size());

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                retval.put(String.valueOf(func.eval(Arrays.asList(entry.getValue(), entry.getKey(),
                        map))), entry.getValue());
            }
            return retval;
        }
    }

    /**
     * Implementation of the mapValues() method defined by lodash.
     *
     * https://lodash.com/docs/4.17.2#mapValues
     */
    private enum MapValues implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            JavascriptFunction func = MethodUtils.getFunction(args, 0);
            Map<String, Object> map = (Map) member;
            Map<String, Object> retval = new LinkedHashMap<>(map.size());

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                retval.put(entry.getKey(), func.eval(Arrays.asList(entry.getValue(), entry
                        .getKey(), map)));
            }
            return retval;
        }
    }

    /**
     * Implementation of the merge() method defined by lodash.
     *
     * https://lodash.com/docs/4.17.2#merge
     */
    private enum Merge implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            Map<String, Object> map = (Map) member;
            Object retval = DeepUtils.copy(map);

            for (Object arg : args) {
                retval = DeepUtils.merge(retval, arg);
            }
            return retval;
        }
    }

    private enum GetWithDefault implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            if (args.isEmpty()) {
                return null;
            }
            Map<String, Object> map = (Map) member;
            String key = ObjectType.toString(args.get(0));
            if (map.containsKey(key)) {
                return map.get(key);
            }
            if (args.size() > 1) {
                return args.get(1);
            }
            return null;
        }
    }
}