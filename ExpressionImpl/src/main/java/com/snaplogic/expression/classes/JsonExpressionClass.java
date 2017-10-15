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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.ImmutableMap;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.snap.api.SnapDataException;
import com.snaplogic.util.SnapTypeModule;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.snaplogic.expression.classes.Messages.*;

/**
 * Implements the 'JSON' object in the expression language.
 *
 * @author tstack
 */
public enum JsonExpressionClass implements JavascriptClass, Scope {
    INSTANCE;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper() {
        {
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            registerModule(new JodaModule());
            registerModule(new SnapTypeModule());
            configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
            configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
            configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
            configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
            configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        }
    };

    private static Map<String, JavascriptFunction> METHODS = ImmutableMap.of(
            "parse", (JavascriptFunction) Parse.INSTANCE,
            "stringify", (JavascriptFunction) Stringify.INSTANCE
    );

    @Override
    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   List<Object> args) {
        JavascriptFunction method = METHODS.get(methodName);
        if (method == null) {
            throw new ExecutionException(NO_SUCH_METHOD)
                    .formatWith(methodName)
                    .withReason(THE_METHOD_WAS_NOT_FOUND_IN_THE_JSON_OBJECT)
                    .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
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
     * Implementation of the 'parse()' function.
     */
    public enum Parse implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            if (args.isEmpty() || !(args.get(0) instanceof String)) {
                throw new SnapDataException(EXPECTING_A_STRING_ARGUMENT)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            String jsonData = (String) args.get(0);

            try {
                return OBJECT_MAPPER.readValue(jsonData, Object.class);
            } catch (IOException e) {
                throw new SnapDataException(e, UNABLE_TO_PARSE_JSON_VALUE)
                        .withReason(e.getMessage())
                        .withResolution(PLEASE_CHECK_THE_FORMAT_OF_THE_JSON_VALUE);
            }
        }
    }

    /**
     * Implementation of the 'stringify()' function.
     */
    public enum Stringify implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            if (args.isEmpty()) {
                throw new SnapDataException(EXPECTING_A_VALUE_TO_ENCODE)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }

            try {
                return OBJECT_MAPPER.writeValueAsString(args.get(0));
            } catch (IOException e) {
                throw new SnapDataException(e, UNABLE_TO_STRINGIFY_ARGUMENT)
                        .withReason(e.getMessage())
                        .withResolution(INVALID_STRINGIFY_ARG_RESOLUTION);
            }
        }
    }
}
