/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
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
import org.apache.commons.codec.digest.DigestUtils;
import sl.EvaluatorUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.snaplogic.expression.classes.Messages.EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG;
import static com.snaplogic.expression.classes.Messages.PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX;

/**
 * Implements the 'Digest' object in the expression language.
 *
 * @author vsoni
 */
public enum DigestExpressionClass implements JavascriptClass, Scope {
    INSTANCE;

    private static Map<String, JavascriptFunction> METHODS = ImmutableMap.of(
            "md5", (JavascriptFunction) Md5.INSTANCE,
            "sha1", (JavascriptFunction) Sha1.INSTANCE,
            "sha256", (JavascriptFunction) Sha256.INSTANCE
    );

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   List<Object> args) {
        JavascriptFunction method = METHODS.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("Digest", METHODS.keySet(), methodName);
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
     * Implementation of the 'md5()' function.
     */
    public enum Md5 implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            String output;

            if (arg0 instanceof byte[]) {
                output = DigestUtils.md5Hex((byte[]) arg0);
            } else if (arg0 instanceof String) {
                output = DigestUtils.md5Hex((String) arg0);
            } else {
                throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            return output;
        }
    }

    /**
     * Implementation of the 'sha1()' function.
     */
    public enum Sha1 implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            String output;

            if (arg0 instanceof byte[]) {
                output = DigestUtils.sha1Hex((byte[]) arg0);
            } else if (arg0 instanceof String) {
                output = DigestUtils.sha1Hex((String) arg0);
            } else {
                throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            return output;
        }
    }

    /**
     * Implementation of the 'sha256()' function.
     */
    public enum Sha256 implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            Object arg0 = args.isEmpty() ? null : args.get(0);
            String output;

            if (arg0 instanceof byte[]) {
                output = DigestUtils.sha256Hex((byte[]) arg0);
            } else if (arg0 instanceof String) {
                output = DigestUtils.sha256Hex((String) arg0);
            } else {
                throw new SnapDataException(EXPECTING_A_STRING_OR_BIN_AS_THE_FIRST_ARG)
                        .formatWith(ObjectType.objectToType(arg0))
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            return output;
        }
    }
}
