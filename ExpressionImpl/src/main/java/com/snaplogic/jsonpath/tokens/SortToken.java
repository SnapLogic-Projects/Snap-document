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
import com.snaplogic.expression.ObjectType;
import com.snaplogic.jsonpath.CompiledExpressionHolder;
import com.snaplogic.jsonpath.PathVisitor;
import com.snaplogic.jsonpath.PathWalker;
import com.snaplogic.jsonpath.UnexpectedTypeException;
import com.snaplogic.snap.api.SnapDataException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.snaplogic.jsonpath.tokens.Messages.*;

/**
 * Implements the '.sort_asc()' and 'sort_desc()' json-path methods.
 *
 * @author tstack
 */
public class SortToken extends MethodToken {

    /**
     * Sort ordering.
     */
    public enum Ordering {
        ASCENDING("asc"),
        DESCENDING("desc");

        private final String token;

        Ordering(final String token) {
            this.token = token;
        }

        public String getToken() {
            return this.token;
        }
    }

    private final Ordering ordering;

    public SortToken(Ordering ordering, final String value, final CompiledExpressionHolder code) {
        super(value, code);
        this.ordering = ordering;
    }

    private int checkOrder(int cmp) {
        if (cmp == 0) {
            return 0;
        }

        if (ordering == Ordering.DESCENDING) {
            return -cmp;
        }

        return cmp;
    }

    @Override
    public Object process(final PathWalker walker, final PathVisitor visitor, final Object obj)
            throws InvalidPathException {
        if (obj instanceof List) {
            // TODO tstack - Instead of a copy, we should return a view of the original
            // list so that a subsequent visitor can do writes/deletes.
            List<Object> copy = new ArrayList<>((List<Object>) obj);

            try {
                Collections.sort(copy, new MyComparator(walker, visitor));
            } catch (SnapDataException e) {
                if (e.getCause() instanceof InvalidPathException) {
                    throw (InvalidPathException) e.getCause();
                }
            }

            return copy;
        } else if (obj == null) {
            return obj;
        } else {
            throw new UnexpectedTypeException(List.class, String.format(EXPECT_AN_ARRAY_TO_SORT,
                    ObjectType.objectToType(obj).getName()))
                    .withResolution(CHANGE_THE_PRECEDING_PATH_TO_REFER_TO_AN_ARRAY);
        }
    }

    @Override
    public String toString() {
        return String.format("sort_%s(%s)", ordering.getToken(), this.value);
    }

    private class MyComparator implements Comparator<Object> {
        private final PathWalker walker;
        private final PathVisitor visitor;

        public MyComparator(final PathWalker walker, final PathVisitor visitor) {
            this.walker = walker;
            this.visitor = visitor;
        }

        @Override
        public int compare(final Object o1, final Object o2) {
            try {
                Object o1value = walker.evaluate(code, visitor, null, o1);
                Object o2value = walker.evaluate(code, visitor, null, o2);

                return compareInternal(o1value, o2value);
            } catch (InvalidPathException e) {
                throw new SnapDataException(e, INVALID_SORT_EXPRESSION);
            }
        }

        private int compareInternal(final Object o1value, final Object o2value) {
            if (o1value == o2value) {
                return 0;
            }

            // Consider nulls as less than everything else.
            if (o1value == null) {
                return checkOrder(-1);
            }
            if (o2value == null) {
                return checkOrder(1);
            }

            if (o1value instanceof List && o2value instanceof List) {
                List o1list = (List) o1value, o2list = ((List) o2value);

                for (int i = 0; i < Math.min(o1list.size(), o2list.size()); i++) {
                    int rc = compareInternal(o1list.get(i), o2list.get(i));
                    if (rc != 0) {
                        return checkOrder(rc);
                    }
                }
                if (o1list.isEmpty() && o2list.isEmpty()) {
                    return 0;
                } else if (o1list.isEmpty()) {
                    return checkOrder(-1);
                } else {
                    return checkOrder(1);
                }
            }

            int rc;

            if (!(o1value instanceof Comparable) ||
                    !(o2value instanceof Comparable)) {
                // If one of the objects is not Comparable, try comparing
                // their string representations.
                rc = o1value.toString().compareTo(o2value.toString());
            } else {
                Comparable o1cmp = (Comparable) o1value;
                Comparable o2cmp = (Comparable) o2value;

                if (o1cmp instanceof Number && o2cmp instanceof Number) {
                    o1cmp = new BigDecimal(o1cmp.toString());
                    o2cmp = new BigDecimal(o2cmp.toString());
                }

                try {
                    rc = o1cmp.compareTo(o2cmp);
                } catch (ClassCastException e) {
                    rc = o1value.toString().compareTo(o2value.toString());
                }
            }
            return checkOrder(rc);
        }
    }
}
