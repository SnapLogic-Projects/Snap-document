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

package com.snaplogic.common.jsonpath;

import java.util.ArrayList;
import java.util.List;

import static com.snaplogic.common.jsonpath.Messages.DEFAULT_PATH_RESOLUTION;

/**
 * Base exception for the JSON-Path parser and walker.
 *
 * @author tstack
 */
public class InvalidPathException extends Exception {
    private String resolution = DEFAULT_PATH_RESOLUTION;
    private Object parent = null;
    private Object path = null;
    private final List<Object> parentPath = new ArrayList<>();

    public InvalidPathException(final String message) {
        super(message);
    }

    public InvalidPathException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public <T extends InvalidPathException> T withResolution(String resolution) {
        this.resolution = resolution;
        return (T) this;
    }

    /**
     * @param path The path or sub-path which caused the problem.
     * @return this
     */
    public <T extends InvalidPathException> T withPath(Object path) {
        if (this.path == null) {
            this.path = path;
        }
        return (T) this;
    }

    /**
     * The value that was being traversed when the invalid path was encountered.
     *
     * @param parent The value.
     * @return this
     */
    public <T extends InvalidPathException> T withParent(Object parent) {
        this.parent = parent;
        return (T) this;
    }

    public String getResolution() {
        return resolution;
    }

    public String getPath() {
        return String.valueOf(path);
    }

    public Object getParent() {
        return parent;
    }

    /**
     * @return The actual path to the parent object.  For example, if the
     * input path is '$foo[*]' and the error occurred while traversing the
     * third element of the array, this will contain '[$, foo, 3]'.
     */
    public List<Object> getParentPath() {
        return parentPath;
    }
}
