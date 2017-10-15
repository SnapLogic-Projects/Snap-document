/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2012, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */
package com.snaplogic.util;

import org.apache.commons.io.output.StringBuilderWriter;

/**
 * Helper to create the path, currently only supports dotted path expressions.
 *
 * @author mklumpp
 */
@SuppressWarnings("serial")
public final class JsonPathBuilder extends StringBuilderWriter {

    public final static String ROOT_ELEMENT = "$";
    public final static String CURRENT_ELEMENT = ".";
    public final static String ALL_LIST_ELEMENTS = "*";

    public final static String OPENING_BRACKET = "[";
    public final static String CLOSING_BRACKET = "]";

    public final static String ROOT_ARRAY = OPENING_BRACKET + ALL_LIST_ELEMENTS + CLOSING_BRACKET;
    /**
     * This value is copied here from {@link Constants} to keep jutils independent of jcommon
     * module.
     */
    private static final String SNAP_PROPERTY_VALUE = "value";

    public JsonPathBuilder(String part) {
        write(part);
    }

    public JsonPathBuilder appendCurrentElement(String element) {
        if (!element.startsWith(OPENING_BRACKET)) {
            write(CURRENT_ELEMENT);
        }
        write(element);
        return this;
    }

    public JsonPathBuilder appendListIndex(Integer index) {
        write(OPENING_BRACKET);
        write(index.toString());
        write(CLOSING_BRACKET);
        return this;
    }

    public JsonPathBuilder appendAllListElements() {
        write(OPENING_BRACKET);
        write(ALL_LIST_ELEMENTS);
        write(CLOSING_BRACKET);
        return this;
    }

    public JsonPathBuilder append(String element) {
        write(element);
        return this;
    }

    public JsonPathBuilder appendValueElement() {
        return appendCurrentElement(SNAP_PROPERTY_VALUE);
    }

    public String build() {
        String path = toString();
        close();
        return path;
    }
}
