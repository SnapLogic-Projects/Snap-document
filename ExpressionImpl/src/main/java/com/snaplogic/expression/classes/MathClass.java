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
import com.snaplogic.common.expressions.Scope;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.ObjectType;
import com.snaplogic.expression.methods.JavascriptFunction;
import sl.EvaluatorUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Mimics javascript Math class.
 *
 * @author mklumpp
 */
public enum MathClass implements JavascriptClass, Scope {
    INSTANCE;

    @Override
    public boolean evalInstanceOf(final Object value) {
        return false;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static final Map<String, JavascriptFunction> STRING_TO_METHOD =
            new ImmutableMap.Builder<String, JavascriptFunction>()
                    .put("abs", Abs.INSTANCE)
                    .put("ceil", Ceil.INSTANCE)
                    .put("floor", Floor.INSTANCE)
                    .put("max", Max.INSTANCE)
                    .put("min", Min.INSTANCE)
                    .put("pow", Pow.INSTANCE)
                    .put("random", Random.INSTANCE)
                    .put("randomUUID", RandomUUID.INSTANCE)
                    .put("sign", Sign.INSTANCE)
                    .put("sqrt", Sqrt.INSTANCE)
                    .put("trunc", Trunc.INSTANCE)
                    .build();

    private static final Map<String, Object> STRING_TO_MEMBER =
            new ImmutableMap.Builder<String, Object>()
                    .putAll(STRING_TO_METHOD)
                    .put("E", new BigDecimal("2.718281828459045"))
                    .put("LN2", new BigDecimal("0.6931471805599453"))
                    .put("LN10", new BigDecimal("2.302585092994046"))
                    .put("LOG2E", new BigDecimal("1.4426950408889634"))
                    .put("LOG10E", new BigDecimal("0.4342944819032518"))
                    .put("PI", new BigDecimal("3.141592653589793"))
                    .put("SQRT1_2", new BigDecimal("0.7071067811865476"))
                    .put("SQRT2", new BigDecimal("1.4142135623730951"))
                    .build();

    public Object evalStaticMethod(ScopeStack scopes, String methodName,
                                   List<Object> args) {
        JavascriptFunction method = STRING_TO_METHOD.get(methodName);
        if (method == null) {
            EvaluatorUtils.undefinedStaticMethod("Math", STRING_TO_METHOD.keySet(), methodName);
        }
        return method.eval(args);
    }

    @Override
    public Object get(String name) {
        Object retval = STRING_TO_MEMBER.get(name);
        if (retval == null) {
            retval = UNDEFINED;
        }
        return retval;
    }

    @Override
    public Set<String> keySet() {
        return STRING_TO_MEMBER.keySet();
    }

    private static Object attemptToConvertArgs(List<Object> args, final int index) {
        if (index >= args.size()) {
            return Double.NaN;
        }
        Object retval = ObjectType.attemptToConvertToBigDecimal(args.get(index));
        if (retval instanceof BigDecimal) {
            return retval;
        }
        return Double.NaN;
    }

    /**
     * Mimics javascript Math.abs method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/abs
     */
    public enum Abs implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object argument = attemptToConvertArgs(args, 0);
            if (!argument.equals(Double.NaN)) {
                BigDecimal num = ((BigDecimal) argument);
                return num.abs();
            }
            return Double.NaN;
        }
    }

    /**
     * Mimics javascript Math.random method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
     */
    public enum Random implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            EvaluatorUtils.CONTEXT_THREAD_LOCAL.get().impure = true;
            return BigDecimal.valueOf(Math.random());
        }
    }

    /**
     * Mimics javascript Math.ceil method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/ceil
     */
    public enum Ceil implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object argument = attemptToConvertArgs(args, 0);
            if (!argument.equals(Double.NaN)) {
                BigDecimal num = ((BigDecimal) argument);
                return num.setScale(0, BigDecimal.ROUND_CEILING).toBigInteger();
            }
            return Double.NaN;
        }
    }

    /**
     * Mimics javascript Math.floor method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/floor
     */
    public enum Floor implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object argument = attemptToConvertArgs(args, 0);
            if (!argument.equals(Double.NaN)) {
                BigDecimal num = ((BigDecimal) argument);
                return num.setScale(0, BigDecimal.ROUND_FLOOR).toBigInteger();
            }
            return Double.NaN;
        }
    }

    /**
     * Mimics javascript Math.max method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/max
     */
    public enum Max implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            if (args.isEmpty()) {
                return Double.NEGATIVE_INFINITY;
            }
            BigDecimal retval = null;
            for (Object arg : args) {
                Object converted = ObjectType.attemptToConvertToBigDecimal(arg);
                if (converted instanceof BigDecimal) {
                    BigDecimal bigDecimal = ((BigDecimal) converted);
                    if (retval == null || bigDecimal.compareTo(retval) > 0) {
                        retval = bigDecimal;
                    }
                } else {
                    return Double.NaN;
                }
            }
            return retval;
        }
    }

    /**
     * Mimics javascript Math.min method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/max
     */
    public enum Min implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            if (args.isEmpty()) {
                return Double.POSITIVE_INFINITY;
            }
            BigDecimal retval = null;
            for (Object arg : args) {
                Object converted = ObjectType.attemptToConvertToBigDecimal(arg);
                if (converted instanceof BigDecimal) {
                    BigDecimal bigDecimal = ((BigDecimal) converted);
                    if (retval == null || bigDecimal.compareTo(retval) < 0) {
                        retval = bigDecimal;
                    }
                } else {
                    return Double.NaN;
                }
            }
            return retval;
        }
    }

    /**
     * Mimics javascript Math.pow method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/pow
     */
    public enum Pow implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object argument1 = attemptToConvertArgs(args, 0);
            Object argument2 = attemptToConvertArgs(args, 1);
            if (!argument1.equals(Double.NaN) && !argument2.equals(Double.NaN)) {
                BigDecimal num1 = ((BigDecimal) argument1);
                BigDecimal num2 = ((BigDecimal) argument2);
                return num1.pow(num2.intValue());
            }
            return Double.NaN;
        }
    }

    /**
     * Allows to create a random and unique UUID.
     */
    public enum RandomUUID implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            EvaluatorUtils.CONTEXT_THREAD_LOCAL.get().impure = true;
            return UUID.randomUUID().toString();
        }
    }

    /**
     * Mimics javascript Math.sign method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/sign
     */
    public enum Sign implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object argument = attemptToConvertArgs(args, 0);
            if (!argument.equals(Double.NaN)) {
                BigDecimal num = ((BigDecimal) argument);
                int signum = num.signum();
                switch (signum) {
                    case 0:
                        return BigInteger.ZERO;
                    case 1:
                        return BigInteger.ONE;
                    default:
                        return BigInteger.valueOf(signum);
                }
            }
            return Double.NaN;
        }
    }

    /**
     * Mimics javascript Math.sqrt method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/sqrt
     */
    public enum Sqrt implements JavascriptFunction {
        INSTANCE;

        private static final int DEFAULT_SCALE = 20;
        private static final BigDecimal TWO = BigDecimal.valueOf(2);

        /**
         * Lifted from:
         *
         * https://github.com/ChiralBehaviors/IBE/blob/master/framework/src/main/java/nuim/cs/
         * crypto/util/BigSquareRoot.java
         *
         * @param n the value of x
         * @param scale`the desired scale of the result
         * @return the result value
         */

        protected Object sqrt(BigDecimal n, int scale) {
            switch (n.compareTo(BigDecimal.ZERO)) {
                case 0:
                    return BigDecimal.ZERO;
                case -1:
                    return Double.NaN;
            }
            BigDecimal guess = BigDecimal.ONE;
            BigDecimal lastGuess;
            boolean more = true;
            for (int i = 0; i < 50 && more; i++) {
                lastGuess = new BigDecimal(guess.toString());
                guess = n.divide(lastGuess, scale, BigDecimal.ROUND_HALF_UP);
                guess = guess.add(lastGuess);
                guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);

                BigDecimal error = n.subtract(guess.multiply(guess));
                if (lastGuess.equals(guess)) {
                    more = error.abs().compareTo(BigDecimal.ONE) >= 0;
                }
            }

            return guess.stripTrailingZeros();
        }

        @Override
        public Object eval(final List<Object> args) {
            Object argument = attemptToConvertArgs(args, 0);
            if (!argument.equals(Double.NaN)) {
                BigDecimal num = ((BigDecimal) argument);
                return sqrt(num, num.scale() > DEFAULT_SCALE ? num.scale() : DEFAULT_SCALE);
            }
            return Double.NaN;
        }
    }

    /**
     * Mimics javascript Math.trunc method.
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/trunc
     */
    public enum Trunc implements JavascriptFunction {
        INSTANCE;

        @Override
        public Object eval(final List<Object> args) {
            Object argument = attemptToConvertArgs(args, 0);
            if (!argument.equals(Double.NaN)) {
                BigDecimal num = ((BigDecimal) argument);
                return num.setScale(0, BigDecimal.ROUND_DOWN).toBigInteger();
            }
            return Double.NaN;
        }
    }
}
