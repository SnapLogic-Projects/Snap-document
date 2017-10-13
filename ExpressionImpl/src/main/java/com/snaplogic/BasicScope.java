package com.snaplogic;

import com.snaplogic.api.common.expressions.Scope;

import java.util.Map;
import java.util.Set;

/**
 * A simple variable scope implementation backed by a Map.
 *
 * @author tstack
 */
public class BasicScope implements Scope {
    private final Map<String, Object> backing;

    /**
     * @param backing Map of variable names to their values.
     */
    public BasicScope(Map<String, Object> backing) {
        this.backing = backing;
    }

    @Override
    public Object get(final String name) {
        Object retval = backing.get(name);

        if (retval == null && !backing.containsKey(name)) {
            return UNDEFINED;
        }
        return retval;
    }

    @Override
    public Set<String> keySet() {
        return backing.keySet();
    }
}
