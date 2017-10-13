package com.snaplogic.jsonpath;

/**
 * Simple container for expressions compiled by the PathExpressionService.
 *
 * @author tstack
 */
public class CompiledExpressionHolder {
    private final Object code;

    public CompiledExpressionHolder(final Object code) {
        this.code = code;
    }

    public Object getCode() {
        return code;
    }
}

