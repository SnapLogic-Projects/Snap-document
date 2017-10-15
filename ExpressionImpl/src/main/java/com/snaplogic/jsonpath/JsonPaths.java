/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.jsonpath;

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.jsonpath.tokens.NameToken;

import java.util.Arrays;

/**
 * A helper class for dealing with JsonPath objects.
 *
 * @author tstack
 */
public class JsonPaths {
    private static final JsonPath ROOT = JsonPathImpl.compileStatic(NameToken.ROOT_ELEMENT);

    private static final JsonPath ALL_ELEMENTS = JsonPathImpl.compileStatic("$[*]");

    /**
     * @return A JsonPath that refers to the root object.
     */
    public static JsonPath root() {
        return ROOT;
    }

    /**
     * @return A JsonPath that consists of a wildcard (i.e. $[*]).
     */
    public static JsonPath all() {
        return ALL_ELEMENTS;
    }

    public static JsonPath compile(String path) throws InvalidPathException {
        return JsonPathImpl.compile(path);
    }

    public static JsonPath compileStatic(String path) {
        return JsonPathImpl.compileStatic(path);
    }

    public static JsonPath compile(Object... path) {
        return JsonPathImpl.compile(Arrays.asList(path));
    }
}
