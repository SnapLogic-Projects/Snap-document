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

package sl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ListBuilder is the utility class that helps is building list with initial data. This is
 * similar to guava's immutable list builder. Difference is this one allows addition of null values.
 *
 * @author ksubramanian
 * @since 2015
 */
public class ListBuilder {

    private final List list;

    private ListBuilder() {
        this.list = new ArrayList();
    }

    /**
     * Returns a list builder.
     *
     * @return builder
     */
    public static ListBuilder builder() {
        return new ListBuilder();
    }

    /**
     * Adds a value to the list.
     *
     * @param key
     * @return builder
     */
    public ListBuilder add(Object key) {
        this.list.add(key);
        return this;
    }

    public ListBuilder addAll(Object val) {
        if (val == null) {
            // Do nothing
        } else if (!(val instanceof Collection)) {
            // ECMAScript expects an iterable and raises an error.  I'm gonna just be nice and
            // allow anything through.
            this.list.add(val);
        } else {
            this.list.addAll((Collection) val);
        }
        return this;
    }

    /**
     * Returns the list that was build.
     *
     * @return list
     */
    public List get() {
        return this.list;
    }
}
