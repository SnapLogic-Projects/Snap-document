package com.snaplogic.expression.methods;

import java.util.List;

/**
 * Interface for method in expression language.
 */
public interface Method {
    Object evaluate(Object member, List args);
}

