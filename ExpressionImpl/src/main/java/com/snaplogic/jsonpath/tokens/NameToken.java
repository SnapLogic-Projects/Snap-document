package com.snaplogic.jsonpath.tokens;


import com.snaplogic.jsonpath.InvalidPathException;

/**
 * Container for a plain name in a JSON-Path.
 *
 * @author tstack
 */
public class NameToken extends TraversalToken {
    public static final String ROOT_ELEMENT = "$";
    public static final String CURRENT_ELEMENT = "@";
    public static final String CURRENT_KEY = "key";
    public static final String CURRENT_VALUE = "value";

    public static final NameToken ROOT = new NameToken(ROOT_ELEMENT);

    private final String value;

    public NameToken(String value) {
        this.value = value;
    }

    @Override
    public Object resolve() throws UnsupportedPathException {
        return value;
    }

    @Override
    public void traverse(final PathWalker walker, final int index, final Object obj,
                         final PathVisitor visitor) throws InvalidPathException {
        walker.traverseObjectField(index, obj, this.value, visitor);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NameToken)) {
            return false;
        }

        final NameToken nameToken = (NameToken) o;

        if (!value.equals(nameToken.value)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        return this.value;
    }
}

