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
import net.iharder.Base64;
import sl.EvaluatorUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.snaplogic.expression.classes.Messages.*;

/**
 * Implements the 'Base64' object in the expression language.
 *
 * @author tstack
 */
public enum Base64ExpressionClass implements JavascriptClass, Scope {
    INSTANCE;

    private static Map<String, JavascriptFunction> METHODS =
            ImmutableMap.<String, JavascriptFunction>builder()
            .put("encode", Encode.INSTANCE)
            .put("encodeAsBinary", EncodeAsBinary.INSTANCE)
            .put("decode", Decode.INSTANCE)
            .put("decodeGZip", DecodeGZip.INSTANCE)
            .put("decodeAsBinary", DecodeAsBinary.INSTANCE)
            .put("decodeGZipAsBinary", DecodeGZipAsBinary.INSTANCE).build();

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   List<Object> args) {
        JavascriptFunction method = METHODS.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("Base64", METHODS.keySet(), methodName);
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
     * Implementation of the 'encode()' function.
     */
    public enum Encode implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            byte[] bits;

            if (arg0 instanceof byte[]) {
                bits = (byte[]) arg0;
            } else if (arg0 instanceof String) {
                bits = ((String) arg0).getBytes(StandardCharsets.UTF_8);
            } else {
                throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            return Base64.encodeBytes(bits);
        }
    }

    /**
     * Implementation of the 'encodeAsBinary()' function.
     */
    public enum EncodeAsBinary implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            byte[] bits;

            if (arg0 instanceof byte[]) {
                bits = (byte[]) arg0;
            } else if (arg0 instanceof String) {
                bits = ((String) arg0).getBytes(StandardCharsets.UTF_8);
            } else {
                throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            return Base64.encodeBytesToBytes(bits);
        }
    }

    /**
     * Implementation of the 'decode()' function.
     */
    public enum Decode implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            try {
                return new String(((byte[]) DecodeAsBinary.INSTANCE.eval(args)), StandardCharsets
                        .UTF_8);
            } catch (RuntimeException e) {
                throw new SnapDataException(e, UNABLE_TO_DECODE_BASE64_VALUE)
                        .withResolution(MAKE_SURE_THE_ARGUMENT_IS_VALID_BASE64);
            }
        }
    }

    /**
     * Implementation of the 'decodeGZip()' function.
     */
    public enum DecodeGZip implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            try {
                return new String(((byte[]) DecodeGZipAsBinary.INSTANCE.eval(args)),
                        StandardCharsets.UTF_8);
            } catch (RuntimeException e) {
                throw new SnapDataException(e, UNABLE_TO_DECODE_BASE64_VALUE)
                        .withResolution(MAKE_SURE_THE_ARGUMENT_IS_VALID_BASE64);
            }
        }
    }

    /**
     * Implementation of the 'decodeAsBinary()' function.
     */
    public enum DecodeAsBinary implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            if (args.isEmpty()) {
                throw new SnapDataException(EXPECTING_A_STRING_AS_THE_FIRST_ARG_FOUND)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            String arg = ObjectType.toString(arg0);
            try {
                return Base64.decode(arg, Base64.DONT_GUNZIP);
            } catch (IOException e) {
                throw new SnapDataException(e, UNABLE_TO_DECODE_BASE64_VALUE)
                        .withResolution(MAKE_SURE_THE_ARGUMENT_IS_VALID_BASE64);
            }
        }
    }

    /**
     * Implementation of the 'decodeGZipAsBinary()' function.
     */
    public enum DecodeGZipAsBinary implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            if (args.isEmpty()) {
                throw new SnapDataException(EXPECTING_A_STRING_AS_THE_FIRST_ARG_FOUND)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            String arg = ObjectType.toString(arg0);
            try {
                return Base64.decode(arg, Base64.NO_OPTIONS);
            } catch (IOException e) {
                throw new SnapDataException(e, UNABLE_TO_DECODE_BASE64_VALUE)
                        .withResolution(MAKE_SURE_THE_ARGUMENT_IS_VALID_BASE64);
            }
        }
    }
}
