package com.snaplogic.expression;

/**
 * An internal type for storing variable references to be used
 * when we want to turn an expression like ('foo ' + $foo) into
 * "foo $foo".  Theoretically, we should be able to handle this
 * by just returning an object in
 * DataValueHandler.handleUndefinedReference() that implements
 * the Map and List interfaces.  However, that is not possible
 * because there is a remove() method in both interfaces that
 * clash.  So, we need this hack to store the reference path.
 *
 * @author tstack
 */
public class PathReference {
    private final String path;

    public PathReference(final String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
