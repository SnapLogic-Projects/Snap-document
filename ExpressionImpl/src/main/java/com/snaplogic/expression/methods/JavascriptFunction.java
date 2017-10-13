package com.snaplogic.expression.methods;

import java.util.List;

/**
 * Represents an expression-language function.
 *
 * @author tstack
 */
public interface JavascriptFunction {
    Object eval(List<Object> args);
}
