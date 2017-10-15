/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2013, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.jsonpath;

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.jsonpath.tokens.MethodToken;
import com.snaplogic.jsonpath.tokens.NameToken;
import com.snaplogic.jsonpath.tokens.PathToken;
import com.snaplogic.jsonpath.tokens.TraversalToken;

import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.Messages.*;

/**
 * Class that follows a JSON-Path for a given object tree.
 *
 * The walker takes a parsed JSON-Path from the PathParser and
 * iterates over the components of the path, traversing an object
 * tree provided by the caller.  When the last token in the path
 * is reached, it will call out to a PathVisitor to handle the
 * final processing.
 *
 * @author tstack
 */
public class PathWalker {
    private static final String PATH_JOIN_FORMAT = "%s.%s";
    private final PathWalker prefix;
    private final List<PathToken> methodStream;
    private final List<PathToken> tokens;
    private final PathExpressionService expressionHandler;
    private final boolean multipleResults;
    private final int tokenCount;
    protected final NameToken simplePath;

    /**
     * @param tokens The tokens parsed from a JSON-Path.
     * @param expressionService The service used to evaluate expressions.
     */
    public PathWalker(List<PathToken> tokens, PathExpressionService expressionService) {
        // Split the tokens around any method call segments we find.  We only
        // care about the last call segment, we'll pass any preceding tokens
        // to the 'prefix' PathWalker so it can do another segmentation.
        int firstMethodTokenIndex = -1, lastMethodTokenIndex = -1;
        boolean foundBranching = false;
        for (int i = tokens.size() - 1; i >= 0; i--) {
            PathToken token = tokens.get(i);

            if (token instanceof MethodToken) {
                if (lastMethodTokenIndex == -1) {
                    // Found the end of a method call segment.
                    lastMethodTokenIndex = i;
                }
            } else if (lastMethodTokenIndex != -1 && firstMethodTokenIndex == -1) {
                // Found the start of the method call segment.
                firstMethodTokenIndex = i + 1;
            }
            if (token instanceof TraversalToken) {
                if (((TraversalToken) token).isBranchingToken()) {
                    foundBranching = true;
                }
            }
        }
        if (firstMethodTokenIndex == -1) {
            prefix = null;
            methodStream = null;
        } else {
            prefix = new PathWalker(tokens.subList(0, firstMethodTokenIndex), expressionService);
            methodStream = tokens.subList(firstMethodTokenIndex, lastMethodTokenIndex + 1);
            tokens = tokens.subList(lastMethodTokenIndex + 1, tokens.size());
            if (tokens.isEmpty()) {
                // If the last token in a path is a method, the whole path will no longer
                // be considered branching.
                foundBranching = false;
            }
        }
        this.tokens = tokens;
        this.tokenCount = tokens.size();
        this.expressionHandler = expressionService;
        this.multipleResults = foundBranching;
        if (prefix == null && tokens.size() == 2 && NameToken.ROOT.equals(tokens.get(0)) &&
                tokens.get(1) instanceof NameToken) {
            simplePath = (NameToken) tokens.get(1);
        } else {
            simplePath = null;
        }
    }

    /**
     * Copy constructor.
     *
     * @param original The original PathWalker instance to copy.
     */
    public PathWalker(PathWalker original) {
        this.prefix = original.prefix;
        this.methodStream = original.methodStream;
        this.tokens = original.tokens;
        this.multipleResults = original.hasMultipleResults();
        this.expressionHandler = original.expressionHandler;
        this.tokenCount = original.tokenCount;
        this.simplePath = original.simplePath;
    }

    /**
     * @return True if the contained path will return a list of results.
     */
    public boolean hasMultipleResults() {
        return multipleResults;
    }

    public NameToken getSimplePath() {
        return simplePath;
    }

    public boolean isRoot() {
        return (this.methodStream == null || this.methodStream.isEmpty()) && this.tokens.size()
                == 1;
    }

    /**
     * @return The tokens that this walker will follow when traversing an object tree.
     */
    public List<PathToken> getTokens() {
        return this.tokens;
    }

    public void flattenTokensTo(List<PathToken> tokensOut) {
        if (prefix != null) {
            prefix.flattenTokensTo(tokensOut);
        }
        if (methodStream != null) {
            tokensOut.addAll(methodStream);
        }
        tokensOut.addAll(tokens);
    }

    /**
     * @return The environment values to make available to any expressions in a path.
     */
    public PathExpressionService getExpressionHandler() {
        return this.expressionHandler;
    }

    /**
     * Evaluate a JavaScript expression.
     *
     * @param key The key value when processing a map.
     * @param value The data to be used for the "current element" symbol (i.e. @).
     * @return The result of the expression.
     * @throws InvalidPathException If there was a problem parsing or executing
     * the given expression.
     */
    public Object evaluate(CompiledExpressionHolder code, final PathVisitor visitor,
                           final Object key, final Object value) throws InvalidPathException {
        return expressionHandler.evaluate(code, visitor.getScopes(), visitor.getRoot(), key, value);
    }

    /**
     * @param index The index in the list of path tokens.
     * @return True if the given index is the last in the list of tokens.
     */
    public final boolean isLastToken(int index) {
        return (index >= tokenCount - 1);
    }

    /**
     * Transform and check an array of indexes for the given list.  Any
     * negative indexes will be treated as relative to the end of the list.
     *
     * @param tokenIndex The index of the token currently being processed.
     * @param list The list the array indexes are relative to.
     * @param indexes The indexes to process.
     * @param visitor The visitor that should handle missing elements.
     * @throws InvalidPathException If the index is out-of-bounds.
     */
    public void computeListIndex(int tokenIndex, List<Object> list, int[] indexes,
            PathVisitor visitor) throws InvalidPathException {
        for (int i = 0; i < indexes.length; i++) {
            int element = indexes[i];

            if (element < 0) {
                element = list.size() + element;
            }
            if (element < 0) {
                throw new IndexNotFoundException(element, JSON_PATH_ARRAY_INDEX_IS_NEGATIVE)
                        .withPath(subpath(tokenIndex))
                        .withResolution(ENSURE_THE_INDEX_IS_WITHIN_THE_ARRAY_BOUNDS);
            }
            if (tokenIndex >= 0 && element >= list.size()) {
                visitor.handleMissingElement(this, list, element, (TraversalToken)
                        tokens.get(tokenIndex + 1));
            }
            indexes[i] = element;
        }
    }

    /**
     * Compute the indexes for the given array.
     *
     * @param list The list the array indexes are relative to.
     * @param indexes The indexes to process.
     * @param visitor The visitor that should handle missing elements.
     * @throws InvalidPathException If the index is out-of-bounds.
     */
    public void computeListIndexForLastToken(List<Object> list, int[] indexes,
            PathVisitor visitor) throws InvalidPathException {
        computeListIndex(-1, list, indexes, visitor);
    }

    /**
     * Convert an expression result into an integer list index.
     *
     * @param index The index of the currently processed token in the path.
     * @param value The number value to convert into an array index.
     * @return The integer version of the given number value.
     * @throws InvalidPathException If the given number is Infinity or NaN.
     */
    public int convertListIndex(int index, Number value) throws InvalidPathException {
        double doubleValue = value.doubleValue();

        if (Double.isInfinite(doubleValue) || Double.isNaN(doubleValue)) {
            throw new UnexpectedTypeException(Integer.class,
                    String.format(EXPECTING_INTEGER_FOR_JSON_PATH_ARRAY_INDEX_FOUND, value))
                    .withParent(subpath(index))
                    .withResolution(CHANGE_THE_EXPRESSION_TO_RETURN_AN_INTEGER);
        }
        return value.intValue();
    }

    private void redirectTraverseException(int index, Object obj, PathVisitor visitor,
            InvalidPathException e) throws InvalidPathException {
        e.withPath(subpath(index));
        if (((TraversalToken) tokens.get(index)).isBranchingToken()) {
            visitor.handleExceptionOnBranch(this, index, obj, e);
        } else {
            throw e;
        }
    }

    /**
     * Traverse the given elements of a list.  If this is the last token,
     * the visitor will be called to handle processing, otherwise, the
     * walker will follow the specified elements.  All elements of an
     * array are visited at once since the array indexes would not be
     * stable across visits, for example, because elements were deleted.
     *
     * @param index The token index.
     * @param list The list the array indexes are relative to.
     * @param elements The indexes to traverse.
     * @param visitor The visitor that handles the last element in a path.
     * @throws InvalidPathException If there was an issue traversing the elements.
     */
    public void traverseList(int index, List<Object> list, int[] elements,
            PathVisitor visitor) throws InvalidPathException {
        if (isLastToken(index)) {
            try {
                visitor.visitElement(this, list, elements);
            } catch (InvalidPathException e) {
                e.withPath(subpath(index));
                throw e;
            }
        } else {
            computeListIndex(index, list, elements, visitor);
            for (int element : elements) {
                try {
                    Object elementValue = list.get(element);

                    if (elementValue == null) {
                        visitor.handleMissingElement(this, list, element,
                                (TraversalToken) tokens.get(index + 1));
                        elementValue = list.get(element);
                    }
                    traverse(index + 1, elementValue, visitor);
                } catch (InvalidPathException e) {
                    e.getParentPath().add(0, element);
                    redirectTraverseException(index, list, visitor, e);
                }
            }
        }
    }

    /**
     * Traverse a field in a map.  If this is the last token, the visitor
     * will be called to handle processing, otherwise, the walker will
     * continue to drill down.
     *
     * @param index The token index.
     * @param map The map the field is relative to.
     * @param field The field name to traverse.
     * @param visitor The visitor that handles the last element in a path.
     * @throws InvalidPathException If there was an issue traversing the field.
     */
    public void traverseMap(int index, Map<String, Object> map, String field,
            PathVisitor visitor) throws InvalidPathException {
        if (isLastToken(index)) {
            try {
                visitor.visitField(this, map, field);
            } catch (InvalidPathException e) {
                e.withPath(subpath(index));
                throw e;
            }
        } else {
            Object child = map.get(field);

            try {
                if (child == null && !map.containsKey(field)) {
                    visitor.handleMissingField(this, map, field,
                            (TraversalToken) tokens.get(index + 1));
                    if (!map.containsKey(field)) {
                        return;
                    }
                    child = map.get(field);
                }
            } catch (InvalidPathException e) {
                redirectTraverseException(index, map, visitor, e);
            }
            try {
                traverse(index + 1, child, visitor);
            } catch (InvalidPathException e) {
                e.getParentPath().add(0, field);
                redirectTraverseException(index, map, visitor, e);
            }
        }
    }

    /**
     * Traverse an element of a JSON object or an array.
     *
     * @param index The token index.
     * @param obj The object the field is relative to.
     * @param field The field/index to traverse.
     * @param visitor The visitor that handles the last element in a path.
     * @throws InvalidPathException If there was an issue traversing the field.
     */
    public void traverseObject(int index, Object obj, Object field, PathVisitor visitor) throws
            InvalidPathException {
        if (field instanceof Number) {
            Number element = (Number) field;

            if (!(obj instanceof List)) {
                throw new UnexpectedTypeException(List.class,
                        String.format(EXPECTING_ARRAY_FOR_JSON_PATH_ARRAY_INDEX,
                                ObjectType.objectToType(obj).getName()))
                        .withPath(subpath(index))
                        .withParent(obj)
                        .withResolution(CHANGE_THE_PATH_TO_REFER_TO_AN_ARRAY);
            }

            traverseList(index, (List) obj, new int[]{convertListIndex(index, element)}, visitor);
        } else {
            if (!(obj instanceof Map)) {
                throw new UnexpectedTypeException(Map.class, String.format(EXPECTING_OBJECT,
                        field, ObjectType.objectToType(obj).getName()))
                        .withPath(subpath(index))
                        .withParent(obj)
                        .withResolution(CHANGE_THE_PATH_TO_REFER_TO_AN_OBJECT);
            }

            traverseMap(index, (Map) obj, field.toString(), visitor);
        }
    }

    public void traverseObjectField(int index, Object obj, String field, PathVisitor visitor) throws
            InvalidPathException {
        if (!(obj instanceof Map)) {
            throw new UnexpectedTypeException(Map.class, String.format(EXPECTING_OBJECT,
                    field, ObjectType.objectToType(obj).getName()))
                    .withPath(subpath(index))
                    .withParent(obj)
                    .withResolution(CHANGE_THE_PATH_TO_REFER_TO_AN_OBJECT);
        }

        traverseMap(index, (Map) obj, field, visitor);
    }

    /**
     * Traverse a JSON object or an array.
     *
     * @param index The token index.
     * @param obj The object to traverse.
     * @param visitor The visitor that handles the last element in a path.
     * @throws InvalidPathException If there was an issue traversing the field.
     */
    public void traverse(int index, Object obj, PathVisitor visitor) throws InvalidPathException {
        TraversalToken token = (TraversalToken) tokens.get(index);

        token.traverse(this, index, obj, visitor);
    }

    /**
     * Traverse a JSON object, starting at the first part of the JSON-Path.  Note, this method
     * should not be used during a write operation since you cannot write to the root value.
     *
     * @param root The object to traverse.
     * @param visitor The visitor that handles the last element in a path.
     * @throws InvalidPathException If there was an issue traversing the field.
     */
    public void traverse(Object root, PathVisitor visitor) throws InvalidPathException {
        int startingIndex = 0;

        if (simplePath != null) {
            traverseObjectField(1, root, simplePath.toString(), visitor);
            return;
        }

        if (prefix != null) {
            // There are some method calls and prefix path(s), so we need to perform
            // a read on those to get the data for the given visitor to process.
            ReadVisitor readVisitor = new ReadVisitor(visitor.getScopes(), root);
            List<Object> results;

            prefix.traverse(root, readVisitor);
            results = readVisitor.getResultSet();
            if (prefix.hasMultipleResults()) {
                root = results;
            } else {
                root = results.get(0);
            }
            // Process the results of the json-path query with the methods in our
            // part of the path.
            for (int i = 0; methodStream != null && i < methodStream.size(); i++) {
                MethodToken methodToken = (MethodToken) methodStream.get(i);
                try {
                    root = methodToken.process(this, visitor, root);
                } catch (InvalidPathException e) {
                    e.withPath(String.format(PATH_JOIN_FORMAT, prefix.fullpath(),
                            PathParser.unparse(methodStream, i)));
                    e.withParent(root);
                    throw e;
                }
            }

            if (tokens.isEmpty()) {
                // Normally, a json-path always begins with a root NameToken,
                // unless the last part of a path is a method call.  So, we
                // simulate the root token processing by just calling visitField
                // here.
                visitor.visitField(this, new RootMap(root), NameToken.ROOT_ELEMENT);
            } else {
                this.traverse(0, root, visitor);
            }
        } else {
            startingIndex += 1;
            if (startingIndex == tokenCount) {
                visitor.visitField(this, new RootMap(root), NameToken.ROOT_ELEMENT);
            } else {
                this.traverse(startingIndex, root, visitor);
            }
        }
    }

    /**
     * Traverse a JSON object, starting at the first part of the JSON-Path.
     *
     * @param root The object to traverse.
     * @param visitor The visitor that handles the last element in a path.
     * @throws InvalidPathException If there was an issue traversing the field.
     */
    public void traverseWithWritableRoot(Object root, PathVisitor visitor) throws
            InvalidPathException {
        if (simplePath != null) {
            traverseObjectField(1, root, simplePath.toString(), visitor);
            return;
        }

        RootMap rootWrapper = new RootMap(root);
        int startingIndex = 0;

        if (prefix != null) {
            // There are some method calls and prefix path(s), so we need to perform
            // a read on those to get the data for the given visitor to process.
            ReadVisitor readVisitor = new ReadVisitor(visitor.getScopes(), root);
            List<Object> results;

            prefix.traverse(root, readVisitor);
            results = readVisitor.getResultSet();
            if (prefix.hasMultipleResults()) {
                root = results;
            } else {
                root = results.get(0);
            }
            // Process the results of the json-path query with the methods in our
            // part of the path.
            for (int i = 0; i < methodStream.size(); i++) {
                MethodToken methodToken = (MethodToken) methodStream.get(i);
                try {
                    root = methodToken.process(this, visitor, root);
                } catch (InvalidPathException e) {
                    e.withPath(String.format(PATH_JOIN_FORMAT, prefix.fullpath(),
                            PathParser.unparse(methodStream, i)));
                    e.withParent(root);
                    throw e;
                }
            }

            if (tokens.isEmpty()) {
                // Normally, a json-path always begins with a root NameToken,
                // unless the last part of a path is a method call.  So, we
                // simulate the root token processing by just calling visitField
                // here.
                rootWrapper.rootValue = root;
                visitor.visitField(this, rootWrapper, NameToken.ROOT_ELEMENT);
            } else {
                this.traverse(0, root, visitor);
            }
        } else {
            this.traverse(startingIndex, rootWrapper, visitor);
            // TODO tstack - Should we return the current value of the root document in case
            // it was overwritten during the traversal instead of rewriting it?
            updateRootDocument(rootWrapper, root);
        }
    }

    /**
     * Check to see if the visitor overwrote the root document.  The
     * existing API we have doesn't allow a new root document to be
     * returned, but we should still allow the user to overwrite the
     * root doc.  So, we check if the root has been overwritten and
     * then we replace the contents of the old one with the new one.
     *
     * @param rootWrapper The wrapper around the root document.
     * @throws InvalidPathException
     */
    private void updateRootDocument(final RootMap rootWrapper, Object originalRoot) throws
            InvalidPathException {
        Object newRoot = rootWrapper.rootValue;

        if (newRoot != originalRoot) {
            // Try to replace the contents of the old root with the new one.
            // If the new root is a null value, then we just clear the old doc.
            if (originalRoot instanceof Map) {
                Map<String, Object> originalMap = (Map<String, Object>) originalRoot;

                if (newRoot != null && !(newRoot instanceof Map)) {
                    throw new UnexpectedTypeException(Map.class,
                            ROOT_OVERWRITE_TYPE_MISMATCH)
                            .withPath(NameToken.ROOT)
                            .withResolution(WRITE_THE_SAME_TYPE_OF_DATA_TO_THE_ROOT);
                }

                originalMap.clear();
                if (newRoot != null) {
                    originalMap.putAll((Map) newRoot);
                }
            } else if (originalRoot instanceof List) {
                List<Object> originalList = (List<Object>) originalRoot;

                if (newRoot != null && !(newRoot instanceof List)) {
                    throw new UnexpectedTypeException(List.class,
                            ROOT_OVERWRITE_TYPE_MISMATCH)
                            .withPath(NameToken.ROOT)
                            .withResolution(WRITE_THE_SAME_TYPE_OF_DATA_TO_THE_ROOT);
                }

                originalList.clear();
                if (newRoot != null) {
                    originalList.addAll((List) newRoot);
                }
            } else {
                throw new UnexpectedTypeException(Map.class,
                        ROOT_OVERWRITE_TYPE_MISMATCH)
                        .withPath(NameToken.ROOT)
                        .withResolution(WRITE_THE_SAME_TYPE_OF_DATA_TO_THE_ROOT);
            }
            rootWrapper.rootValue = originalRoot;
        }
    }

    /**
     * Return a subset of the JSON-path used by this walker as a string.
     *
     * @param index The index of the last token to include in the path.
     * @return The JSON-subpath.
     */
    public Object subpath(final int index) {
        return new Object() {
            @Override
            public String toString() {
                String retval = PathParser.unparse((List) tokens, index);
                if (prefix != null) {
                    retval = String.format(PATH_JOIN_FORMAT, prefix.fullpath(), retval);
                }

                return retval;
            }
        };
    }

    public String fullpath() {
        return subpath(tokens.size() - 1).toString();
    }

    public boolean isEmpty() {
        return tokens.isEmpty() && prefix == null;
    }
}
