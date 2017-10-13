package com.snaplogic.jsonpath;

import java.util.List;

/**
 * A convenience class for performing operations using a JSON-Path.
 *
 * @author tstack
 */
public interface JsonPath {
    /**
     * Unique value that indicates a read() should throw an exception
     * instead of returning a default value.
     */
    Object NO_DEFAULT = new Object();

    /**
     * @param scopes The scopes to use during expression evaluation.
     * @return A new JsonPath object that is bound to the given scopes.
     */
    JsonPath withScopes(Object scopes);

    /**
     * Read the element in the given data that is specified by this JSON-Path.
     *
     * @param data The input data to traverse.
     * @param <T> The return type.
     * @return The element value, if the path does not include any branching tokens
     *         (like wildcard), or a list of values that match the path.
     * @throws InvalidPathException If there was an issue traversing the path.
     */
    <T> T read(Object data) throws InvalidPathException;

    /**
     * Read the element in the given data that is specified by this JSON-Path.
     *
     * @param data The input data to traverse.
     * @param defaultValue The to use if the path could not be found.
     * @param <T> The return type.
     * @return The element value, if the path does not include any branching tokens
     *         (like wildcard), or a list of values that match the path.
     * @throws InvalidPathException If there was an issue traversing the path.
     */
    <T> T read(Object data, Object defaultValue) throws InvalidPathException;

    /**
     * Read the element in the given data that is specified by this statically
     * defined JSON-Path.  This method differs from the normal read in that the
     * path is known to be good and the data structure has been validated.
     *
     * @param data The input data to traverse.
     * @param <T> The return type.
     * @return The element value, if the path does not include any branching tokens
     *         (like wildcard), or a list of values that match the path.
     */
    <T> T readStatic(Object data);

    /**
     * Read the element in the given data that is specified by this statically
     * defined JSON-Path.  This method differs from the normal read in that the
     * path is known to be good and the data structure has been validated.
     *
     * @param data The input data to traverse.
     * @param defaultValue The to use if the path could not be found.
     * @param <T> The return type.
     * @return The element value, if the path does not include any branching tokens
     *         (like wildcard), or a list of values that match the path.
     */
    <T> T readStatic(Object data, Object defaultValue);

    /**
     * Test if this path is in the given object.
     *
     * @param data The object to check.
     * @return True if this path is in the given object, false otherwise.
     */
    boolean existsIn(Object data);

    /**
     * Write a value to the input data at the path specified by this JSON-Path.
     *
     * @param data The object tree to be traversed.
     * @param value The value to write to the path.
     * @throws InvalidPathException If there was an issue traversing the path.
     */
    void write(Object data, Object value) throws InvalidPathException;

    /**
     * Write a value to the input data at the path specified by this JSON-Path.
     * This method differs from the normal write in that the path is known to be good and
     * the data structure has been validated.
     *
     * @param data The object tree to be traversed.
     * @param value The value to write to the path.
     */
    void writeStatic(Object data, Object value);

    /**
     * Insert a value to the input data at the path specified by this JSON-Path.
     * This method differs from the write() method when inserting into arrays.
     * A call to write will overwrite an existing element in an array, while
     * an insert will add a new element to the array and shift any following
     * elements to the right.  For example, the paths "$.foo[0]" and
     * "$.foo[(@.length)]" will insert elements at the beginning and end of
     * the array, respectively.
     *
     * @param data The object tree to be traversed.
     * @param value The value to write to the path.
     * @throws InvalidPathException If there was an issue traversing the path.
     */
    void insert(Object data, Object value) throws InvalidPathException;

    /**
     * Delete the element(s) specified by this JSON-Path in the given object tree.
     *
     * @param data The object tree to be traversed.
     * @throws InvalidPathException If there was an issue traversing the path.
     */
    void delete(Object data) throws InvalidPathException;

    /**
     * Return a list of all paths that match this JSON-path in the given document.
     *
     * @param data The document to traverse.
     * @return The paths that were found.
     * @throws InvalidPathException If there was a problem traversing the path.
     */
    List<WalkResult> walk(Object data) throws InvalidPathException;

    /**
     * Join two JSON paths together.
     *
     * @param child The child path to append to this path.
     * @return The new JSON-path that is the concatenation of this path and the child.
     */
    JsonPath join(JsonPath child);

    /**
     * Return a new JsonPath object with the given path elements appended to this path.
     *
     * @param paths The path elements to append to this path.
     * @return A new JsonPath object.
     */
    JsonPath children(Object... paths);

    /**
     * @return True if this path can return multiple results.
     */
    boolean hasMultipleResults();

    /**
     * @return The parent path of this path.  If this path refers to the document root,
     *   then this path is returned.
     */
    JsonPath parent();

    /**
     * @return The last component of this JSON-path.  If this path refers to the document
     *   root, then this path is returned.
     */
    JsonPath basename();

    /**
     * Create a path from a portion of this path.
     *
     * @param startIndex The starting index (inclusive) for the tokens to include in the returned
     *                   path.  If this value is negative, the index is taken from the end of the
     *                   token list instead of the start.
     * @param endIndex The ending index (exclusive) for the tokens to include in the returned path.
     * @return A sub-path of this path.
     */
    JsonPath subPath(int startIndex, int endIndex);

    /**
     * Create a path from a trailing portion of this path.
     *
     * @param startIndex The starting index (inclusive) for the tokens to include in the returned
     *                   path.  If this value is negative, the index is taken from the end of the
     *                   token list instead of the start.
     * @return A sub-path of this path.
     */
    default JsonPath subPath(int startIndex) {
        return subPath(startIndex, -1);
    }

    /**
     * Attempt to resolve a simple path to the list of name strings or integer indexes.  For
     * example, the path "$.foo.bar[0]" will resolve to a list containing "foo", "bar", and
     * the number zero.  Resolving paths that contain branching tokens, like filters and
     * descent, is not supported.
     *
     * @param data The root object to use when evaluating expressions, can be null.
     * @return A list of elements on the path.
     * @throws InvalidPathException If the path cannot be resolved.
     */
    List<Object> resolvedPath(Object data) throws InvalidPathException;

    /**
     * Test if this path starts with the components of the given path.
     *
     * @param head The leading components to check for.
     * @return True if this path ends with the given head, false otherwise.
     */
    boolean startsWith(JsonPath head);

    /**
     * Test if this path ends with the components of the given path.
     *
     * @param tail The tail components to check for.
     * @return True if this path ends with the given tail, false otherwise.
     */
    boolean endsWith(JsonPath tail);

    /**
     * @return True if this path references the root.
     */
    boolean isRoot();
}

