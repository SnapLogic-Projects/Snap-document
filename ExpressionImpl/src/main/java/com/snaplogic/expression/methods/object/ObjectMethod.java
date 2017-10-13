package com.snaplogic.expression.methods.object;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.methods.Method;

import java.util.Map;

/**
 * Methods common to all objects in expression language.
 *
 * @author jinloes
 */
public abstract class ObjectMethod {
    // In implementing classes .putAll(OBJECT_METHODS) must be called to ensure object methods
    // get added into all types!
    protected static final Map<String, Method> OBJECT_METHODS =
            ImmutableMap.<String, Method>builder()
                    .put("hasOwnProperty", HasOwnProperty.INSTANCE)
                    .put("toString", ToString.INSTANCE)
                    .build();
}