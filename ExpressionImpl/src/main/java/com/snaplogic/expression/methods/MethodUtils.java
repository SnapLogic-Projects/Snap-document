package com.snaplogic.expression.methods;

import com.snaplogic.expression.ObjectType;
import com.snaplogic.snap.api.SnapDataException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class MethodUtils {
    /**
     * Returns an index for the given argument.
     *
     * @param arg        method argument
     * @param defaultIdx default index
     *
     * @return index
     */
    public static final int getIndex(Object arg, int defaultIdx) {
        Number num = defaultIdx;
        try {
            num = (Number) arg;
        } catch (ClassCastException e) {
            try {
                num = new BigInteger(arg.toString());
            } catch (NumberFormatException numEx) {
                // ignore exception javascript defaults index to 0 if not a number
            }
        }
        return num.intValue();
    }

    /**
     * @param args The arguments to the method.
     * @param index The index where a function is expected.
     * @return A JavascriptFunction object that can be used as a callback.
     */
    public static final JavascriptFunction getFunction(List args, int index) {
        if (index >= args.size()) {
            throw new SnapDataException("Not enough arguments")
                    .withReason(String.format("Expecting a function parameter at index %s", index))
                    .withResolution("Pass a global function or an arrow function that can be used" +
                            " as a callback");
        }

        Object retval = args.get(index);

        if (!(retval instanceof JavascriptFunction)) {
            throw new SnapDataException("Expecting a callback function")
                    .withReason(String.format("Expecting a function parameter at index %s", index))
                    .withResolution("Pass a global function or an arrow function that can be used" +
                            " as a callback");
        }

        return (JavascriptFunction) retval;
    }

    /**
     * Returns an index for the given argument. In javascript, numbers as strings are converted to
     * numbers and non numbers default to 0 for some methods.
     *
     * @param arg method argument
     *
     * @return index
     */
    public static final int getIndex(Object arg) {
        return getIndex(arg, 0);
    }

    /**
     * Get the argument at the given index and cast it to a Number.
     *
     * @param args The argument list passed to a Method.
     * @param index The index into the argument list.
     * @return The argument as a Number or zero if the argument was missing or could not
     *         be converted.
     */
    public static final Number getArgAsNumber(List<Object> args, int index) {
        if (index >= args.size()) {
            return 0;
        }

        Object arg = args.get(index);
        Number num = 0;
        try {
            num = (Number) arg;
        } catch (ClassCastException e) {
            try {
                num = new BigInteger(arg.toString());
            } catch (NumberFormatException numEx) {
                // ignore exception javascript defaults index to 0 if not a number
            }
        }

        return num;
    }

    public static BigDecimal getArgAsBigDecimal(List<Object> args, int index) {
        if (index >= args.size()) {
            return BigDecimal.ZERO;
        }

        Object obj = ObjectType.attemptToConvertToBigDecimal(args.get(index));
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        throw new SnapDataException("Unable to convert argument %s into number, found: %s")
                .formatWith(index, ObjectType.objectToType(obj))
                .withReason("The function is expecting a number argument")
                .withResolution("Please check your expression syntax");
    }
}

