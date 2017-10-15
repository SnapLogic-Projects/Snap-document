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

package com.snaplogic.expression.methods.bytearray;

import com.google.common.collect.ImmutableMap;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.Method;
import com.snaplogic.expression.methods.MethodUtils;
import com.snaplogic.expression.methods.UnknownMethodException;
import com.snaplogic.expression.methods.object.ObjectMethod;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for list methods in expression language.
 */
public class ByteArrayMethod extends ObjectMethod {
    private static final String METHOD_SEPARATOR = ",";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, Method> ARRAY_METHODS =
            ImmutableMap.<String, Method>builder()
                    .put("concat", Concat.INSTANCE)
                    .put("subarray", Subarray.INSTANCE)
                    .put("indexOf", IndexOf.INSTANCE)
                    .put("lastIndexOf", LastIndexOf.INSTANCE)
                    .putAll(OBJECT_METHODS)
                    .build();


    public static Method getMethod(String methodName) {
        Method method = ARRAY_METHODS.get(methodName);
        if (method == null) {
            throw new UnknownMethodException(ARRAY_METHODS.keySet());
        }
        return method;
    }

    /**
     * Implements a custom concat() method for Uint8Arrays.
     */
    public enum Concat implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            List<byte[]> allBits = new ArrayList<>();
            byte[] bits = (byte[]) member;
            int totalLength = bits.length;

            allBits.add(bits);
            for (Object arg : args) {
                if (arg instanceof byte[]) {
                    bits = (byte[]) arg;
                } else {
                    bits = ObjectType.toString(arg).getBytes(StandardCharsets.UTF_8);
                }
                allBits.add(bits);
                totalLength += bits.length;
            }

            byte[] retval = new byte[totalLength];
            int offset = 0;

            for (byte[] elem : allBits) {
                System.arraycopy(elem, 0, retval, offset, elem.length);
                offset += elem.length;
            }

            return retval;
        }
    }

    /**
     * Implements the subarray() method for Uint8Arrays.
     */
    public enum Subarray implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            byte[] bits = (byte[]) member;
            int argsSize = args.size();
            int startIndex = 0;
            int endIndex = bits.length;

            if (argsSize == 0) {
                return bits;
            }
            if (argsSize >= 1) {
                startIndex = MethodUtils.getIndex(args.get(0));
            }
            if (argsSize >= 2) {
                endIndex = MethodUtils.getIndex(args.get(1), bits.length);
            }
            return ArrayUtils.subarray(bits, startIndex, endIndex);
        }
    }

    /**
     * Implements the indexOf() method for Uint8Arrays.
     */
    public enum IndexOf implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            byte[] bits = (byte[]) member;
            int retval = -1;

            if (args.size() > 0) {
                Object arg0 = args.get(0);
                int offset = 0;
                Number num = ObjectType.toNumber(arg0, 0);
                byte needle = num.byteValue();

                if (args.size() > 1) {
                    offset = MethodUtils.getIndex(args.get(1));
                }

                retval = ArrayUtils.indexOf(bits, needle, offset);
            }

            return BigInteger.valueOf(retval);
        }
    }

    /**
     * Implements the lastIndexOf() method for Uint8Arrays.
     */
    public enum LastIndexOf implements Method {
        INSTANCE;

        @Override
        public Object evaluate(final Object member, final List args) {
            byte[] bits = (byte[]) member;
            int retval = -1;

            if (args.size() > 0) {
                Object arg0 = args.get(0);
                int offset = bits.length;
                Number num = ObjectType.toNumber(arg0, 0);
                byte needle = num.byteValue();

                if (args.size() > 1) {
                    offset = MethodUtils.getIndex(args.get(1));
                }

                retval = ArrayUtils.lastIndexOf(bits, needle, offset);
            }

            return BigInteger.valueOf(retval);
        }
    }
}
