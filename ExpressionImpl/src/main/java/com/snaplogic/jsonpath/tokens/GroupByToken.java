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

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.CompiledExpressionHolder;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;
import com.snaplogic.jsonpath.UnexpectedTypeException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.tokens.Messages.CHANGE_THE_EXPRESSION_TO_RETURN_A_STRING;
import static com.snaplogic.jsonpath.tokens.Messages.EXPECTING_A_STRING_AS_GROUP_KEY;

/**
 * Implements the '.group_by()' json-path method.
 *
 * @author tstack
 */
public class GroupByToken extends MethodToken {

    public GroupByToken(final String value, final CompiledExpressionHolder code) {
        super(value, code);
    }

    @Override
    public Object process(final PathWalker walker, final PathVisitor visitor, final Object obj)
            throws InvalidPathException {
        Map<String, List<Object>> retval = new LinkedHashMap<>();
        if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            BigInteger index = BigInteger.ZERO;
            for (Object elem : list) {
                addToGroup(walker, retval, index, elem, visitor);
                index = index.add(BigInteger.ONE);
            }
        } else if (obj instanceof Map) {
            Map<Object, Object> map = ((Map) obj);
            for (Map.Entry entry : map.entrySet()) {
                addToGroup(walker, retval, entry.getKey(), entry.getValue(), visitor);
            }
        } else {
            addToGroup(walker, retval, null, obj, visitor);
        }
        return retval;
    }

    private void addToGroup(final PathWalker walker, final Map<String, List<Object>> retval,
                            final Object elemKey, final Object elem, PathVisitor visitor) throws
            InvalidPathException {
        Object key = walker.evaluate(code, visitor, elemKey, elem);

        if (key instanceof List || key instanceof Map) {
            throw new UnexpectedTypeException(String.class,
                    EXPECTING_A_STRING_AS_GROUP_KEY)
                    .withPath(this.toString())
                    .withParent(key)
                    .withResolution(CHANGE_THE_EXPRESSION_TO_RETURN_A_STRING);
        }

        String keyString = String.valueOf(key);
        List<Object> sublist = retval.get(keyString);
        if (sublist == null) {
            sublist = new ArrayList<>();
            retval.put(keyString, sublist);
        }
        sublist.add(elem);
    }

    @Override
    public String toString() {
        return String.format("group_by(%s)", this.value);
    }
}
