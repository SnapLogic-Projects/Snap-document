/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.classes;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.snap.api.SnapDataException;
import org.apache.commons.io.IOUtils;
import sl.EvaluatorUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.snaplogic.expression.classes.Messages.*;

/**
 * Implements the 'GZip' object in the expression language.
 *
 * @author ksubramanian
 */
public enum GZipExpressionClass implements JavascriptClass, Scope {
    INSTANCE;

    private static final String NONE = "none";
    private static Map<String, JavascriptFunction> METHODS = ImmutableMap.of(
            "compress", (JavascriptFunction) Compress.INSTANCE,
            "decompress", (JavascriptFunction) Decompress.INSTANCE
    );

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   List<Object> args) {
        JavascriptFunction method = METHODS.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("GZip", METHODS.keySet(), methodName);
        }
        return method.eval(args);
    }

    @Override
    public boolean evalInstanceOf(final Object value) {
        return false;
    }

    @Override
    public Object get(String name) {
        Object retval = METHODS.get(name);
        if (retval == null) {
            retval = UNDEFINED;
        }
        return retval;
    }

    @Override
    public Set<String> keySet() {
        return METHODS.keySet();
    }

    /**
     * Implementation of the 'compress()' function.
     */
    public enum Compress implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            if (args.size() == 1) {
                Object arg0 = args.get(0);
                InputStream inputStream;
                if (arg0 instanceof String) {
                    inputStream = new ByteArrayInputStream(((String) arg0).getBytes(
                            StandardCharsets.UTF_8));
                } else if (arg0 instanceof byte[]) {
                    inputStream = new ByteArrayInputStream((byte[]) arg0);
                } else {
                    throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                            .formatWith(ObjectType.objectToType(arg0))
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
                    IOUtils.copy(inputStream, gzipOutputStream);
                } catch (IOException e) {
                    throw new SnapDataException(e, UNABLE_TO_CREATE_GZIP_STREAM)
                            .formatWith(ObjectType.objectToType(arg0))
                            .withResolution(PLEASE_CHECK_YOUR_INPUT_DATA);
                }
                return outputStream.toByteArray();
            } else {
                throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                        .formatWith(ObjectType.objectToType(NONE))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
        }
    }

    /**
     * Implementation of the 'decompress()' function.
     */
    public enum Decompress implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            InputStream inputStream;
            if (arg0 instanceof byte[]) {
                inputStream = new ByteArrayInputStream((byte[]) arg0);
            } else if (arg0 instanceof String) {
                inputStream = new ByteArrayInputStream(((String) arg0).getBytes());
            } else {
                throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
                IOUtils.copy(gzipInputStream, byteArrayOutputStream);
            } catch (IOException e) {
                throw new SnapDataException(e, UNABLE_TO_CREATE_GZIP_STREAM)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_INPUT_DATA);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
}
