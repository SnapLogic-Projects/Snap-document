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

import com.snaplogic.api.SnapException;
import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.common.jsonpath.WalkResult;
import com.snaplogic.expression.JsonPathExpressionService;
import com.snaplogic.jsonpath.tokens.NameToken;
import com.snaplogic.jsonpath.tokens.PathToken;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.snaplogic.jsonpath.Messages.READ_STATIC_PATH_FAILED;
import static com.snaplogic.jsonpath.Messages.STATICALLY_DEFINED_JSON_PATH_IS_INVALID;
import static com.snaplogic.jsonpath.Messages.UNABLE_TO_WRITE_DATA_TO_A_STATICALLY_DEFINED_PATH;

/**
 * A convenience class for performing operations using a JSON-Path.
 *
 * This class will take care of parsing the path with PathParser and
 * building a PathWalker that can traverse an object tree.  The
 * read/write/insert/delete methods will take care of creating the
 * appropriate PathVisitor and calling the walker with the given
 * object tree.
 *
 * @author tstack
 */
public class JsonPathImpl implements JsonPath {
    private static final PathExpressionService EMPTY_EXPR_HANDLER = new JsonPathExpressionService();
    private static final Logger LOG = LoggerFactory.getLogger(JsonPathImpl.class);

    /**
     * Compile a JSON-Path that can then be used to read/modify/delete the
     * elements specified by the path.
     *
     * @param path The JSON-Path string.
     * @param expressionHandler The service used to compile/evaluate expressions in the path.
     * @return A JsonPath object that can be used to manipulate data.
     * @throws InvalidPathException If there was an issue while compiling the path.
     */
    public static JsonPath compile(String path, PathExpressionService expressionHandler) throws
            InvalidPathException {
        return new JsonPathImpl(path, expressionHandler);
    }

    /**
     * Compile a JSON-Path that can then be used to read/modify/delete the
     * elements specified by the path.
     *
     * @param path The JSON-Path string.
     * @return A JsonPath object that can be used to manipulate data.
     * @throws InvalidPathException If there was an issue while compiling the path.
     */
    public static JsonPath compile(String path) throws InvalidPathException {
        return compile(path, EMPTY_EXPR_HANDLER);
    }

    /**
     * Compile a statically defined JSON-Path that can then be used to
     * read/modify/delete the elements specified by the path.  This method
     * can be used when constructing JsonPath objects from static paths that
     * are expected to be valid.
     *
     * @param path The JSON-Path string.
     * @return A JsonPath object that can be used to manipulate data.
     */
    public static JsonPath compileStatic(String path) {
        try {
            return compile(path);
        } catch (InvalidPathException e) {
            throw new RuntimeException(
                    String.format(STATICALLY_DEFINED_JSON_PATH_IS_INVALID, path), e);
        }
    }

    /**
     * Compile a list of field names and indexes into a path.
     *
     * @param path The path to compile.
     * @return A JsonPath that encodes the given path.
     */
    public static JsonPath compile(List<Object> path) {
        return new JsonPathImpl(new PathWalker(PathParser.tokenize(path, EMPTY_EXPR_HANDLER),
                EMPTY_EXPR_HANDLER));
    }

    private String originalPath = null;
    private Object scopes;
    private final PathWalker walker;
    private final int tokenHash;

    private JsonPathImpl(PathWalker walker) {
        this.walker = walker;
        tokenHash = this.walker.getTokens().hashCode();
    }

    private JsonPathImpl(String path, PathExpressionService expressionHandler) throws
            InvalidPathException {
        originalPath = path;
        walker = new PathWalker(PathParser.parse(path, expressionHandler), expressionHandler);
        tokenHash = this.walker.getTokens().hashCode();
    }

    @Override
    public JsonPath withScopes(final Object scopes) {
        JsonPathImpl retval = new JsonPathImpl(walker);
        retval.scopes = scopes;
        return retval;
    }

    @Override
    public <T> T read(Object data, Object defaultValue) throws InvalidPathException {
        ReadVisitor visitor = new ReadVisitor(scopes, data);
        List<Object> results;

        try {
            walker.traverse(data, visitor);
        } catch (PathNotFoundException e) {
            if (defaultValue == NO_DEFAULT) {
                throw e.fillInStackTraceForCaller();
            }

            return (T) defaultValue;
        }

        results = visitor.getResultSet();

        if (walker.hasMultipleResults()) {
            return (T) results;
        }

        assert results.size() == 1;

        return (T) results.get(0);
    }

    @Override
    public <T> T read(final Object data) throws InvalidPathException {
        return read(data, NO_DEFAULT);
    }

    @Override
    public <T> T readStatic(final Object data) {
        try {
            return read(data, NO_DEFAULT);
        } catch (InvalidPathException e) {
            throw new SnapException(e, READ_STATIC_PATH_FAILED);
        }
    }

    @Override
    public <T> T readStatic(final Object data, Object defaultValue) {
        try {
            return read(data, defaultValue);
        } catch (InvalidPathException e) {
            throw new SnapException(e, READ_STATIC_PATH_FAILED);
        }
    }

    @Override
    public boolean existsIn(final Object data) {
        ReadVisitor visitor = new ReadVisitor(scopes, data);

        try {
            walker.traverse(data, visitor);
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }

    @Override
    public void write(Object data, Object value) throws InvalidPathException {
        try {
            NameToken simplePath = walker.getSimplePath();

            if (simplePath != null) {
                // Optimistic flow where the path is just a single field -- $foo
                ((Map<String, Object>) data).put(simplePath.toString(), value);
                return;
            }
        } catch (ClassCastException e) {
            // ignore
        }

        WriteVisitor visitor = new WriteVisitor(scopes, data, false, value);

        try {
            walker.traverseWithWritableRoot(data, visitor);
        } catch (PathNotFoundException e) {
            throw e.fillInStackTraceForCaller();
        }
    }

    @Override
    public void writeStatic(final Object data, final Object value) {
        try {
            write(data, value);
        } catch (InvalidPathException e) {
            throw new SnapException(e, UNABLE_TO_WRITE_DATA_TO_A_STATICALLY_DEFINED_PATH);
        }
    }

    @Override
    public void insert(Object data, Object value) throws InvalidPathException {
        WriteVisitor visitor = new WriteVisitor(scopes, data, true, value);

        try {
            walker.traverseWithWritableRoot(data, visitor);
        } catch (PathNotFoundException e) {
            throw e.fillInStackTraceForCaller();
        }
    }

    @Override
    public void delete(Object data) throws InvalidPathException {
        try {
            walker.traverse(data, new DeleteVisitor(scopes, data));
        } catch (PathNotFoundException e) {
            throw e.fillInStackTraceForCaller();
        }
    }

    @Override
    public List<WalkResult> walk(final Object data) throws InvalidPathException {
        if (this.isRoot()) {
            // If this JsonPath refers to the root, just return a canned result set.  We care about
            // this case since the Mapper calls walk for the Mapping Root on every document and it's
            // pretty costly to do that for every document..
            return new RootWalkResult(data);
        }

        TracingPathWalker tracingPathWalker = new TracingPathWalker(walker);
        WalkVisitor walkVisitor = new WalkVisitor(scopes, data);

        try {
            tracingPathWalker.traverseWithWritableRoot(data, walkVisitor);
        } catch (PathNotFoundException e) {
            throw e.fillInStackTraceForCaller();
        }

        List retval = walkVisitor.getResultSet();

        return (List<WalkResult>) retval;
    }

    @Override
    public JsonPath join(JsonPath child) {
        JsonPathImpl childImpl = (JsonPathImpl) child;

        if (childImpl.walker.isEmpty()) {
            return this;
        }

        List<PathToken> tokens = new ArrayList<>();
        walker.flattenTokensTo(tokens);
        int childRootIndex = tokens.size();

        childImpl.walker.flattenTokensTo(tokens);
        // Remove the root document token that was at the front of the child path.
        if (NameToken.ROOT.equals(tokens.get(childRootIndex))) {
            tokens.remove(childRootIndex);
        }

        return new JsonPathImpl(new PathWalker(tokens, walker.getExpressionHandler()));
    }

    @Override
    public JsonPath children(final Object... names) {
        List<PathToken> childTokens = PathParser.tokenize(Arrays.asList(names), walker
                .getExpressionHandler());
        // Remove the '$' token and then
        childTokens.remove(0);
        // ... append the remaining to the original path.
        childTokens.addAll(0, walker.getTokens());

        return new JsonPathImpl(new PathWalker(childTokens, walker.getExpressionHandler()));
    }

    @Override
    public boolean hasMultipleResults() {
        return walker.hasMultipleResults();
    }

    @Override
    public JsonPath parent() {
        List<PathToken> thisTokens = walker.getTokens();
        int size = thisTokens.size();

        if (size == 1) {
            return this;
        } else {
            List<PathToken> tokens = new ArrayList<>(thisTokens);

            tokens.remove(size - 1);
            return new JsonPathImpl(new PathWalker(tokens, walker.getExpressionHandler()));
        }
    }

    @Override
    public JsonPath basename() {
        List<PathToken> thisTokens = walker.getTokens();
        int size = thisTokens.size();

        if (size == 1) {
            return this;
        } else {
            List<PathToken> tokens = Arrays.asList(NameToken.ROOT, thisTokens.get(size - 1));

            return new JsonPathImpl(new PathWalker(tokens, walker.getExpressionHandler()));
        }
    }

    @Override
    public JsonPath subPath(int startIndex, int endIndex) {
        List<PathToken> thisTokens = new ArrayList<>();
        walker.flattenTokensTo(thisTokens);

        if (startIndex < 0) {
            startIndex = thisTokens.size() + startIndex;
        }
        if (startIndex < 0 || startIndex >= thisTokens.size()) {
            throw new IndexOutOfBoundsException(String.format("Index (%s) is not in the range " +
                    "0..%s", startIndex, thisTokens.size()));
        }
        if (endIndex < 0) {
            endIndex = thisTokens.size();
        }
        if (endIndex < startIndex || endIndex > thisTokens.size()) {
            throw new IndexOutOfBoundsException(String.format("Index (%s) is not in the range " +
                    "%s..%s", endIndex, startIndex, thisTokens.size()));
        }

        List<PathToken> sub = new ArrayList<>(thisTokens.subList(startIndex, endIndex));
        if (sub.isEmpty()) {
            return JsonPaths.root();
        } else if (!sub.get(0).equals(NameToken.ROOT)) {
            sub.add(0, NameToken.ROOT);
        }
        return new JsonPathImpl(new PathWalker(sub, walker.getExpressionHandler()));
    }

    @Override
    public List<Object> resolvedPath(Object data) throws InvalidPathException {
        List<Object> retval = new ArrayList<>();

        for (PathToken token : walker.getTokens()) {
            if (NameToken.ROOT.equals(token)) {
                continue;
            }
            retval.add(resolveTokenWithData(token, data));
        }

        return retval;
    }

    @Override
    public boolean startsWith(final JsonPath head) {
        JsonPathImpl headImpl = ((JsonPathImpl) head);
        List<PathToken> thisTokens = walker.getTokens();
        List<PathToken> headTokens = headImpl.walker.getTokens();
        int headSize = headTokens.size() - 1;

        if (headSize >= thisTokens.size()) {
            return false;
        }

        int thisIndex = 0;
        for (PathToken token : headTokens) {
            try {
                Object thisToken = resolveTokenWithData(thisTokens.get(thisIndex), null);
                Object tailToken = resolveTokenWithData(token, null);
                if (!thisToken.equals(tailToken)) {
                    return false;
                }
                thisIndex += 1;
            } catch (InvalidPathException e) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean endsWith(final JsonPath tail) {
        JsonPathImpl tailImpl = ((JsonPathImpl) tail);
        List<PathToken> thisTokens = walker.getTokens();
        List<PathToken> tailTokens = tailImpl.walker.getTokens();
        int tailSize = tailTokens.size() - 1;

        if (tailSize >= thisTokens.size()) {
            return false;
        }

        int thisIndex = thisTokens.size() - tailSize;
        boolean first = true;
        for (PathToken token : tailTokens) {
            if (first) {
                // The first token is always '$', so we skip that.
                first = false;
                continue;
            }

            try {
                Object thisToken = resolveTokenWithData(thisTokens.get(thisIndex), null);
                Object tailToken = resolveTokenWithData(token, null);
                if (!thisToken.equals(tailToken)) {
                    return false;
                }
            } catch (InvalidPathException e) {
                return false;
            }
            thisIndex += 1;
        }

        return true;
    }

    @Override
    public boolean isRoot() {
        return walker.isRoot();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonPathImpl)) {
            return false;
        }

        final JsonPathImpl jsonPath = (JsonPathImpl) o;

        if (tokenHash != jsonPath.tokenHash) {
            return false;
        }

        return walker.getTokens().equals(jsonPath.walker.getTokens());
    }

    @Override
    public int hashCode() {
        return tokenHash;
    }

    @Override
    public synchronized String toString() {
        if (originalPath == null) {
            List<PathToken> tokens = new ArrayList<>();
            walker.flattenTokensTo(tokens);
            originalPath = PathParser.unparse(tokens);
        }
        return originalPath;
    }

    private Object resolveTokenWithData(PathToken token, Object data) throws InvalidPathException {
        Object expressionOrValue = token.resolve();
        Object retval;
        if (expressionOrValue instanceof CompiledExpressionHolder) {
            ReadVisitor readVisitor = new ReadVisitor(scopes, data);
            retval = walker.evaluate(((CompiledExpressionHolder) expressionOrValue),
                    readVisitor, null, null);
        } else {
            retval = expressionOrValue;
        }
        return retval;
    }

    private class RootWalkResult extends AbstractList<WalkResult> implements WalkResult {
        private final Object data;

        public RootWalkResult(final Object data) {
            this.data = data;
        }

        @Override
        public List<Pair<Object, Object>> getParentPath() {
            RootMap wrapper = new RootMap(data);
            List<Pair<Object, Object>> retval = Arrays.asList(Pair.<Object, Object>of(
                    wrapper, NameToken.ROOT_ELEMENT));

            return retval;
        }

        @Override
        public JsonPath getActualPath() {
            return JsonPathImpl.this;
        }

        @Override
        public Map<Object, Object> getParentHierarchy() {
            return Collections.emptyMap();
        }

        @Override
        public <T> T getValue() {
            return (T) data;
        }

        @Override
        public <T> T setValue(final Object value) {
            return (T) value;
        }

        @Override
        public WalkResult get(final int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            return this;
        }

        @Override
        public int size() {
            return 1;
        }
    }
}
