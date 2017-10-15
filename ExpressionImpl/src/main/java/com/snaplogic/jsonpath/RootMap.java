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
package com.snaplogic.jsonpath;

import com.snaplogic.jsonpath.tokens.NameToken;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Dummy map that wraps the root value (i.e. $)
 *
 * XXX This implementation is simpler and a tiny bit faster than constructing a
 * HashMap to hold the root value.
 *
 * @author tstack
 */
public final class RootMap implements Map<String, Object> {
    Object rootValue = null;

    public RootMap(Object rootValue) {
        this.rootValue = rootValue;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(final Object key) {
        return NameToken.ROOT_ELEMENT.equals(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return rootValue == value;
    }

    @Override
    public Object get(final Object key) {
        return rootValue;
    }

    @Override
    public Object put(final String key, final Object value) {
        Object retval = rootValue;
        rootValue = value;
        return retval;
    }

    @Override
    public Object remove(final Object key) {
        Object retval = rootValue;
        rootValue = null;
        return retval;
    }

    @Override
    public void putAll(final Map<? extends String, ?> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        rootValue = null;
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Object> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
