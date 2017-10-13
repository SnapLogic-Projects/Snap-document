package com.snaplogic.common.expressions;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Convenience Stack-class with helper methods for doing variable lookups.
 *
 * @author tstack
 */
public class ScopeStack extends Stack<Scope> {
    /**
     * Lookup a variable with the given name in this stack of scopes.
     *
     * @param name The variable to look up.
     * @return The value of the variable or Scope.UNDEFINED.
     */
    public Object lookupVariable(String name) {
        for (int index = this.size() - 1; index >= 0; index--) {
            Object retval = this.get(index).get(name);
            if (retval != Scope.UNDEFINED) {
                return retval;
            }
        }
        return Scope.UNDEFINED;
    }

    public Set<String> keySet() {
        Set<String> retval = new HashSet<>();

        for (int index = 0; index < size(); index++) {
            retval.addAll(get(index).keySet());
        }
        return retval;
    }

    /**
     * Push all of the scopes from the given stack onto this one.
     *
     * @param newScopes The stack of scopes to include in this stack.
     */
    public void pushAllScopes(Stack<Scope> newScopes) {
        if (newScopes == null) {
            return;
        }
        for (Scope scope : newScopes) {
            this.push(scope);
        }
    }
}
