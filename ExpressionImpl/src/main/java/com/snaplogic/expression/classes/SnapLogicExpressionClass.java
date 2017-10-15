/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2014, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression.classes;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.Document;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.DataValueHandler;
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.*;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.expression.methods.MethodUtils;
import com.snaplogic.snap.api.SnapDataException;
import com.snaplogic.util.DefaultValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static com.snaplogic.expression.classes.Messages.*;

/**
 * Container for SnapLogic-specific functions needed in the expression language.
 *
 * @author tstack
 */
public enum SnapLogicExpressionClass implements JavascriptClass, Scope {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(SnapLogicExpressionClass.class);

    private static Map<String, JavascriptFunction> METHODS = ImmutableMap.of(
            "zip", (JavascriptFunction) Zip.INSTANCE,
            "range", (JavascriptFunction) Range.INSTANCE,
            "gethostbyname", (JavascriptFunction) GetHostByName.INSTANCE,
            "ensureArray", (JavascriptFunction) EnsureArray.INSTANCE
    );

    @Override
    public Object evalStaticMethod(ScopeStack scopes,
            String methodName, final List<Object> args) {
        if ("templatize".equals(methodName)) {
            return Templatize.INSTANCE.eval(scopes, args);
        } else {
            JavascriptFunction method = METHODS.get(methodName);
            if (method == null) {
                throw new ExecutionException(NO_SUCH_METHOD)
                        .formatWith(methodName)
                        .withReason(THE_METHOD_WAS_NOT_FOUND_IN_THE_SL_OBJECT)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            return method.eval(args);
        }
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
     * Implementation of the sl.templatize() function that turns an expression with
     * variable references into a string with those references embedded in the output
     * string so they can be interpolated later.
     */
    public enum Templatize {
        INSTANCE;

        static final String ARGUMENTS = "arguments";

        public Object eval(ScopeStack scopes, final List<Object> args) {
            if (args.isEmpty()) {
                throw new ExecutionException(EXPECTING_AN_EXPRESSION_AND_ARGS)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            try {
                SnapLogicExpression expr = ExpressionUtil.compile(args.get(0).toString());
                DataValueHandler<Object> handler = new DefaultValueHandler() {
                    @Override
                    public Object handleUndefinedReference(final Document
                            originalDocument, final String fieldName,
                            final String fieldPath) throws SnapDataException {
                        return new PathReference(fieldPath);
                    }
                };

                Map<String, Object> argMap = new HashMap<>(1);
                argMap.put(ARGUMENTS, args.subList(1, args.size()));
                ScopeStack scopeStack = new ScopeStack();
                for (int i = 0; i < scopes.size(); i++) {
                    Scope scope = scopes.get(i);
                    if (scope instanceof DocumentScope) {
                        continue;
                    }
                    scopeStack.push(scope);
                }
                scopeStack.push(new BasicScope(argMap));
                return expr.evaluate(null, scopeStack, handler);
            } catch (SnapDataException e) {
                throw e;
            } catch (ExecutionException e) {
                throw new SnapDataException(e, e.getMessage())
                        .withReason(e.getReason())
                        .withResolution(e.getResolution());
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable th) {
                throw new ExecutionException(th, "Unexpected error while evaluating expression: " +
                        "%s")
                        .formatWith(args)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
        }
    }

    /**
     * Implementation of the 'zip()' function that behaves like the python version.
     */
    public enum Zip implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            for (Object arg : args) {
                if (!(arg instanceof List)) {
                    throw new SnapDataException(ZIP_ARGUMENT_WAS_NOT_AN_ARRAY_FOUND)
                            .formatWith(ObjectType.objectToType(arg))
                            .withReason(ARGS_TO_THE_ZIP_FUNCTION_MUST_BE_ARRAYS)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
                }
            }

            List<List<Object>> retval = new ArrayList<>();
            for (int index = 0; ; index += 1) {
                List<Object> tuple = new ArrayList<>();
                for (Object arg : args) {
                    List argList = (List) arg;
                    if (index >= argList.size()) {
                        return retval;
                    }

                    tuple.add(argList.get(index));
                }
                if (tuple.isEmpty()) {
                    return retval;
                }
                retval.add(tuple);
            }
        }
    }

    /**
     * Implementation of the 'range()' function that behaves like the python version.
     */
    public enum Range implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            List<BigDecimal> retval = new ArrayList<>();
            BigDecimal start = BigDecimal.ZERO, stop, step = BigDecimal.ONE;

            switch (args.size()) {
                case 0:
                    throw new SnapDataException(NOT_ENOUGH_ARGUMENTS_TO_RANGE_FUNCTION)
                            .withReason(RANGE_REQUIRES_AT_LEAST_ONE_ARGUMENT)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
                case 1:
                    stop = MethodUtils.getArgAsBigDecimal(args, 0);
                    break;
                case 2:
                    start = MethodUtils.getArgAsBigDecimal(args, 0);
                    stop = MethodUtils.getArgAsBigDecimal(args, 1);
                    break;
                case 3:
                    start = MethodUtils.getArgAsBigDecimal(args, 0);
                    stop = MethodUtils.getArgAsBigDecimal(args, 1);
                    step = MethodUtils.getArgAsBigDecimal(args, 2);
                    break;
                default:
                    throw new SnapDataException(TOO_MANY_ARGUMENTS_TO_RANGE)
                            .withReason(THE_RANGE_FUNCTION_ONLY_ACCEPTS_THREE_ARGS)
                            .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            if (step.equals(BigDecimal.ZERO)) {
                throw new SnapDataException(THE_STEP_ARGUMENT_IS_ZERO)
                        .withReason(THE_STEP_ARGUMENT_MUST_BE_A_NON_ZERO_INT)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            boolean isReverse = step.compareTo(BigDecimal.ZERO) < 0;
            while (isReverse ? stop.compareTo(start) < 0 : start.compareTo(stop) < 0) {
                retval.add(start);
                start = start.add(step);
            }
            return retval;
        }
    }

    /**
     * Implementation of the 'gethostbyname()' function.
     */
    public enum GetHostByName implements JavascriptFunction {
        INSTANCE;

        private static final String ADDRESS = "address";
        private static final String HOSTNAME = "hostname";
        private static final String UNKNOWN = "unknown";
        private static final String IPV4 = "ipv4";
        private static final String IPV6 = "ipv6";
        private static final String FAMILY = "family";

        @Override
        public Object eval(List<Object> args) {
            if (args.size() != 1) {
                throw new SnapDataException(INVALID_NUMBER_OF_ARGUMENTS)
                        .withReason(THE_GETHOSTBYNAME_FUNCTION_ACCEPTS_A_SINGLE_ARGUMENT)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            String hostname = args.get(0).toString();
            List<Map<String, Object>> retval = new ArrayList<>();

            try {
                InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
                for (InetAddress inetAddress : inetAddresses) {
                    Map<String, Object> addrMap = new HashMap<>();
                    addrMap.put(ADDRESS, inetAddress.getHostAddress());
                    addrMap.put(HOSTNAME, inetAddress.getHostName());
                    String family = UNKNOWN;
                    if (inetAddress instanceof Inet4Address) {
                        family = IPV4;
                    } else if (inetAddress instanceof Inet6Address) {
                        family = IPV6;
                    }
                    addrMap.put(FAMILY, family);
                    retval.add(addrMap);
                }
            } catch (UnknownHostException e) {
                LOG.error(LOG_UNABLE_TO_GET_ADDRESS_FOR_HOST, hostname, e);
            }
            return retval;
        }
    }

    /**
     * Implementation of the 'ensureArray()' function.
     */
    public enum EnsureArray implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(List<Object> args) {
            if (args.size() != 1) {
                throw new SnapDataException(INVALID_NUMBER_OF_ARGUMENTS)
                        .withReason(THE_ENSURE_ARRAY_FUNCTION_ACCEPTS_A_SINGLE_ARGUMENT)
                        .withResolution(PLEASE_CHECK_YOUR_EXPRESSION_SYNTAX);
            }
            Object arrayOrObject = args.get(0);
            List retval;
            if (arrayOrObject instanceof List) {
                retval = (List) arrayOrObject;
            } else {
                retval = new ArrayList();
                retval.add(arrayOrObject);
            }
            return retval;
        }
    }
}
