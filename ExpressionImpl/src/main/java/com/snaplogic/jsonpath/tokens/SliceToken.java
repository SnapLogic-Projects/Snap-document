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

package com.snaplogic.jsonpath.tokens;

import com.snaplogic.common.jsonpath.InvalidPathException;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;
import com.snaplogic.jsonpath.UnexpectedTypeException;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static com.snaplogic.jsonpath.tokens.Messages.*;

/**
 * A token used to represent an array-slice operation.
 *
 * @author tstack
 */
public class SliceToken extends TraversalToken {
    private static final String SLICE_FORMAT = "%s:%s:%s";

    private final ExpressionToken start;
    private final ExpressionToken end;
    private final ExpressionToken step;

    /**
     * @param start The expression for the start index or null.
     * @param end The expression for the end index or null.
     * @param step The expression for the step count or null.
     */
    public SliceToken(ExpressionToken start, ExpressionToken end, ExpressionToken step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    /**
     * Convert an expression into an integer.  If the expression evaluates to
     * a number, we convert it into an integer and use that.  Also, to be nice,
     * we convert strings that are integers.
     *
     * @param walker The walker that will evaluate the expression.
     * @param expr The expression to evaluate.
     * @param listObj The list object to pass to the evaluator.
     * @return The integer value of the expression.
     * @throws InvalidPathException If there was a problem parsing the expression.
     */
    private int convertExpr(PathWalker walker, int index, ExpressionToken expr,
                            List listObj, PathVisitor visitor) throws InvalidPathException {
        Object result = walker.evaluate(expr.getCode(), visitor, null, listObj);

        if (result instanceof String) {
            try {
                return Integer.valueOf((String) result);
            } catch (NumberFormatException e) {
                throw new UnexpectedTypeException(Number.class,
                        String.format(UNABLE_TO_CONVERT_INT, result), e)
                        .withResolution(ENTER_AN_INTEGER_VALUE_FOR_THE_SLICE_EXPR);
            }
        } else if (result instanceof Number) {
            return walker.convertListIndex(index, (Number) result);
        }

        throw new UnexpectedTypeException(Number.class,
                String.format(SLICE_VALUE_NOT_AN_INTEGER, expr))
                .withResolution(ENTER_AN_INTEGER_VALUE_FOR_THE_SLICE_EXPR);
    }

    /**
     * Convert an expression into an index.  This method is an extra wrapper
     * around convertExpr that ensures that the index is within the array bounds.
     *
     * @param walker The walker that will evaluate the expression.
     * @param expr The expression to evaluate.
     * @param listObj The list object to pass to the evaluator.
     * @return The integer value of the expression.
     * @throws InvalidPathException If there was a problem parsing the expression.
     */
    private int convertIndex(PathWalker walker, int index, ExpressionToken expr,
                             List listObj, PathVisitor visitor) throws InvalidPathException {
        int retval = convertExpr(walker, index, expr, listObj, visitor);
        int listSize = listObj.size();

        if (retval < 0) {
            retval = listSize + retval;
            if (retval < 0) {
                retval = 0;
            }
        }

        if (retval > listSize) {
            retval = listSize;
        }

        return retval;
    }

    @Override
    public void traverse(final PathWalker walker, final int index, final Object obj,
                         final PathVisitor visitor) throws InvalidPathException {
        if (!(obj instanceof List)) {
            throw new UnexpectedTypeException(List.class,
                    String.format(EXPECTING_ARRAY, obj))
                    .withPath(walker.subpath(index))
                    .withParent(obj)
                    .withResolution(CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_ARRAY);
        }

        List listObj = (List) obj;
        int listSize = listObj.size();
        List<Integer> arrayIndexes = new ArrayList<>();
        int startIndex = 0, endIndex = listSize, stepCount = 1;

        if (start != null) {
            startIndex = convertIndex(walker, index, start, listObj, visitor);
        }
        if (end != null) {
            endIndex = convertIndex(walker, index, end, listObj, visitor);
        }
        if (step != null) {
            stepCount = convertExpr(walker, index, step, listObj, visitor);
        }

        if (stepCount == 0) {
            throw new InvalidPathException(SLICE_STEP_CANNOT_BE_ZERO)
                    .withPath(walker.subpath(index))
                    .withResolution(USE_A_POSITIVE_OR_NEGATIVE_NUMBER_FOR_THE_STEP);
        }
        if (stepCount < 0) {
            int tmp = startIndex;

            startIndex = endIndex - 1;
            endIndex = tmp;
        }

        for (int i = startIndex; stepCount < 0 ? i >= endIndex : i < endIndex; i += stepCount) {
            arrayIndexes.add(i);
        }

        int[] primIndexes = ArrayUtils.toPrimitive(arrayIndexes.toArray(new Integer[0]));

        walker.traverseList(index, (List) obj, primIndexes, visitor);
    }

    @Override
    public boolean isBranchingToken() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SliceToken)) {
            return false;
        }

        final SliceToken that = (SliceToken) o;

        if (end != null ? !end.equals(that.end) : that.end != null) {
            return false;
        }
        if (start != null ? !start.equals(that.start) : that.start != null) {
            return false;
        }
        if (step != null ? !step.equals(that.step) : that.step != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (step != null ? step.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(SLICE_FORMAT,
                start != null ? start : "",
                end != null ? end : "",
                step != null ? step : "");
    }
}
