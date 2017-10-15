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

import com.snaplogic.jsonpath.PathParser;

import java.util.List;

/**
 * Represents a path to sort on in a JSON-path ordering clause.
 *
 * @author tstack
 */
public class OrderingToken extends PathToken {
    private final Order order;
    private final List<PathToken> path;

    /**
     * The possible order directions.
     */
    public enum Order {
        ASCENDING,
        DESCENDING
    }

    /**
     * @param order The order direction, a '<' in the path represents ASCENDING and
     *              a '>' represents DESCENDING.
     * @param path The path to the object to sort on.
     */
    public OrderingToken(Order order, List<PathToken> path) {
        this.order = order;
        this.path = path;
    }

    public Order getOrder() {
        return order;
    }

    public List<PathToken> getPath() {
        return path;
    }

    /**
     * Given a comparison result, return a result adjust to the ordering in this object.
     *
     * @param cmp The comparison result to adjust.
     * @return If the order in this object is ASCENDING, the same value is returned.
     *         Otherwise, the comparison is negated and returned.
     */
    public int checkOrder(int cmp) {
        if (cmp == 0) {
            return 0;
        }

        if (order == Order.DESCENDING) {
            return -cmp;
        }

        return cmp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderingToken)) {
            return false;
        }

        final OrderingToken that = (OrderingToken) o;

        if (order != that.order) {
            return false;
        }
        if (!path.equals(that.path)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = order.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s%s",
                order == Order.ASCENDING ? "<" : ">",
                PathParser.unparse(path));
    }
}
