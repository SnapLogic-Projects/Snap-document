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

package com.snaplogic.expression.methods.array;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.JavascriptFunction;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.MethodUtils;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.object.HasOwnProperty;
import com.snaplogic.expression.methods.object.ObjectMethod;
import com.snaplogic.snap.api.SnapDataException;

import java.math.BigInteger;
import java.util.*;

/**
 * Wrapper class for list methods in expression language.
 *
 * @author jinloes
 */
public class ListMethod extends ObjectMethod {
    private static final String METHOD_SEPARATOR = ",";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, Method> LIST_METHODS =
            ImmutableMap.<String, Method>builder().put("pop", Pop.INSTANCE)
                    .put("push", Push.INSTANCE)
                    .put("reverse", Reverse.INSTANCE)
                    .put("shift", Shift.INSTANCE)
                    .put("splice", Splice.INSTANCE)
                    .put("unshift", Unshift.INSTANCE)
                    .put("concat", Concat.INSTANCE)
                    .put("join", Join.INSTANCE)
                    .put("slice", Slice.INSTANCE)
                    .put("toString", ToString.INSTANCE)
                    .put("indexOf", IndexOf.INSTANCE)
                    .put("lastIndexOf", LastIndexOf.INSTANCE)
                    .put("filter", Filter.INSTANCE)
                    .put("find", Find.INSTANCE)
                    .put("findIndex", FindIndex.INSTANCE)
                    .put("map", MapMethod.INSTANCE)
                    .put("reduce", Reduce.INSTANCE)
                    .put("reduceRight", ReduceRight.INSTANCE)
                    .put("sort", Sort.INSTANCE)
                    // XXX We can't add all of the object methods because a
                    // toString() is defined in there as well and it conflicts
                    // with this one.
                    .put("hasOwnProperty", HasOwnProperty.INSTANCE)
                    .build();


    public static Method getMethod(String methodName) {
        Method method = LIST_METHODS.get(methodName);
        if (method == null) {
            throw new UnknownMethodException(LIST_METHODS.keySet());
        }
        return method;
    }

    /**
     * Implements the Array.filter() method.
     */
    public enum Filter implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            List<Object> list = (List<Object>) member;
            List<Object> retval = new ArrayList<>();
            JavascriptFunction func = MethodUtils.getFunction(args, 0);

            for (int index = 0; index < list.size(); index++) {
                Object elem = list.get(index);

                boolean result = ObjectType.toBoolean(func.eval(Arrays.asList(elem, BigInteger
                                .valueOf(index), list)));
                if (result) {
                    retval.add(elem);
                }
            }

            return retval;
        }
    }

    /**
     * Implements the Array.find() method.
     */
    public enum Find implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            List<Object> list = (List<Object>) member;
            JavascriptFunction func = MethodUtils.getFunction(args, 0);

            for (int index = 0; index < list.size(); index++) {
                Object elem = list.get(index);

                boolean result = ObjectType.toBoolean(func.eval(Arrays.asList(elem, BigInteger
                                .valueOf(index), list)));

                if (result) {
                    return elem;
                }
            }

            return null;
        }
    }

    /**
     * Implements the Array.findIndex() method.
     */
    public enum FindIndex implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            List<Object> list = (List<Object>) member;
            JavascriptFunction func = MethodUtils.getFunction(args, 0);

            for (int index = 0; index < list.size(); index++) {
                Object elem = list.get(index);

                boolean result = ObjectType.toBoolean(func.eval(Arrays.asList(elem, BigInteger
                        .valueOf(index), list)));

                if (result) {
                    return BigInteger.valueOf(index);
                }
            }

            return BigInteger.valueOf(-1);
        }
    }

    /**
     * Implements the Array.map() method.
     */
    public enum MapMethod implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            List<Object> list = (List<Object>) member;
            List<Object> retval = new ArrayList<>(list.size());
            JavascriptFunction func = MethodUtils.getFunction(args, 0);

            for (int index = 0; index < list.size(); index++) {
                retval.add(index, func.eval(Arrays.asList(list.get(index), BigInteger
                                .valueOf(index), list)));
            }

            return retval;
        }
    }

    /**
     * Implements the Array.reduce() method.
     */
    public enum Reduce implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            JavascriptFunction func = MethodUtils.getFunction(args, 0);
            List<Object> list = (List<Object>) member;
            int index = 0;
            Object retval;

            if (args.size() > 1) {
                retval = args.get(1);
            } else {
                if (list.isEmpty()) {
                    throw new SnapDataException("Invalid reduce invocation")
                            .withReason("Reduce on empty list with no initial value")
                            .withResolution("Pass an initial value or ensure the array is not " +
                                    "empty");
                }
                retval = list.get(index);
                index += 1;
            }

            for (; index < list.size(); index++) {
                Object elem = list.get(index);

                retval = func.eval(Arrays.asList(retval, elem, BigInteger.valueOf(index), list));
            }

            return retval;
        }
    }

    /**
     * Implements the Array.reduceRight() method.
     */
    public enum ReduceRight implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            JavascriptFunction func = MethodUtils.getFunction(args, 0);
            List<Object> list = (List<Object>) member;
            int index = list.size() - 1;
            Object retval;

            if (args.size() > 1) {
                retval = args.get(1);
            } else {
                if (list.isEmpty()) {
                    throw new SnapDataException("Invalid reduceRight invocation")
                            .withReason("Reduce on empty list with no initial value")
                            .withResolution("Pass an initial value or ensure the array is not " +
                                    "empty");
                }
                retval = list.get(index);
                index -= 1;
            }

            for (; index >= 0; index--) {
                Object elem = list.get(index);

                retval = func.eval(Arrays.asList(retval, elem, BigInteger.valueOf(index), list));
            }

            return retval;
        }
    }

    /**
     * Implements the Array.sort() method.
     */
    public enum Sort implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            Comparator<Object> comparator;

            if (args.isEmpty()) {
                comparator = new Comparator<Object>() {
                    @Override
                    public int compare(final Object o1, final Object o2) {
                        return ObjectType.toString(o1).compareTo(ObjectType.toString(o2));
                    }
                };
            } else {
                final JavascriptFunction func = MethodUtils.getFunction(args, 0);
                comparator = new Comparator<Object>() {
                    @Override
                    public int compare(final Object o1, final Object o2) {
                        return ObjectType.toNumber(func.eval(Arrays.asList(o1, o2)), 0)
                                .intValue();
                    }
                };
            }
            List<Object> retval = new ArrayList((List<Object>) member);

            Collections.sort(retval, comparator);

            return retval;
        }
    }
}
