package com.snaplogic.expression.classes;

import com.snaplogic.api.common.expressions.ScopeStack;

import java.util.List;

/**
 *.Represents the Array type in JavaScript
 *
 * @author tstack
 */
public enum JavascriptArray implements JavascriptClass {
    INSTANCE;

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   List<Object> args) {
        return null;
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return value instanceof List;
    }
}
