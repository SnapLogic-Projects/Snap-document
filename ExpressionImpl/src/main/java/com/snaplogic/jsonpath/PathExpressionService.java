package com.snaplogic.jsonpath;

import com.snaplogic.common.jsonpath.InvalidPathException;

/**
 * Interface for services that can compile and evaluate expressions found in JSON-Paths.
 *
 * @author tstack
 */
public interface PathExpressionService {
    /**
     * Compile an expression.
     *
     * @param path The original path.
     * @param offset The offset of the expression being compiled in the original path.
     * @param inputString The expression to compile.
     * @return The compiled form of the expression.  This value will be passed to evaluate.
     * @throws InvalidPathSyntaxException If there was a problem compiling the expression.
     */
    CompiledExpressionHolder compile(String path, int offset, String inputString)
            throws InvalidPathSyntaxException;

    /**
     * Evaluated a compiled expression.
     *
     * @param code The compiled form of the expression.
     * @param root The root object that is being traversed with JSON-path.
     * @param key The key when iterating over object fields or NULL if not-applicable.
     * @param value The value when iterating over an object/array.
     * @return The result of the expression.
     * @throws InvalidPathException If there was a problem executing the expression.
     */
    Object evaluate(CompiledExpressionHolder code, Object scopes, Object root, Object key, Object
            value) throws InvalidPathException;
}
