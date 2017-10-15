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

package com.snaplogic.jsonpath.tokens;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.CompiledExpressionHolder;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Implements the '.map()' json-path method.
 *
 * @author tstack
 */
public class MapToken extends MethodToken {

    public MapToken(final String value, final CompiledExpressionHolder code) {
        super(value, code);
    }

    @Override
    public Object process(final PathWalker walker, final PathVisitor visitor, final Object obj)
            throws InvalidPathException {
        if (obj instanceof List) {
            List list = (List) obj;
            List<Object> retval = Lists.newArrayListWithExpectedSize(list.size());
            BigInteger index = BigInteger.ZERO;

            for (Object element : list) {
                retval.add(walker.evaluate(code, visitor, index, element));
                index = index.add(BigInteger.ONE);
            }

            return retval;
        } else if (obj instanceof Map) {
            Map<Object, Object> map = (Map) obj;
            Map<Object, Object> retval = Maps.newLinkedHashMap();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                retval.put(entry.getKey(), walker.evaluate(code, visitor, entry.getKey(),
                        entry.getValue()));
            }

            return retval;
        } else {
            return walker.evaluate(code, visitor, null, obj);
        }
    }

    @Override
    public String toString() {
        return String.format("map(%s)", this.value);
    }
}
