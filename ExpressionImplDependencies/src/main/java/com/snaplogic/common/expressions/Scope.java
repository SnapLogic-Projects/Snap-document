package com.snaplogic.common.expressions;

import java.io.Serializable;
import java.util.Set;

/**
 * Interface for variable scopes.  The interface is pretty simplistic since variables
 * are read-only in the expression language.
 *
 * @author tstack
 */
public interface Scope extends Serializable {
    Object UNDEFINED = new Object();

    /**
     * Get the variable with the given name.
     *
     * @param name The name of the variable.
     * @return The variable value or UNDEFINED if variable is not defined in this scope.
     */
    Object get(String name);

    Set<String> keySet();
}
