package com.snaplogic.jsonpath;

import com.snaplogic.common.jsonpath.InvalidPathException;

/**
 * Exception thrown if there was a problem parsing a JSON-Path.
 *
 * @author tstack
 */
public class InvalidPathSyntaxException extends InvalidPathException {
    private final int offset;

    public InvalidPathSyntaxException(final int offset, final String message) {
        this(offset, message, null);
    }

    public InvalidPathSyntaxException(final int offset, final String message,
                                      final Throwable cause) {
        super(message, cause);
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
