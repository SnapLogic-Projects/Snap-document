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

import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.common.jsonpath.WalkResult;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple container for results of the JsonPath.walk() method.
 */
public class WalkResultImpl implements WalkResult {
    private final List<Pair<Object, Object>> parentPath;
    private final Object value;

    public WalkResultImpl(List<Pair<Object, Object>> parentPath, Object value) {
        this.parentPath = parentPath;
        this.value = value;
    }

    @Override
    public List<Pair<Object, Object>> getParentPath() {
        return parentPath;
    }

    @Override
    public JsonPath getActualPath() {
        List<Object> pathObjects = new ArrayList();
        for (Pair<Object, Object> pair : parentPath) {
            pathObjects.add(pair.getRight());
        }
        return JsonPathImpl.compile(pathObjects);
    }

    @Override
    public Map<Object, Object> getParentHierarchy() {
        Map<Object, Object> parentMap = new LinkedHashMap<>();
        for (Pair<Object, Object> pair : parentPath) {
            Object parentObj = pair.getLeft();
            Object childObj = null;
            if (parentObj instanceof List) {
                childObj = ((List) parentObj).get((Integer) pair.getRight());
            } else if (parentObj instanceof Map) {
                childObj = ((Map<Object, Object>) parentObj).get(pair.getRight());
            }
            // parentMap contains key/value pairs that have value NOT of type Map/List
            if (!(childObj instanceof Map)) {
                continue;
            }
            Map<Object, Object> childVal = (Map<Object, Object>) childObj;
            for (Map.Entry<Object, Object> entry : childVal.entrySet()) {
                if (!(entry.getValue() instanceof Map) &&
                        !(entry.getValue() instanceof List)) {
                    parentMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return parentMap;
    }

    @Override
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public <T> T setValue(Object value) {
        Pair<Object, Object> parentPair = parentPath.get(parentPath.size() - 1);
        Object parent = parentPair.getLeft();

        if (parent instanceof Map) {
            Map map = (Map) parent;

            map.put(parentPair.getRight(), value);
        } else if (parent instanceof List) {
            List list = (List) parent;

            list.set((int) parentPair.getRight(), value);
        }

        Pair<Object, Object> rootPair = parentPath.get(0);
        Map<String, Object> root = (Map<String, Object>) rootPair.getLeft();
        return (T) root.get(rootPair.getRight());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalkResultImpl)) {
            return false;
        }

        final WalkResultImpl that = (WalkResultImpl) o;

        if (!parentPath.equals(that.parentPath)) {
            return false;
        }
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = parentPath != null ? parentPath.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
